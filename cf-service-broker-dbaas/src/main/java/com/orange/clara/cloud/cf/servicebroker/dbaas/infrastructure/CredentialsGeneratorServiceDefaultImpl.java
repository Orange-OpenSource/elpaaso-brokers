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
package com.orange.clara.cloud.cf.servicebroker.dbaas.infrastructure;

import com.orange.clara.cloud.cf.servicebroker.dbaas.domain.CredentialsGeneratorService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Created by sbortolussi on 23/04/2015.
 */
@Service
public class CredentialsGeneratorServiceDefaultImpl implements CredentialsGeneratorService {

    //8 characters with at least one digit, one lower case character, one upper case character
    public static final String PASSWORD_REGEX = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8})";

    /**
     * @return a 8 character password starting with 1 lower case letter then many lower case letters or digits, without specials characters
     */
    public String randomUsername() {
        return new StringBuilder().append(RandomStringUtils.randomAlphabetic(1)).append(RandomStringUtils.randomAlphanumeric(7)).toString().toLowerCase();
    }

    /**
     * @return - a 8 characters password with at least one digit, one lower case character, one upper case character, without specials characters
     */
    public String randomPassword() {
        while (true) {
            //because RandomStringUtils.randomAlphanumeric(8) may produce String with no digit or no upper case letter, we should iterate
            final String s = RandomStringUtils.randomAlphanumeric(8);
            if (Pattern.matches(PASSWORD_REGEX, s)) return s;
        }
    }
}
