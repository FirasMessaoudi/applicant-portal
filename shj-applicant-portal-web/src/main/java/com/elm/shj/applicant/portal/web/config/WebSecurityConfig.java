/*
 * Copyright (c) 2018 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.config;

import com.elm.dcc.foundation.commons.web.cors.CorsExceptionTranslationFilter;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtAuthenticationProvider;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenFilter;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import com.elm.shj.applicant.portal.web.security.otp.OtpAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.security.access.event.LoggerListener;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.writers.DelegatingRequestMatcherHeaderWriter;
import org.springframework.security.web.header.writers.HstsHeaderWriter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Main security configuration
 *
 * @author Aymen DHAOUI
 * @since 1.0.0
 */
@Configurable
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@ComponentScan(basePackages = {"com.elm.shj.applicant.portal.web", "com.elm.dcc.foundation.commons.web"})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String HEADER_WRITER_PATTERN = "/*";
    // any URL does not require authentication should be added to this array
    private static final String[] PUBLIC_URLS = {"/api/auth/login", "/api/auth/otp-for-login", "/api/users/reset-password", "/api/register","/api/register/validate-otp-for-registration","/api/register/generate-otp-for-registration","/api/register/verify", "/index.html", "/error", "/api-docs", "/swagger-ui.html", "/swagger-ui/**"};
    // URLs that will be ignored by spring security should be added to this array
    private static final String[] IGNORED_URLS = {"/assets/**", "/cpm-error/**", "/*.png", "/*.jpg", "/*.jpeg",
            "/*.ttf", "/*.svg", "/*.woff", "/*.woff2", "/*.eot", "/*.ico", "/*.js", "/*.css", "/*.json"};

    @Autowired
    private CorsExceptionTranslationFilter corsExceptionTranslationFilter;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private OtpAuthenticationProvider otpAuthenticationProvider;

    @Value("${content.security.policy.img-src}")
    private String contentSecurityPolicyImgSrc;

    @Value("${content.security.policy.default-src}")
    private String contentSecurityPolicyDefaultSrc;

    @Value("${content.security.policy.font-src}")
    private String contentSecurityPolicyFontSrc;

    @Value("${content.security.policy.style-src}")
    private String contentSecurityPolicyStyleSrc;

    @Value("${content.security.policy.script-src}")
    private String contentSecurityPolicyScriptSrc;

    @Bean
    WebClient authWebClient() {
        return WebClient.builder().build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(IGNORED_URLS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                // disabling the CSRF - Cross Site Request Forgery
                csrf().csrfTokenRepository(csrfTokenRepository())
                .requireCsrfProtectionMatcher((HttpServletRequest request) -> {
                    // request methods allowed to be without CSRF
                    Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE)$");
                    // check if caller type custom header
                    String callerType = request.getHeader(JwtTokenService.CALLER_TYPE_HEADER_NAME);
                    if (callerType != null && callerType.equals(JwtTokenService.WEB_SERVICE_CALLER_TYPE)) {
                        return false;
                    }
                    // CSRF for everything else that is not an a tablet or mobile call
                    Device device = deviceResolver().resolveDevice(request);
                    // ///////////////////////////////////////////// //
                    //   THIS IS FOR DEVELOPMENT ONLY TO SKIP CSRF   //
                    // ///////////////////////////////////////////// //
                    boolean devRequest = false;
                    try {
                        URL url = new URL(request.getRequestURL().toString());
                        devRequest = ("localhost".equalsIgnoreCase(url.getHost()) && (url.getPort() == 4200 || url.getPort() == 8080) &&
                                (request.getRequestURI().contains(Navigation.API_AUTH)
                                        || request.getRequestURI().contains(Navigation.API_USERS_RESET_PWRD)));
                    } catch (Exception e) {
                        // die silently
                    }
                    // ///////////////////////////////////////////// //
                    //                END DEV BLOCK                  //
                    // ///////////////////////////////////////////// //
                    return !devRequest && !(allowedMethods.matcher(request.getMethod()).matches()) && (device == null || device.isNormal());
                }).and()
                // disable cache
                .requestCache().disable()
                // configure headers
                .headers()// do not use any default headers unless explicitly listed
                .defaultsDisabled().contentTypeOptions().and().referrerPolicy(ReferrerPolicy.SAME_ORIGIN).and().frameOptions().sameOrigin().cacheControl().and().xssProtection().and()
                .contentSecurityPolicy(getContentSecurityPolicy())
                .and().addHeaderWriter(cacheStaticsHeaders()).addHeaderWriter(hstsHeader())
                .addHeaderWriter(serverHeader())
                .and()
                // starts authorizing configurations
                .authorizeRequests()
                // allow public urls
                .antMatchers(PUBLIC_URLS).permitAll()
                // request authentication for all remaining urls
                .anyRequest().fullyAuthenticated().and()
                // add the authentication provider
                .authenticationProvider(jwtAuthenticationProvider)
                .authenticationProvider(otpAuthenticationProvider)
                // exception handling
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)).and()
                // no session will be created or used by spring security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // adding custom filters
                .addFilter(jwtTokenFilter())
                // adding custom handler for cors exceptions
                .addFilterBefore(corsExceptionTranslationFilter, ChannelProcessingFilter.class)
                // adding CORS support
                // by default uses a Bean by the name of corsConfigurationSource
                .cors();
    }

    /**
     * Creates the content security policy header value
     *
     * @return content security policy
     */
    private String getContentSecurityPolicy() {
        String imgSrc = "img-src " + this.contentSecurityPolicyImgSrc;
        String defaultSrc = "default-src " + this.contentSecurityPolicyDefaultSrc;
        String fontSrc = "font-src " + this.contentSecurityPolicyFontSrc;
        String styleSrc = "style-src " + this.contentSecurityPolicyStyleSrc;
        String scriptSrc = "script-src " + this.contentSecurityPolicyScriptSrc;
        List<String> contentSecurityPolicyList = Arrays.asList(imgSrc, defaultSrc, fontSrc, styleSrc, scriptSrc);
        return String.join("; ", contentSecurityPolicyList);
    }

    /**
     * Creates the Csrf Token Repository bean
     *
     * @return a {@link CsrfTokenRepository} instance
     */
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        tokenRepository.setCookiePath("/");
        return tokenRepository;
    }

    /**
     * Creates the JWT Filter bean
     *
     * @return a {@link JwtTokenFilter} instance
     */
    @Bean
    public JwtTokenFilter jwtTokenFilter() throws Exception {
        return new JwtTokenFilter(authenticationManagerBean());
    }

    /**
     * Creates the Device Resolver bean
     *
     * @return a {@link LiteDeviceResolver} instance
     */
    @Bean
    public LiteDeviceResolver deviceResolver() {
        return new LiteDeviceResolver();
    }

    /**
     * Creates the password encoder bean
     *
     * @return a {@link BCryptPasswordEncoder} instance
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates a listener for access events
     *
     * @return a {@link LoggerListener} instance
     */
    @Bean
    public LoggerListener accessLoggerListener() {
        return new LoggerListener();
    }

    /**
     * Creates a listener for authentication events
     *
     * @return a {@link org.springframework.security.authentication.event.LoggerListener} instance
     */
    @Bean
    public org.springframework.security.authentication.event.LoggerListener authenticationLoggerListener() {
        return new org.springframework.security.authentication.event.LoggerListener();
    }

    /**
     * Adds a static header cache control
     *
     * @return a {@link DelegatingRequestMatcherHeaderWriter} instance
     */
    public DelegatingRequestMatcherHeaderWriter cacheStaticsHeaders() {
        List<Header> staticHeaders = new ArrayList<>();
        staticHeaders.add(new Header("cache-control", "max-age=31536000"));
        staticHeaders.add(new Header("Expires", "31536000"));
        return new DelegatingRequestMatcherHeaderWriter(new AntPathRequestMatcher("/resources/**"),
                new StaticHeadersWriter(staticHeaders));
    }

    /**
     * Provides support for <a href="http://tools.ietf.org/html/rfc6797">HTTP Strict Transport
     * Security (HSTS)</a>.
     *
     * @return a {@link DelegatingRequestMatcherHeaderWriter} instance
     */
    public DelegatingRequestMatcherHeaderWriter hstsHeader() {
        return new DelegatingRequestMatcherHeaderWriter(new AntPathRequestMatcher(HEADER_WRITER_PATTERN),
                new HstsHeaderWriter());
    }

    /**
     * Prevents server details to be showed
     *
     * @return a {@link HeaderWriter} instance
     */
    public StaticHeadersWriter serverHeader() {
        return new StaticHeadersWriter("Server", "CONFIDENTIAL");
    }
}
