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
package com.orange.clara.cloud.cf.servicebroker.dbaas.service;

import com.orange.clara.cloud.cf.servicebroker.dbaas.domain.*;
import com.orange.clara.cloud.cf.servicebroker.dbaas.repository.DBaasServiceInstanceRepository;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceExistsException;
import org.cloudfoundry.community.servicebroker.model.CreateServiceInstanceRequest;
import org.cloudfoundry.community.servicebroker.model.DeleteServiceInstanceRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DbaasServiceInstanceServiceTest {

    @Mock
    DBaasService dBaasService;

    @Mock
    DBaasServiceInstanceRepository repository;

    @Mock
    CredentialsGeneratorService credentialsGeneratorService;

    @Test
    public void should_create_service_instance_if_service_instance_does_not_exist() throws Exception {
        final DbaasServiceInstanceService service = new DbaasServiceInstanceService(repository, dBaasService, credentialsGeneratorService);
        final CreateServiceInstanceRequest request = new CreateServiceInstanceRequest().withServiceInstanceId("instance_id");
        request.setPlanId("MYSQL_1G");
        when(repository.findOne("instance_id")).thenReturn(null);

        service.createServiceInstance(request);

        verify(repository).save(any(DbaasServiceInstance.class));
        verify(dBaasService).createDatabase(eq(DBaasPlan.MYSQL_1G), isA(DBaasUser.class));
    }

    @Test(expected = ServiceInstanceExistsException.class)
    public void fail_to_create_service_instance_if_service_instance_exists() throws Exception {
        final DbaasServiceInstanceService service = new DbaasServiceInstanceService(repository, dBaasService, credentialsGeneratorService);
        final CreateServiceInstanceRequest request = new CreateServiceInstanceRequest().withServiceInstanceId("instance_id");
        request.setPlanId("a_plan");
        when(repository.findOne("instance_id")).thenReturn(new DbaasServiceInstance("instance_id", "database_id"));

        service.createServiceInstance(request);
    }

    @Test
    public void should_delete_service_instance_if_service_instance_exists() throws Exception {
        final DbaasServiceInstanceService service = new DbaasServiceInstanceService(repository, dBaasService, credentialsGeneratorService);
        final DeleteServiceInstanceRequest request = new DeleteServiceInstanceRequest("instance_id", "dbaas_service", "a_plan", true);
        final DbaasServiceInstance dbaasServiceInstance = new DbaasServiceInstance("instance_id", "database_id");
        when(repository.findOne("instance_id")).thenReturn(dbaasServiceInstance);

        service.deleteServiceInstance(request);

        verify(repository).delete(dbaasServiceInstance);
        verify(dBaasService).deleteDatabase("database_id");
    }

    @Test
    public void should_not_delete_service_instance_if_service_instance_does_not_exist() throws Exception {
        final DbaasServiceInstanceService service = new DbaasServiceInstanceService(repository, dBaasService, credentialsGeneratorService);
        final DeleteServiceInstanceRequest request = new DeleteServiceInstanceRequest("instance_id", "dbaas_service", "a_plan", true);
        when(repository.findOne("instance_id")).thenReturn(null);

        service.deleteServiceInstance(request);

        verify(repository, never()).delete(any(DbaasServiceInstance.class));
        verify(dBaasService, never()).deleteDatabase(anyString());
    }

}