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
package com.orange.clara.cloud.cf.servicebroker.log.infrastructure;

import com.orange.clara.cloud.cf.servicebroker.log.domain.DashboardUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SplunkDashboardUrlFactory implements DashboardUrlFactory {

    public static final String SEARCH_PATH = "/en-US/app/search/flashtimeline";

    private String logServerUrl;

    @Autowired
    public SplunkDashboardUrlFactory(@Value("${log.server.url}") String logServerUrl) {
        setLogServerUrl(logServerUrl);
    }

    @Override
    public String getAppDashboardUrl(String appGuid) {
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(logServerUrl);
        urlBuilder.path(SEARCH_PATH);
        //uncomment to enable auto pause mode
        //urlBuilder.queryParam("auto_pause", "true");
        urlBuilder.queryParam("q", "search index=\"*\" source=\"tcp:12345\" appname=\"" + appGuid + "\"");
        return urlBuilder.build().encode().toUriString();
    }

    @Override
    public String getAllAppsDashboardUrl() {
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(logServerUrl);
        urlBuilder.path(SEARCH_PATH);
        urlBuilder.queryParam("q", "search index=\"*\" source=\"tcp:12345\"");
        return urlBuilder.build().encode().toUriString();
    }


    private void setLogServerUrl(String logServerUrl) {
        Assert.hasText(logServerUrl, "Invalid log server url <" + logServerUrl + ">. Log server url should not be empty.");
        this.logServerUrl = logServerUrl;
    }

}