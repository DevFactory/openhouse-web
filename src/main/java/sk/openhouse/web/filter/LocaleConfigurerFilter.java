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

package sk.openhouse.web.filter;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.LocaleResolver;

/**
 * Sets LocaleContextHolder's locale to locale from locale resolver set in
 * application context (if there's any)
 *
 * @author pete <p.reisinger@gmail.com>
 */
public class LocaleConfigurerFilter extends OncePerRequestFilter {

    /* locale resolver set in app context */
    private LocaleResolver localeResolver;

    @Override
    protected void initFilterBean() throws ServletException {

        WebApplicationContext context = WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext());

        Map resolvers = context.getBeansOfType(LocaleResolver.class);
        if (resolvers.size() == 1) {
            localeResolver = (LocaleResolver) resolvers.values().iterator().next();
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (localeResolver != null) {
            Locale locale = localeResolver.resolveLocale(request);
            LocaleContextHolder.setLocale(locale);
        }

        filterChain.doFilter(request, response);

        if (localeResolver != null) {
            LocaleContextHolder.resetLocaleContext();
        }
    }
}
