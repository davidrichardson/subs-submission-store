package uk.ac.ebi.submission.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.hal.DefaultCurieProvider;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableHypermediaSupport(type= {EnableHypermediaSupport.HypermediaType.HAL})
public class CurieConfiguration {

    @Bean
    public CurieProvider curieProvider() {
        return new DefaultCurieProvider("subs", new UriTemplate("http://www.example.com{#rel}"));
    }
}
