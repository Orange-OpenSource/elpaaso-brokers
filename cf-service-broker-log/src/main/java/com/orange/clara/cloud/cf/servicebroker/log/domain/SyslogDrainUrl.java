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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

/**
 * Created by sbortolussi on 20/04/2015.
 */
public class SyslogDrainUrl {

    public static final String SYSLOG_SCHEME = "syslog://";

    private String value;

    public SyslogDrainUrl(@Value("${log.syslogdraindurl}") String value) {
        setValue(value);
    }

    private void setValue(String value) {
        Assert.hasText(value, "Invalid syslog drain url <" + value + ">. Syslog drain url should not be empty.");
        Assert.isTrue(value.startsWith(SYSLOG_SCHEME), "Invalid syslog drain url <" + value + ">. Syslog drain url should start with " + SYSLOG_SCHEME + ".");
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SyslogDrainUrl url = (SyslogDrainUrl) o;

        return !(value != null ? !value.equals(url.value) : url.value != null);

    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
