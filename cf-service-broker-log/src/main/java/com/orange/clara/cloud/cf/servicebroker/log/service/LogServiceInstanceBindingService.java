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
import com.orange.clara.cloud.cf.servicebroker.log.domain.SyslogDrainUrl;
import org.cloudfoundry.community.servicebroker.exception.ServiceBrokerException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceBindingExistsException;
import org.cloudfoundry.community.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.cloudfoundry.community.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.cloudfoundry.community.servicebroker.model.ServiceInstanceBinding;
import org.cloudfoundry.community.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LogServiceInstanceBindingService implements ServiceInstanceBindingService {

    private SyslogDrainUrl syslogDrainUrl;

    private DashboardUrlFactory dashboardUrlFactory;

    @Autowired
    public LogServiceInstanceBindingService(@Value("${log.syslogdrain.url}") String syslogDrainUrl, DashboardUrlFactory dashboardUrlFactory) {
        this.syslogDrainUrl = new SyslogDrainUrl(syslogDrainUrl);
        this.dashboardUrlFactory = dashboardUrlFactory;
    }

    @Override
    public ServiceInstanceBinding createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) throws ServiceInstanceBindingExistsException, ServiceBrokerException {
        return new ServiceInstanceBinding(request.getBindingId(), request.getServiceInstanceId(), getCredentials(request.getAppGuid()), getSyslogDrainUrl(), request.getAppGuid());
    }

    private Map<String, Object> getCredentials(String appGuid) {
        Map<String, Object> credentials = new HashMap<String, Object>();
        credentials.put("dashboard_url", dashboardUrlFactory.getAppDashboardUrl(appGuid));
        return credentials;
    }

    protected String getSyslogDrainUrl() {
        return syslogDrainUrl.getValue();
    }


    @Override
    public ServiceInstanceBinding deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) throws ServiceBrokerException {
        return null;
    }

}
