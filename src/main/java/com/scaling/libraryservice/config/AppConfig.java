package com.scaling.libraryservice.config;

import com.scaling.libraryservice.recommend.util.RecommendRanRule;
import com.scaling.libraryservice.search.util.relate.RelationTokenRule;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan("com.scaling.libraryservice.mapBook.util")
@EnableAspectJAutoProxy
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        factory.setConnectTimeout(3000);
        factory.setReadTimeout(4000);

        return new RestTemplate(factory);
    }

    @Bean
    public Komoran komoran() {

        return new Komoran(DEFAULT_MODEL.FULL);
    }

    @Bean
    public RelationTokenRule relationTokenRule() {

        return new RelationTokenRule();
    }

    @Bean
    public RecommendRanRule recommendRanRule() {

        return new RecommendRanRule();
    }

}
