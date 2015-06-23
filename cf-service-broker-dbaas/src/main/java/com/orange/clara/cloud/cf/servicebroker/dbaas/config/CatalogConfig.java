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

import com.orange.clara.cloud.cf.servicebroker.dbaas.domain.DBaasPlan;
import org.cloudfoundry.community.servicebroker.model.Catalog;
import org.cloudfoundry.community.servicebroker.model.Plan;
import org.cloudfoundry.community.servicebroker.model.ServiceDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class CatalogConfig {
    @Bean
    public Catalog catalog() {
        return new Catalog(Arrays.asList(
                new ServiceDefinition(
                        "o-dbaas",
                        "o-dbaas",
                        "database on demand service using dbaas",
                        true,
                        false,
                        Arrays.asList(
                                new Plan(DBaasPlan.MYSQL_1G.toString(),
                                        DBaasPlan.MYSQL_1G.toString(),
                                        "1G mysql database instance",
                                        getPlanMetadata(), true),
                                new Plan(DBaasPlan.POSTGRESQL_1G.toString(),
                                        DBaasPlan.POSTGRESQL_1G.toString(),
                                        "1G postgres database instance",
                                        getPlanMetadata(), true)),
                        Arrays.asList("dbaas", "document"),
                        getServiceDefinitionMetadata(),
                        getRequires(),
                        null)));
    }

    private List<String> getRequires() {
        return Arrays.asList("syslog_drain");
    }

    /* Used by Pivotal CF console */
    private Map<String, Object> getServiceDefinitionMetadata() {
        Map<String, Object> sdMetadata = new HashMap<String, Object>();
        sdMetadata.put("displayName", "Orange Dbaas");
        sdMetadata.put("imageUrl", "");
        sdMetadata.put("longDescription", "DataBaseAsAService");
        sdMetadata.put("providerDisplayName", "DBAAS");
        sdMetadata.put("documentationUrl", "");
        sdMetadata.put("supportUrl", "");
        return sdMetadata;
    }

    private Map<String, Object> getPlanMetadata() {
        Map<String, Object> planMetadata = new HashMap<String, Object>();
        planMetadata.put("costs", getCosts());
        planMetadata.put("bullets", getBullets());
        return planMetadata;
    }

    private List<Map<String, Object>> getCosts() {
        Map<String, Object> costsMap = new HashMap<String, Object>();
        Map<String, Object> amount = new HashMap<String, Object>();
        amount.put("usd", new Double(0.0));
        costsMap.put("amount", amount);
        costsMap.put("unit", "MONTHLY");
        return Arrays.asList(costsMap);
    }

    private List<String> getBullets() {
        return Arrays.asList("Shared dbaas server",
                "blah bla",
                "blah bla");
    }
}
