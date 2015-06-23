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
package com.orange.clara.cloud.cf.servicebroker.dbaas.domain;

import org.fest.assertions.Assertions;
import org.junit.Test;

/**
 * Created by sbortolussi on 24/04/2015.
 */
public class DatabaseCredentialsTest {

    @Test
    public void should_get_database_connection_uri() throws Exception {
        DatabaseCredentials databaseCredentials = new DatabaseCredentials("mya23bed", "mysql", "localhost", "3306");
        Assertions.assertThat(databaseCredentials.getUri(new DBaasUser("john", "password"))).isEqualTo("mysql://john:password@localhost:3306/mya23bed");
    }

    @Test
    public void should_get_database_jdbc_url() throws Exception {
        DatabaseCredentials databaseCredentials = new DatabaseCredentials("mya23bed", "mysql", "localhost", "3306");
        Assertions.assertThat(databaseCredentials.getJdbcUrl(new DBaasUser("john", "password"))).isEqualTo("jdbc:mysql://john:password@localhost:3306/mya23bed");
    }
}