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

/**
 * Created by sbortolussi on 14/04/2015.
 */
public class DatabaseCredentials {

    private final String databaseName;
    private final String protocol;
    private final String hostname;
    private final String port;

    public DatabaseCredentials(String databaseName, String protocol, String hostname, String port) {
        this.databaseName = databaseName;
        this.protocol = protocol;
        this.hostname = hostname;
        this.port = port;
    }

    public String getDatabaseName() {
        return port;
    }

    public String getPort() {
        return port;
    }

    public String getHostname() {
        return hostname;
    }

    public String getUri(DBaasUser dBaasUser) {
        return new StringBuilder().append(protocol).append("://").append(dBaasUser.getName()).append(":").append(dBaasUser.getPassword()).append("@").append(hostname).append(":").append(port).append("/").append(databaseName).toString();
    }

    public String getJdbcUrl(DBaasUser user) {
        return new StringBuilder("jdbc").append(":").append(getUri(user)).toString();
    }
}
