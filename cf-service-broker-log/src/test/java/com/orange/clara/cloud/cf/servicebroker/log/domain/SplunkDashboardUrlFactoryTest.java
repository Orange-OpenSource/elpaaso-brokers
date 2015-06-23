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
package com.orange.clara.cloud.cf.servicebroker.log.domain;

import com.orange.clara.cloud.cf.servicebroker.log.infrastructure.SplunkDashboardUrlFactory;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Created by sbortolussi on 21/04/2015.
 */
public class SplunkDashboardUrlFactoryTest {

    public static final String LOG_SERVER_URL = "http://localhost";

    @Test(expected = IllegalArgumentException.class)
    public void base_splunk_dashboard_url_should_not_be_empty() throws Exception {
        new SplunkDashboardUrlFactory("");
    }

    @Test
    public void splunk_app_dashboard_url_should_start_with_log_server_url() throws Exception {
        final SplunkDashboardUrlFactory urlFactory = new SplunkDashboardUrlFactory(LOG_SERVER_URL);

        final String appDashboardUri = urlFactory.getAppDashboardUrl("app_uid");

        Assertions.assertThat(appDashboardUri).startsWith(LOG_SERVER_URL);
    }

    @Test
    public void splunk_all_app_dashboard_url_should_start_with_log_server_url() throws Exception {
        final SplunkDashboardUrlFactory urlFactory = new SplunkDashboardUrlFactory(LOG_SERVER_URL);

        final String appDashboardUri = urlFactory.getAllAppsDashboardUrl();

        Assertions.assertThat(appDashboardUri).startsWith(LOG_SERVER_URL);
    }

    @Test
    public void splunk_app_dashboard_url_path_should_be_flashtimeline() throws Exception {
        final SplunkDashboardUrlFactory urlFactory = new SplunkDashboardUrlFactory(LOG_SERVER_URL);

        final String appDashboardUri = urlFactory.getAppDashboardUrl("app_uid");

        Assertions.assertThat(UriComponentsBuilder.fromHttpUrl(appDashboardUri).build().toUri().getPath()).isEqualTo("/en-US/app/search/flashtimeline");
    }

    @Test
    public void splunk_all_app_dashboard_url_path_should_be_flashtimeline() throws Exception {
        final SplunkDashboardUrlFactory urlFactory = new SplunkDashboardUrlFactory(LOG_SERVER_URL);

        final String appDashboardUri = urlFactory.getAllAppsDashboardUrl();

        Assertions.assertThat(UriComponentsBuilder.fromHttpUrl(appDashboardUri).build().toUri().getPath()).isEqualTo("/en-US/app/search/flashtimeline");
    }

    @Test
    public void splunk_app_dashboard_url_should_contain_app_search_query_parameters() throws Exception {
        final SplunkDashboardUrlFactory urlFactory = new SplunkDashboardUrlFactory(LOG_SERVER_URL);

        final String appDashboardUri = urlFactory.getAppDashboardUrl("app_uid");

        Assertions.assertThat(appDashboardUri).endsWith("q=search%20index%3D%22*%22%20source%3D%22tcp:12345%22%20appname%3D%22app_uid%22");
    }

    @Test
    public void splunk_all_app_dashboard_url_should_contain_base_search_query_parameters() throws Exception {
        final SplunkDashboardUrlFactory urlFactory = new SplunkDashboardUrlFactory(LOG_SERVER_URL);

        final String appDashboardUri = urlFactory.getAllAppsDashboardUrl();

        Assertions.assertThat(appDashboardUri).endsWith("q=search%20index%3D%22*%22%20source%3D%22tcp:12345%22");
    }

}