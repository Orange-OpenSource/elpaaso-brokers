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

import javax.persistence.Embeddable;

/**
 * Created by sbortolussi on 22/04/2015.
 */
@Embeddable
public class DBaasUser {

    private String name;

    private String password;

    protected DBaasUser() {
    }

    public DBaasUser(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public static DBaasUser newInstance(CredentialsGeneratorService service) {
        return new DBaasUser(service.randomUsername(), service.randomPassword());
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
