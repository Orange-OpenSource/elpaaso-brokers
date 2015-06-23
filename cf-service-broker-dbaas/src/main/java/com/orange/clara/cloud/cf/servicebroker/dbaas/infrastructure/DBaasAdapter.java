/**
 * Copyright (C) 2015 Orange
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orange.clara.cloud.cf.servicebroker.dbaas.infrastructure;

import com.orange.clara.cloud.cf.servicebroker.dbaas.domain.DBaasPlan;
import com.orange.clara.cloud.cf.servicebroker.dbaas.domain.DBaasUser;
import com.orange.clara.cloud.cf.servicebroker.dbaas.domain.DatabaseCredentials;
import com.orange.clara.cloud.dbaas.ws.api.data.*;
import com.orange.clara.cloud.dbaas.ws.api.enumeration.*;
import com.orange.clara.cloud.dbaas.ws.api.response.DescribeDatabaseResponse;
import com.orange.clara.cloud.dbaas.ws.api.service.DatabaseAlreadyDeletedFault;
import com.orange.clara.cloud.dbaas.ws.api.service.DbaasApiRemote;
import com.orange.clara.cloud.dbaas.ws.api.service.UnknownDatabaseFault;
import com.orange.clara.cloud.dbaas.ws.api.service.UnknownUserFault;
import org.cloudfoundry.community.servicebroker.model.OperationState;
import org.cloudfoundry.community.servicebroker.model.ServiceInstanceLastOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sbortolussi on 09/04/2015.
 */

@Service
public class DBaasAdapter {

    public static final UsageWsEnum USAGE = UsageWsEnum.DEVTEST;
    public static final boolean IS_SOX = false;
    public static final String DATACENTER = "Cube";
    public static final String DATABASE_PARAMETERS_SET_NAME = "databaseParametersSetName";
    public static final String MAINTENANCE_WINDOW = "Sun:20:00-04";
    public static final boolean IS_AUTO_UPGRADE = false;
    public static final NetworkZoneWsEnum NETWORK_ZONE = NetworkZoneWsEnum.RSC;
    public static final SloWsEnum SLO = SloWsEnum.STANDARD;
    public static final ServiceClassWsEnum SERVICE_CLASS = ServiceClassWsEnum.XXXS;
    public static final String UNDEFINED_DATABASE_NAME = null;
    public static final BackupPlanWsEnum BACKUP_PLAN = BackupPlanWsEnum.NONE;
    public static final String BACKUP_WINDOW = "20:00-4";


    private DbaasApiRemote dbaasProxy;
    private String dbaasGroupName;
    public static final String DESCRIPTION = "generated from dbaas cloudfoundry service broker";

    private static final Logger LOGGER = LoggerFactory.getLogger(DBaasAdapter.class);


    @Autowired
    public DBaasAdapter(DbaasApiRemote dbaasProxy, @Value("${dbaas.api.groupname}") String dbaasGroupName) {
        this.dbaasProxy = dbaasProxy;
        this.dbaasGroupName = dbaasGroupName;
    }

    public String createDatabase(DBaasPlan plan, DBaasUser defaultOwner) {

        // Size to pass to DBaaS stub is in Go
        int size = toSize(plan);
        EngineWsEnum engine = toEngine(plan);
        List<DatabaseUserInfo> users = Arrays.asList(toDatabaseUserInfo(defaultOwner));

        final CreateDatabaseResponseObject response;
        try {
            response = dbaasProxy.createDatabase(UNDEFINED_DATABASE_NAME, dbaasGroupName, size, SERVICE_CLASS, engine, null, users,
                    SLO, IS_SOX, USAGE, DATACENTER, NETWORK_ZONE, "RSC", DATABASE_PARAMETERS_SET_NAME, MAINTENANCE_WINDOW, BACKUP_PLAN, BACKUP_WINDOW,
                    IS_AUTO_UPGRADE, DESCRIPTION);
        } catch (Exception e) {
            throw new DatabaseCreationFailed(e);
        }
        return response.getDatabaseUUId();
    }

