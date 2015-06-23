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
import org.cloudfoundry.community.servicebroker.exception.ServiceBrokerException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceExistsException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceUpdateNotSupportedException;
import org.cloudfoundry.community.servicebroker.model.*;
import org.cloudfoundry.community.servicebroker.service.ServiceInstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sbortolussi on 09/04/2015.
 */
@Service
public class DbaasServiceInstanceService implements ServiceInstanceService {

    private DBaasServiceInstanceRepository repository;
    private DBaasService dBaasService;
    private CredentialsGeneratorService credentialsGeneratorService;


    private static final Logger LOGGER = LoggerFactory.getLogger(DbaasServiceInstanceService.class);

    @Autowired
    public DbaasServiceInstanceService(DBaasServiceInstanceRepository repository, DBaasService dBaasService, CredentialsGeneratorService credentialsGeneratorService) {
        this.repository = repository;
        this.dBaasService = dBaasService;
        this.credentialsGeneratorService = credentialsGeneratorService;
    }

    public ServiceInstance createServiceInstance(CreateServiceInstanceRequest request) throws ServiceInstanceExistsException, ServiceBrokerException {
        LOGGER.debug("Creating dbaas broker service instance <{}>.", request.getServiceInstanceId());

        if (repository.findOne(request.getServiceInstanceId()) != null) {
            throw new ServiceInstanceExistsException(new ServiceInstance(request));
        }

        final DBaasUser defaultOwner = DBaasUser.newInstance(credentialsGeneratorService);
        final String databaseUid = dBaasService.createDatabase(getPlan(request.getPlanId()), defaultOwner);

        repository.save(new DbaasServiceInstance(request.getServiceInstanceId(), databaseUid).withOwner(defaultOwner));

        return new ServiceInstance(request).isAsync(true);
    }

    protected DBaasPlan getPlan(String planId) {
        return DBaasPlan.valueOf(planId);
    }

    public ServiceInstance getServiceInstance(String serviceInstanceId) {
        DbaasServiceInstance instance = repository.findOne(serviceInstanceId);
        if (instance != null) {
            ServiceInstanceLastOperation lastOperation = dBaasService.getDatabaseState(instance.getDatabaseUid());
            return new ServiceInstance(new CreateServiceInstanceRequest().withServiceInstanceId(serviceInstanceId)).isAsync(true).withLastOperation(lastOperation);
        }
        return null;
    }

    public ServiceInstance deleteServiceInstance(DeleteServiceInstanceRequest request) throws ServiceBrokerException {
        LOGGER.debug("Deleting dbaas broker service instance <{}>.", request.getServiceInstanceId());
        DbaasServiceInstance instance = repository.findOne(request.getServiceInstanceId());
        if (instance == null) {
            LOGGER.warn("Will not delete dbaas broker service instance <{}>. Dbaas broker service instance is not registered.", request.getServiceInstanceId());
            return null;
        }
        dBaasService.deleteDatabase(instance.getDatabaseUid());

        repository.delete(instance);

        return new ServiceInstance(request).isAsync(true);
    }

    public ServiceInstance updateServiceInstance(UpdateServiceInstanceRequest request) throws ServiceInstanceUpdateNotSupportedException, ServiceBrokerException, ServiceInstanceDoesNotExistException {
        throw new ServiceInstanceUpdateNotSupportedException(request.getServiceInstanceId());
    }
}
