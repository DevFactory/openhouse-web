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

package sk.openhouse.web.recaptcha;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import net.tanesha.recaptcha.ReCaptchaImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 *
 * @author pete <p.reisinger@gmail.com>
 */
public class LocaleReCaptchaImpl extends ReCaptchaImpl implements MessageSourceAware {

    private static final Logger logger = Logger.getLogger(LocaleReCaptchaImpl.class);

    private MessageSourceAccessor messages = ReCaptchaMessageSource.getAccessor();
    private String publicKey;
    private String recaptchaServer = HTTP_SERVER;
    private boolean includeNoscript = false;

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    @Override
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public void setRecaptchaServer(String recaptchaServer) {
        this.recaptchaServer = recaptchaServer;
    }

    @Override
    public void setIncludeNoscript(boolean includeNoscript) {
        this.includeNoscript = includeNoscript;
    }

    public String createRecaptchaHtml(String errorMessage) {

        return createRecaptchaHtml(errorMessage, null);
    }

    @Override
    public String createRecaptchaHtml(String errorMessage, Properties options) {

        String errorPart = "";
        if (errorMessage != null) {
            String encoding = "UTF-8";
            try {
                errorPart = "&amp;error=" + URLEncoder.encode(errorMessage, encoding);
            } catch (UnsupportedEncodingException ex) {
                logger.fatal(encoding + " encoding is not supported", ex);
            }
        }

        if (options == null) {
            options = new Properties();
        }

        String prefix = "reCaptcha.";
        /* message key with default value */
        Map<String, String> keys = new HashMap<String, String>();
        keys.put("visual_challenge", "Get a visual challenge");
        keys.put("audio_challenge", "Get an audio challenge");
        keys.put("refresh_btn", "Get a new challenge");
        keys.put("instructions_visual", "Type the two words:");
        keys.put("instructions_context", "Type the words in the boxes:");
        keys.put("instructions_audio", "Type what you hear:");
        keys.put("help_btn", "Help");
        keys.put("play_again", "Play sound again");
        keys.put("cant_hear_this", "Download sound as MP3");
        keys.put("incorrect_try_again", "Incorrect. Try again.");

        /* get locale from properties if set */
        Locale locale = null;
        String langKey = "lang";
        if (options.containsKey(langKey)) {
            List<String> langs = Arrays.asList(Locale.getISOLanguages());
            String lang = options.getProperty(langKey);
            if (langs.contains(lang)) {
                locale = new Locale(lang);
            }
        }

        /* if locale is not set, get it from context */
        if (locale == null) {
            locale = LocaleContextHolder.getLocale();
        }

        /* load messages for a given locale, if it fails, default to EN */
        StringBuilder sb = new StringBuilder("{");
        try {
            for (Map.Entry<String, String> entry : keys.entrySet()) {
                String key = entry.getKey();
                sb.append(key);
                sb.append(":\"");
                sb.append(messages.getMessage(prefix + key, locale));
                sb.append("\",");
            }
        } catch (NoSuchMessageException ex) {
            locale = Locale.ENGLISH;
            for (Map.Entry<String, String> entry : keys.entrySet()) {
                String key = entry.getKey();
                sb.append(key);
                sb.append(":\"");
                sb.append(messages.getMessage(prefix + key, entry.getValue(), locale));
                sb.append("\",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");

        options.put("custom_translations", sb.toString());
        options.put("lang", locale.getLanguage());

        String message = fetchJSOptions(options);

        message += "<script type=\"text/javascript\" src=\"" + recaptchaServer
                + "/challenge?k=" + publicKey + errorPart + "\"></script>\r\n";

        if (includeNoscript) {
            String noscript = "<noscript>\r\n"
                    + "	<iframe src=\"" + recaptchaServer + "/noscript?k=" 
                    + publicKey + errorPart
                    + "\" height=\"300\" width=\"500\" frameborder=\"0\"></iframe><br>\r\n"
                    + "	<textarea name=\"recaptcha_challenge_field\" rows=\"3\" cols=\"40\"></textarea>\r\n"
                    + "	<input type=\"hidden\" name=\"recaptcha_response_field\" value=\"manual_challenge\">\r\n"
                    + "</noscript>";
            message += noscript;
        }

        return message;
    }

    /**
     * Produces javascript array with the RecaptchaOptions encoded.
     * If the value starts with "{" and ends with "}", single quotes are not
     * prepended and appended. This way javascript objects can be passed.
     *
     * @param properties
     * @return
     */
    private String fetchJSOptions(Properties properties) {

        if (properties == null || properties.size() == 0) {
            return "";
        }

        String jsOptions =
                "<script type=\"text/javascript\">\r\n"
                + "var RecaptchaOptions = {";

        for (Enumeration e = properties.keys(); e.hasMoreElements();) {
            String property = (String) e.nextElement();

            String value = properties.getProperty(property).trim();
            if (value.startsWith("{") && value.endsWith("}")) {
                jsOptions += property + ":" + value;
            } else {
                jsOptions += property + ":'" + value + "'";
            }

            if (e.hasMoreElements()) {
                jsOptions += ",";
            }

        }

        jsOptions += "};\r\n</script>\r\n";

        return jsOptions;
    }
}
