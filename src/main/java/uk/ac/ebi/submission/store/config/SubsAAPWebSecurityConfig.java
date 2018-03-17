package uk.ac.ebi.submission.store.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uk.ac.ebi.tsc.aap.client.security.StatelessAuthenticationEntryPoint;
import uk.ac.ebi.tsc.aap.client.security.StatelessAuthenticationFilter;
import uk.ac.ebi.tsc.aap.client.security.TokenAuthenticationService;

/**
 * Created by neilg on 24/05/2017.
 */
@Configuration
@EnableConfigurationProperties
@ConditionalOnWebApplication
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan("uk.ac.ebi.tsc.aap.client.security")
@ConditionalOnProperty(prefix = "aap", name = "enabled", matchIfMissing = true)
@Order(SecurityProperties.BASIC_AUTH_ORDER - 15)
@Slf4j
public class SubsAAPWebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private StatelessAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    private StatelessAuthenticationFilter statelessAuthenticationFilterBean() throws Exception {

        return new StatelessAuthenticationFilter(this.tokenAuthenticationService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {


        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                //general server use + health
                .antMatchers("/").permitAll()
                .antMatchers("/browser/**").permitAll()
                .antMatchers("/docs/**").permitAll()
                .antMatchers("/health").permitAll()
                .antMatchers("/health/summary").permitAll()
                .antMatchers(HttpMethod.GET,"/profile").permitAll()
                .antMatchers(HttpMethod.GET,"/profile/**").permitAll()
                .antMatchers(HttpMethod.GET,"/*/search").permitAll()
                .mvcMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                .mvcMatchers(HttpMethod.HEAD,"/**").permitAll() //
                // parts of the app visible to anyone, even without login
                .antMatchers(HttpMethod.GET,"/submissionPlanWizards").permitAll()
                .antMatchers(HttpMethod.GET,"/submissionPlanWizards/**").permitAll()
                .antMatchers(HttpMethod.GET,"/checklists").permitAll()
                .antMatchers(HttpMethod.GET,"/checklists/**").permitAll()
                .antMatchers(HttpMethod.GET,"/documentTypes").permitAll()
                .antMatchers(HttpMethod.GET,"/documentTypes/**").permitAll()
                // user data, documents must be secure but collection roots are ok
                .antMatchers(HttpMethod.GET,"/submissions").permitAll()
                .antMatchers(HttpMethod.GET,"/submissionDocuments").permitAll()
                .antMatchers(HttpMethod.GET,"/validationResults").permitAll()
                .anyRequest().authenticated();

        httpSecurity.addFilterBefore(statelessAuthenticationFilterBean(),
                UsernamePasswordAuthenticationFilter.class);
        // disable page caching
        httpSecurity.headers().cacheControl();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

}
