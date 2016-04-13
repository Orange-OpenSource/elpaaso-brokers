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

import com.orange.clara.cloud.cf.servicebroker.dbaas.domain.DBaasService;
import com.orange.clara.cloud.cf.servicebroker.dbaas.domain.DatabaseCredentials;
import com.orange.clara.cloud.cf.servicebroker.dbaas.domain.DbaasServiceInstance;
import com.orange.clara.cloud.cf.servicebroker.dbaas.domain.DbaasServiceInstanceBinding;
import com.orange.clara.cloud.cf.servicebroker.dbaas.repository.DBaasServiceInstanceBindingRepository;
import com.orange.clara.cloud.cf.servicebroker.dbaas.repository.DBaasServiceInstanceRepository;
import org.cloudfoundry.community.servicebroker.exception.ServiceBrokerException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceBindingExistsException;
import org.cloudfoundry.community.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.cloudfoundry.community.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.cloudfoundry.community.servicebroker.model.ServiceInstanceBinding;
import org.cloudfoundry.community.servicebroker.service.ServiceInstanceBindingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sbortolussi on 20/04/2015.
 */
@Service
public class DbaasServiceInstanceBindingService implements ServiceInstanceBindingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbaasServiceInstanceBindingService.class);
    private DBaasServiceInstanceBindingRepository dBaasServiceInstanceBindingRepository;
    private DBaasServiceInstanceRepository dBaasServiceInstanceRepository;
    private DBaasService dBaasService;


    @Autowired
    public DbaasServiceInstanceBindingService(DBaasServiceInstanceBindingRepository dBaasServiceInstanceBindingRepository, DBaasServiceInstanceRepository dBaasServiceInstanceRepository, DBaasService dBaasService) {
        this.dBaasServiceInstanceBindingRepository = dBaasServiceInstanceBindingRepository;
        this.dBaasServiceInstanceRepository = dBaasServiceInstanceRepository;
        this.dBaasService = dBaasService;
    }


    public ServiceInstanceBinding createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) throws ServiceInstanceBindingExistsException, ServiceBrokerException {
        if (dBaasServiceInstanceBindingRepository.findOne(request.getBindingId()) != null) {
            //TODO generate ServiceInstanceBinding from DbaasServiceInstanceBinding
            throw new ServiceInstanceBindingExistsException(new ServiceInstanceBinding(request.getBindingId(), request.getServiceInstanceId(), null, null, request.getPlanId()));
        }

        final DbaasServiceInstance database = dBaasServiceInstanceRepository.findOne(request.getServiceInstanceId());
        final DatabaseCredentials databaseCredentials = dBaasService.getDatabaseCredentials(database.getDatabaseUid());
        //dbaas service instance binding user will be dbaas instance service default owner
        final DbaasServiceInstanceBinding binding = new DbaasServiceInstanceBinding(request.getBindingId(), database.getDatabaseUid(), database.getOwner());
        dBaasServiceInstanceBindingRepository.save(binding);

        Map<String, Object> credentials = new HashMap<String, Object>();
        credentials.put("hostname", databaseCredentials.getHostname());
        credentials.put("name", databaseCredentials.getDatabaseName());
        credentials.put("port", databaseCredentials.getPort());
        credentials.put("password", database.getOwner().getPassword());
        credentials.put("username", database.getOwner().getName());
        credentials.put("uri", databaseCredentials.getUri(database.getOwner()));
        return new ServiceInstanceBinding(request.getBindingId(), request.getServiceInstanceId(), credentials, null, request.getAppGuid());
    }

    public ServiceInstanceBinding deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) throws ServiceBrokerException {
        DbaasServiceInstanceBinding binding = dBaasServiceInstanceBindingRepository.findOne(request.getBindingId());
        if (binding == null) {
            LOGGER.warn("Will not delete dbaas broker service instance binding<{}>. Dbaas broker service instance binding is not registered.", request.getBindingId());
            return null;
        }
        //dBaasService.deleteUserFromDatabase(binding.getDatabaseId(), binding.getUser()); removed cause it delete the only user in database (create service instance binding never create a user)
        dBaasServiceInstanceBindingRepository.delete(request.getBindingId());
        return new ServiceInstanceBinding(request.getBindingId(), request.getInstance().getServiceInstanceId(), null, null, null);
    }

}
