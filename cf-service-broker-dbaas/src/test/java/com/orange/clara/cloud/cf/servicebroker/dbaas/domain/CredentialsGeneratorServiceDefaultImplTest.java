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
package com.orange.clara.cloud.cf.servicebroker.dbaas.domain;

import com.orange.clara.cloud.cf.servicebroker.dbaas.infrastructure.CredentialsGeneratorServiceDefaultImpl;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.regex.Pattern;

/**
 * Created by sbortolussi on 23/04/2015.
 */
public class CredentialsGeneratorServiceDefaultImplTest {

    public static final String USERNAME_REGEX = "[a-z]{1}[a-zA-Z0-9]{7}";

    //8 characters with at least one digit, one lower case character, one upper case character
    public static final String PASSWORD_REGEX = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8})";

    @Test
    public void random_username_should_start_with_1_lower_case_letter_then_7_lower_case_letters_or_digits_without_special_characters() throws Exception {
        for (int i = 0; i < 100; i++) {
            final String username = new CredentialsGeneratorServiceDefaultImpl().randomUsername();
            Assertions.assertThat(Pattern.matches(USERNAME_REGEX, username)).overridingErrorMessage("random username <" + username + "> should match regex " + USERNAME_REGEX).isTrue();
        }
    }

    @Test
    public void random_password_should_contain_at_least_one_digit_one_lower_case_character_one_upper_case_character_without_special_characters() throws Exception {
        for (int i = 0; i < 100; i++) {
            final String password = new CredentialsGeneratorServiceDefaultImpl().randomPassword();
            Assertions.assertThat(Pattern.matches(PASSWORD_REGEX, password)).overridingErrorMessage("random password <" + password + "> should match regex " + PASSWORD_REGEX).isTrue();
        }
    }

}