
# Deploying on CF

The following list of environment variables need to be set:
* broker_log_user : the username specified in the [broker registration](http://docs.cloudfoundry.org/services/managing-service-brokers.html#register-broker)
* broker_log_password : the username specified in the [broker registration](http://docs.cloudfoundry.org/services/managing-service-brokers.html#register-broker)
* log_syslogdraindurl : the splunk syslog url that will receive app logs 
* log_server_url : the base splunk UI url exposed as the service dashboard 