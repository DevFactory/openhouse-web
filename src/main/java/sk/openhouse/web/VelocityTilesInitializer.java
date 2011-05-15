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

import java.util.ArrayList;
import java.util.List;
import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.startup.AbstractTilesInitializer;

/**
 *
 * @author pete <p.reisinger@gmail.com>
 */
public class VelocityTilesInitializer extends AbstractTilesInitializer {

    /* definitions */
    private List<String> definitions = new ArrayList<String>();

    /* paths to properties and toolbox */
    private String velocityProperties;
    private String velocityToolbox;

    /** @param definitions - tiles.xml, if not set default is in WEB-INF */
    public void setDefinitions(List<String> definitions) {
        this.definitions = definitions;
    }

    /** @param velocityToolbox path to the velocity properties file */
    public void setVeolocityProperties(String velocityProperties) {
        this.velocityProperties = velocityProperties;
    }

    /** @param velocityToolbox path to the velocity toolbox xml file */
    public void setVeolocityToolbox(String velocityToolbox) {
        this.velocityToolbox = velocityToolbox;
    }

    @Override
    protected AbstractTilesContainerFactory createContainerFactory(
            TilesApplicationContext context) {

        VelocityTilesContainerFactory containerFactory = 
                new VelocityTilesContainerFactory();
        containerFactory.setDefinitions(definitions);
        containerFactory.setVeolocityProperties(velocityProperties);
        containerFactory.setVeolocityToolbox(velocityToolbox);

        return containerFactory;
    }

}
