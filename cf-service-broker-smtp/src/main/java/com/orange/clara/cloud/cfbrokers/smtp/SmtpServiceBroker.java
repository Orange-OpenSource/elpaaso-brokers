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
package com.orange.clara.cloud.cfbrokers.smtp;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import cf.spring.servicebroker.Bind;
import cf.spring.servicebroker.BindRequest;
import cf.spring.servicebroker.BindResponse;
import cf.spring.servicebroker.Deprovision;
import cf.spring.servicebroker.DeprovisionRequest;
import cf.spring.servicebroker.Provision;
import cf.spring.servicebroker.ProvisionRequest;
import cf.spring.servicebroker.ProvisionResponse;
import cf.spring.servicebroker.Service;
import cf.spring.servicebroker.ServiceBroker;
import cf.spring.servicebroker.ServicePlan;
import cf.spring.servicebroker.Unbind;
import cf.spring.servicebroker.UnbindRequest;

@ServiceBroker(
	@Service(id = "service", name = "o-smtp", description = "Orange base service smtp service.", tags={"smtp"},
			plans = {
					@ServicePlan(id = "plan1", name = "orange-mail-fed-plan", description = "default shared FED smtp plan"),
		}
	)
)
public class SmtpServiceBroker {
	
	private static final Logger logger = LoggerFactory.getLogger(SmtpServiceBroker.class);
	
	
	@Value("${targetHost}")
	String host;
	
	@Value("${targetPort}")
	int port;

	@Value("${dashboardUrl:http://www.dummy.com}")
	String dashboarUrl;
	
	@Value("${syslogDrainUrl:syslog://www.dummy.com:12345}")
	String syslogDrainUrl;
	
	@Provision
	public ProvisionResponse provision(ProvisionRequest request) {
		logger.info("Provisioning service with id {}", request.getInstanceGuid());
		
		return new ProvisionResponse(this.dashboarUrl);
	}

	@Deprovision
	public void deprovision(DeprovisionRequest request) {
		logger.info("Deprovisioning service with id {}", request.getInstanceGuid());
	}

	
	
	
	@Bind
	public BindResponse bind(BindRequest request) {
		logger.info(
				"Binding service instance {} to application {}",
				request.getServiceInstanceGuid(),
				request.getApplicationGuid());
		
		Map<String, String> credentials = new HashMap<>();
		credentials.put("host", this.host);
		credentials.put("port", new Integer(this.port).toString());
		credentials.put("username", "undefined");
		credentials.put("password", "undefined");
		credentials.put("tags","[smtp]");
		
		return new BindResponse(credentials);

	}

	@Unbind
	public void unbind(UnbindRequest request) {
		logger.info("Unbind service instance {}",
				request.getServiceInstanceGuid());
	}
	
}	


