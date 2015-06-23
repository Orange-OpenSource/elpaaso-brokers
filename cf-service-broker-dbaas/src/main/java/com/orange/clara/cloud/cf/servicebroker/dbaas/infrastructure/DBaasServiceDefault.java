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
import com.orange.clara.cloud.cf.servicebroker.dbaas.domain.DBaasService;
import com.orange.clara.cloud.cf.servicebroker.dbaas.domain.DBaasUser;
import com.orange.clara.cloud.cf.servicebroker.dbaas.domain.DatabaseCredentials;
import org.cloudfoundry.community.servicebroker.model.ServiceInstanceLastOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sbortolussi on 10/04/2015.
 */

@Service
public class DBaasServiceDefault implements DBaasService {

    private DBaasAdapter dBaasAdapter;

    @Autowired
    public DBaasServiceDefault(DBaasAdapter dBaasAdapter) {
        this.dBaasAdapter = dBaasAdapter;
    }

    public String createDatabase(DBaasPlan plan, DBaasUser defaultOwner) {
        return dBaasAdapter.createDatabase(plan, defaultOwner);
    }

    public void deleteDatabase(String databaseId) {
        dBaasAdapter.deleteDb(databaseId);
    }

    public DatabaseCredentials getDatabaseCredentials(String databaseId) {
        return dBaasAdapter.getDatabaseCredentials(databaseId);
    }

    public void deleteUserFromDatabase(String databaseId, DBaasUser user) {
        dBaasAdapter.deleteUser(databaseId, user);
    }

    public ServiceInstanceLastOperation getDatabaseState(String serviceInstanceId) {
        return dBaasAdapter.getDatabaseState(serviceInstanceId);
    }
}
