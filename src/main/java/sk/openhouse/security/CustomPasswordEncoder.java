/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sk.openhouse.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;

/**
 * Uses jBCrypt for encoding, validation and for salt as well. Salt is stored
 * as part of the password, so it does not have separate column in the user
 * table.
 *
 * @author pete <p.reisinger@gmail.com>
 */
public class CustomPasswordEncoder implements PasswordEncoder {

    @Override
    public String encodePassword(String rawPass, Object salt) throws DataAccessException {
        return BCrypt.hashpw(rawPass, salt.toString());
    }

    @Override
    public boolean isPasswordValid(String encPass, String rawPass, Object salt)
            throws DataAccessException {

        return BCrypt.checkpw(rawPass, encPass);
    }
}
