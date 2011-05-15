Helper classes to be used with spring framework and spring security.
Includes Localised ReCaptcha, velocity together with tiles, BCrypt etc.

To use apache tiles together with apache velocity in spring project, add this to your ...-servlet.xml

<pre>
<!-- velocity + tiles initializer -->
<bean id="velocityTilesInitializer" class="sk.openhouse.web.VelocityTilesInitializer">
    <property name="veolocityToolbox" value="/WEB-INF/classes/velocity/tools.xml" />
    <property name="veolocityProperties" value="/WEB-INF/classes/velocity/velocity.properties" />
    <property name="definitions">
        <list>
            <value>/WEB-INF/views/main-tiles.xml</value>
        </list>
    </property>
</bean>

<!-- tiles config with velocityTiles initializer -->
<bean id="tilesConfigurer" class="org.springframework.web.servlet.view.tiles2.TilesConfigurer">
    <property name="tilesInitializer" ref="velocityTilesInitializer" />
</bean>

<!-- tiles view -->
<bean id="viewResolver" class="org.springframework.web.servlet.view.UrlBasedViewResolver">
    <property name="viewClass" value="sk.openhouse.web.VelocityTilesView" />
    <property name="contentType" value="text/html;charset=UTF-8" />
    <!-- default attributes for every view -->
    <property name="attributesMap">
        <map>
            <entry key="authentication" value-ref="authenticationService" />
        </map>
    </property>
</bean>
</pre>

To use localised recaptcha, register ReCaptchaService and call its methods.

<pre>
// recaptcha html for the view
reCaptchaService.getCaptchaHtml()

// HttpServletRequest in the post controller
reCaptchaService.isValid(request)
</pre>

There is BCrypt encoder and salt service (from Damien Miller) to be used with sprint security.