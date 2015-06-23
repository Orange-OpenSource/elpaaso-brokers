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
package com.orange.clara.cloud.cf.servicebroker.dbaas.config;

import com.orange.clara.cloud.dbaas.ws.api.service.DbaasApiRemote;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by sbortolussi on 09/04/2015.
 */
@Configuration
public class DBaasEndpointConfiguration {

    @Value("${dbaas.api.username}")
    String dBaasUser;

    @Value("${dbaas.api.password}")
    String dBaasPwd;

    @Value("${dbaas.api.url}")
    String dBaasUrl;

    @Bean
    public DbaasApiRemote dBaasProxy() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(DbaasApiRemote.class);
        factory.setAddress(dBaasUrl);
        factory.setUsername(dBaasUser);
        factory.setPassword(dBaasPwd);
        return factory.create(DbaasApiRemote.class);
    }

}
