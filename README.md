Helper classes to be used with spring framework and spring security.
Includes Localised ReCaptcha, velocity together with tiles, BCrypt etc.

To use apache tiles together with apache velocity in spring project, use archetype simple-web:
http://github.com/pete911/spring-simple-web

or add this to your ...-servlet.xml

<pre>
&lt;!-- velocity + tiles initializer --&gt;
&lt;bean id="velocityTilesInitializer" class="sk.openhouse.web.VelocityTilesInitializer"&gt;
    &lt;property name="veolocityToolbox" value="/WEB-INF/classes/velocity/tools.xml" /&gt;
    &lt;property name="veolocityProperties" value="/WEB-INF/classes/velocity/velocity.properties" /&gt;
    &lt;property name="definitions"&gt;
        &lt;list&gt;
            &lt;value&gt;/WEB-INF/views/main-tiles.xml&lt;/value&gt;
        &lt;/list&gt;
    &lt;/property&gt;
&lt;/bean&gt;

&lt;!-- tiles config with velocityTiles initializer --&gt;
&lt;bean id="tilesConfigurer" class="org.springframework.web.servlet.view.tiles2.TilesConfigurer"&gt;
    &lt;property name="tilesInitializer" ref="velocityTilesInitializer" /&gt;
&lt;/bean&gt;

&lt;!-- tiles view --&gt;
&lt;bean id="viewResolver" class="org.springframework.web.servlet.view.UrlBasedViewResolver"&gt;
    &lt;property name="viewClass" value="sk.openhouse.web.VelocityTilesView" /&gt;
    &lt;property name="contentType" value="text/html;charset=UTF-8" /&gt;
    &lt;!-- default attributes for every view --&gt;
    &lt;property name="attributesMap"&gt;
        &lt;map&gt;
            &lt;entry key="authentication" value-ref="authenticationService" /&gt;
        &lt;/map&gt;
    &lt;/property&gt;
&lt;/bean&gt;
</pre>

To use localised recaptcha, register ReCaptchaService and call its methods.

<pre>
// recaptcha html for the view
reCaptchaService.getCaptchaHtml()

// HttpServletRequest in the post controller
reCaptchaService.isValid(request)
</pre>

There is BCrypt encoder and salt service (from Damien Miller) to be used with sprint security.