    protected DatabaseUserInfo toDatabaseUserInfo(DBaasUser dBaasUser) {
        DatabaseUserInfo user = new DatabaseUserInfo();
        user.setLogin(dBaasUser.getName());
        user.setPassword(dBaasUser.getPassword());
        user.setDatabaseUserType(DatabaseUserTypeWsEnum.OWNER);
        return user;
    }

    protected int toSize(DBaasPlan plan) {
        if (DBaasPlan.POSTGRESQL_1G.equals(plan)) return 1;
        if (DBaasPlan.MYSQL_1G.equals(plan)) return 1;
        throw new UnsupportedPlanException(plan);
    }

    protected EngineWsEnum toEngine(DBaasPlan plan) {
        if (DBaasPlan.POSTGRESQL_1G.equals(plan)) return EngineWsEnum.POSTGRESQL;
        if (DBaasPlan.MYSQL_1G.equals(plan)) return EngineWsEnum.MYSQL;
        throw new UnsupportedPlanException(plan);
    }

    public void deleteDb(String uid) {
        try {
            dbaasProxy.deleteDatabase(uid);
        } catch (DatabaseAlreadyDeletedFault e) {
            LOGGER.warn("Will not delete database <{}>. Database does not exist in dbaas.", uid);
        } catch (UnknownDatabaseFault e) {
            LOGGER.warn("Will not delete database <{}>. Database does not exist in dbaas.", uid);
        } catch (Exception e) {
            throw new DatabaseDeletionFailed(e);
        }

    }

    public void deleteUser(String databaseId, DBaasUser user) {
        try {
            dbaasProxy.dropDatabaseUser(databaseId, user.getName());
        } catch (UnknownUserFault e) {
            LOGGER.warn("Will not delete user <{}>. User does not exist in dbaas.", user);
        } catch (Exception e) {
            throw new DatabaseDeletionFailed(e);
        }

    }

    public DatabaseCredentials getDatabaseCredentials(String databaseUID) {
        try {
            final DescribeDatabaseResponse database = dbaasProxy.describeDatabase(databaseUID);
            return new DatabaseCredentials(database.getDatabaseName(), getEndpointProtocol(database), database.getEndPointFQDN(), database.getEndPointTCPPort());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String getEndpointProtocol(DescribeDatabaseResponse database) {
        if ("mysql".equalsIgnoreCase(database.getEndpointProtocol())) return "mysql";
        if ("postgresql".equalsIgnoreCase(database.getEndpointProtocol())) return "postgres";
        throw new IllegalStateException("database protocol <" + database.getEndpointProtocol() + "> is not supported by dbaas broker.");
    }

    private boolean databaseActive(DescribeDatabaseResponse database) {
        return ServiceStateWsEnum.ACTIVE.equals(database.getDatabaseState());
    }

    private boolean databaseDeleted(DescribeDatabaseResponse database) {
        return ServiceStateWsEnum.DELETED.equals(database.getDatabaseState());
    }

    private boolean databaseFailed(DescribeDatabaseResponse database) {
        return ServiceStateWsEnum.INCIDENT.equals(database.getDatabaseState());
    }

    public ServiceInstanceLastOperation getDatabaseState(String databaseId) {
        try {
            final DescribeDatabaseResponse database = dbaasProxy.describeDatabase(databaseId);
            if (databaseFailed(database)) return new ServiceInstanceLastOperation("", OperationState.FAILED);
            if (databaseDeleted(database) || databaseActive(database)) return new ServiceInstanceLastOperation("", OperationState.SUCCEEDED);
            return new ServiceInstanceLastOperation("", OperationState.IN_PROGRESS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private class UnsupportedPlanException extends RuntimeException {
        public UnsupportedPlanException(DBaasPlan plan) {
            super("Cannot create database using plan<" + plan + ">. Plan is not supported by dbaas.");
        }
    }

    private class DatabaseCreationFailed extends RuntimeException {
        public DatabaseCreationFailed(Exception e) {
            super("Database creation has failed. Details :", e);
        }
    }

    private class DatabaseDeletionFailed extends RuntimeException {
        public DatabaseDeletionFailed(Exception e) {
            super("Database deletion has failed. Details : ", e);
        }
    }

    private class UserCreationFailed extends RuntimeException {
        public UserCreationFailed(Exception e) {
            super("User creation has failed. Details  : ", e);
        }
    }
}
