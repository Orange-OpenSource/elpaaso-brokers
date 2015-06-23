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
import com.orange.clara.cloud.cf.servicebroker.dbaas.domain.TestUser;
import com.orange.clara.cloud.dbaas.ws.api.data.CreateDatabaseResponseObject;
import com.orange.clara.cloud.dbaas.ws.api.enumeration.*;
import com.orange.clara.cloud.dbaas.ws.api.response.DescribeDatabaseResponse;
import com.orange.clara.cloud.dbaas.ws.api.service.DbaasApiRemote;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by sbortolussi on 16/04/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class DBaasAdapterTest {

    public static final String DBAAS_GROUP_NAME = "group_name";
    public static final TestUser DEFAULT_OWNER = new TestUser();

    @Mock
    DbaasApiRemote dbaasJaxWsProxy;

    @Test
    public void should_create_database() throws Exception {
        final DBaasAdapter dBaasAdapter = new DBaasAdapter(dbaasJaxWsProxy, DBAAS_GROUP_NAME);
        final CreateDatabaseResponseObject responseObject = new CreateDatabaseResponseObject();
        responseObject.setDatabaseUUId("99");
        Mockito.when(dbaasJaxWsProxy.createDatabase(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(ServiceClassWsEnum.class), Mockito.any(EngineWsEnum.class), Mockito.anyString(), Mockito.anyList(), Mockito.any(SloWsEnum.class), Mockito.anyBoolean(), Mockito.any(UsageWsEnum.class), Mockito.anyString(), Mockito.any(NetworkZoneWsEnum.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(BackupPlanWsEnum.class),Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyString())).thenReturn(responseObject);
        final String dbId = dBaasAdapter.createDatabase(DBaasPlan.MYSQL_1G, DEFAULT_OWNER);
        Assertions.assertThat(dbId).isEqualTo("99");
    }

    @Test
    public void should_map_dbaas_protocol_with_value_mysql_to_broker_protocol_value_mysql() throws Exception {
        final DBaasAdapter dBaasAdapter = new DBaasAdapter(dbaasJaxWsProxy, DBAAS_GROUP_NAME);
        final DescribeDatabaseResponse database = new DescribeDatabaseResponse();
        database.setEndpointProtocol("mysql");
        Assertions.assertThat(dBaasAdapter.getEndpointProtocol(database)).isEqualTo("mysql");
    }

    @Test
    public void should_map_dbaas_protocol_with_value_postgresql_to_broker_protocol_value_postgres() throws Exception {
        final DBaasAdapter dBaasAdapter = new DBaasAdapter(dbaasJaxWsProxy, DBAAS_GROUP_NAME);
        final DescribeDatabaseResponse database = new DescribeDatabaseResponse();
        database.setEndpointProtocol("postgresql");
        Assertions.assertThat(dBaasAdapter.getEndpointProtocol(database)).isEqualTo("postgres");
    }

    @Test
    public void testToSize() throws Exception {

    }

    @Test
    public void testToEngine() throws Exception {

    }

    @Test
    public void testDeleteDb() throws Exception {

    }

    @Test
    public void testGetDatabaseCredentials() throws Exception {

    }

    @Test
    public void testAssertDatabaseIsActive() throws Exception {

    }

    @Test
    public void testAssertDatabaseIsDeleted() throws Exception {

    }
}