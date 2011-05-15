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

package sk.openhouse.web;

import org.springframework.web.servlet.view.tiles2.TilesView;

import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.support.RequestContext;

/**
 * This is the same as TilesView but have an option (by default true) to expose
 * spring macro helpers
 *
 * @author pete <p.reisinger@gmail.com>
 */
public class VelocityTilesView extends TilesView {

    public static final String SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE =
            "springMacroRequestContext";

    private boolean exposeSpringMacroHelpers = true;

    /**
     * @param exposeSpringMacroHelpers  indicates if spring macro helpers
     *                                  should be in velocity template, default
     *                                  is true
     */
    public void setExposeSpringMacroHelpers(boolean exposeSpringMacroHelpers) {
        this.exposeSpringMacroHelpers = exposeSpringMacroHelpers;
    }

    /**
     * @return  indicates if spring macro helpers should be in velocity
     *          template, default is true
     */
    public boolean getExposeSpringMacroHelpers() {
        return exposeSpringMacroHelpers;
    }

    @Override
    protected void renderMergedOutputModel(
            Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        /* add spring macro helpers */
        if (this.exposeSpringMacroHelpers) {
            if (model.containsKey(SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE)) {
                throw new ServletException(
                        "Cannot expose bind macro helper '" + SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE +
                        "' because of an existing model object of the same name");
            }
            // Expose RequestContext instance for Spring macros.
            model.put(SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE,
                    new RequestContext(request, response, getServletContext(), model));
        }

        super.renderMergedOutputModel(model, request, response);
    }
}
