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
import org.cloudfoundry.community.servicebroker.model.CreateServiceInstanceRequest;
import org.cloudfoundry.community.servicebroker.model.ServiceInstance;
import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LogServiceInstanceServiceTest {

    @Mock
    DashboardUrlFactory dashboardUrlFactory;

    @Test
    public void should_return_a_service_instance() throws Exception {
        final LogServiceInstanceService service = new LogServiceInstanceService(dashboardUrlFactory);
        final CreateServiceInstanceRequest request = new CreateServiceInstanceRequest().withServiceInstanceId("instance_id");

        final ServiceInstance serviceInstance = service.createServiceInstance(request);

        Assert.assertNotNull(serviceInstance);
    }

    @Test
    public void should_return_a_service_instance_containing_dashboard_url() throws Exception {
        final LogServiceInstanceService service = new LogServiceInstanceService(dashboardUrlFactory);
        final CreateServiceInstanceRequest request = new CreateServiceInstanceRequest().withServiceInstanceId("instance_id");

        Mockito.when(dashboardUrlFactory.getAllAppsDashboardUrl()).thenReturn("http://localhost/search");

        final ServiceInstance serviceInstance = service.createServiceInstance(request);

        Assertions.assertThat(serviceInstance.getDashboardUrl()).isEqualTo("http://localhost/search");
    }
}