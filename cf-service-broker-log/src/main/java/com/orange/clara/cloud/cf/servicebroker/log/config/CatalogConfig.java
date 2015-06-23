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
package com.orange.clara.cloud.cf.servicebroker.log.config;

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
        return new Catalog( Arrays.asList(
                new ServiceDefinition(
                        "o-logs",
                        "o-logs",
                        "simple splunk implementation to get a syslog drain url",
                        true,
                        false,
                        Arrays.asList(
                                new Plan("splunk-plan",
                                        "splunk",
                                        "This is a default splunk plan. All services are created equally.",
                                        getPlanMetadata(),true)),
                        Arrays.asList("splunk", "document"),
                        getServiceDefinitionMetadata(),
                        getRequires(),
                        null)));
    }

    private List<String> getRequires() {
        return Arrays.asList("syslog_drain");
    }

    /* Used by Pivotal CF console */
    private Map<String,Object> getServiceDefinitionMetadata() {
        Map<String,Object> sdMetadata = new HashMap<>();
        sdMetadata.put("displayName", "Orange logs");
        sdMetadata.put("imageUrl","http://www.splunk.com/content/dam/splunk/img/logo.png");
        sdMetadata.put("longDescription","Splunk Service");
        sdMetadata.put("providerDisplayName","Pivotal");
        sdMetadata.put("documentationUrl","http://www.splunk.com");
        sdMetadata.put("supportUrl","http://www.splunk.com");
        return sdMetadata;
    }
    private Map<String,Object> getPlanMetadata() {
        Map<String,Object> planMetadata = new HashMap<String,Object>();
        planMetadata.put("costs", getCosts());
        planMetadata.put("bullets", getBullets());
        return planMetadata;
    }
    private List<Map<String,Object>> getCosts() {
        Map<String,Object> costsMap = new HashMap<String,Object>();
        Map<String,Object> amount = new HashMap<String,Object>();
        amount.put("usd", new Double(0.0));
        costsMap.put("amount", amount);
        costsMap.put("unit", "MONTHLY");
        return Arrays.asList(costsMap);
    }
    private List<String> getBullets() {
        return Arrays.asList("Shared Splunk server",
                "blah bla",
                "blah bla");
    }
}
