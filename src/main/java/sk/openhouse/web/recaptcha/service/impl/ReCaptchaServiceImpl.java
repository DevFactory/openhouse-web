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

package sk.openhouse.web.recaptcha.service.impl;

import java.util.Enumeration;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.apache.log4j.Logger;
import sk.openhouse.web.recaptcha.LocaleReCaptchaImpl;
import sk.openhouse.web.recaptcha.service.ReCaptchaService;

/**
 *
 * @author pete <p.reisinger@gmail.com>
 */
public class ReCaptchaServiceImpl implements ReCaptchaService {

    private static final Logger logger = Logger.getLogger(ReCaptchaServiceImpl.class);
    private LocaleReCaptchaImpl reCaptchaImpl;
    private Properties options;

    public ReCaptchaServiceImpl(String publicKey, String privateKey) {

        reCaptchaImpl = new LocaleReCaptchaImpl();
        reCaptchaImpl.setPublicKey(publicKey);
        reCaptchaImpl.setPrivateKey(privateKey);
    }

    public void setOptions(Properties options) {
        this.options = options;
    }

    @Override
    public String getCaptchaHtml() {

        /* do not pass options (properties are mutable */
        Properties props = new Properties();
        Enumeration e = options.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = options.getProperty(key);
            props.put(key, value);
        }

        return reCaptchaImpl.createRecaptchaHtml(null, props);
    }

    @Override
    public boolean isValid(HttpServletRequest request) {

        String remoteAddr = request.getRemoteAddr();
        String challenge = request.getParameter("recaptcha_challenge_field");
        String uresponse = request.getParameter("recaptcha_response_field");

        ReCaptchaResponse reCaptchaResponse =
                reCaptchaImpl.checkAnswer(remoteAddr, challenge, uresponse);

        return reCaptchaResponse.isValid();
    }
}
