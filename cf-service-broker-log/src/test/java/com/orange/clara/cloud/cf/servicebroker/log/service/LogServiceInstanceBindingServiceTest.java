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
package com.orange.clara.cloud.cf.servicebroker.log.service;

import com.orange.clara.cloud.cf.servicebroker.log.domain.DashboardUrlFactory;
import org.cloudfoundry.community.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.cloudfoundry.community.servicebroker.model.ServiceInstanceBinding;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LogServiceInstanceBindingServiceTest {

    @Mock
    DashboardUrlFactory dashboardUrlFactory;

    @Test
    public void syslog_drain_url_should_be_syslog_colon_slash_slash_serverhost_colon_server_port() {
        LogServiceInstanceBindingService service = new LogServiceInstanceBindingService("syslog://localhost:1234", dashboardUrlFactory);
        Assertions.assertThat(service.getSyslogDrainUrl()).isEqualTo("syslog://localhost:1234");
    }

    @Test
    public void create_service_binding_should_return_syslog_drain_url() throws Exception {
        LogServiceInstanceBindingService service = new LogServiceInstanceBindingService("syslog://localhost:1234", dashboardUrlFactory);
        final CreateServiceInstanceBindingRequest request = new CreateServiceInstanceBindingRequest();

        final ServiceInstanceBinding serviceInstanceBinding = service.createServiceInstanceBinding(request);
        Assertions.assertThat(serviceInstanceBinding.getSyslogDrainUrl()).isEqualTo("syslog://localhost:1234");
    }
}