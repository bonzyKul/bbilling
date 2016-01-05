package com.barclays.bbilling.config;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.spring.KModuleBeanFactoryPostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

/**
 * Created by alokkulkarni on 31/10/2015.
 */
@Configuration
public class KieAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(KieAutoConfiguration.class);


    @Bean
    public KieContainer kieContainer() {
        return KieServices.Factory.get().getKieClasspathContainer();
    }



//    private static final String RULES_PATH = "rules/";
//
//    @Bean
//    public KieFileSystem kieFileSystem() throws IOException {
//        KieFileSystem kieFileSystem = getKieServices().newKieFileSystem();
//        for (Resource file : getRuleFiles()) {
//            System.out.println(ResourceFactory.newClassPathResource(RULES_PATH + file.getFilename(), "UTF-8"));
//            kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + file.getFilename(), "UTF-8"));
//        }
//        return kieFileSystem;
//    }
//
//    private Resource[] getRuleFiles() throws IOException {
//        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
//        System.out.println(resourcePatternResolver.getResources("classpath*:" + RULES_PATH + "**/*.*"));
//        return resourcePatternResolver.getResources("classpath*:" + RULES_PATH + "**/*.*");
//    }
//
//    @Bean
//    public KieContainer kieContainer() throws IOException {
//        final KieRepository kieRepository = getKieServices().getRepository();
//
//        kieRepository.addKieModule(new KieModule() {
//            public ReleaseId getReleaseId() {
//                return kieRepository.getDefaultReleaseId();
//            }
//        });
//
//        KieBuilder kieBuilder = getKieServices().newKieBuilder(kieFileSystem());
//        kieBuilder.buildAll();
//
//        return getKieServices().newKieContainer(kieRepository.getDefaultReleaseId());
//    }
//
//    private KieServices getKieServices() {
//        return KieServices.Factory.get();
//    }
//
//    @Bean
//    public KieBase kieBase() throws IOException {
//        return kieContainer().getKieBase();
//    }
//
//    @Bean
//    public KieSession kieSession() throws IOException {
//        return kieBase().newKieSession();
//    }
//
//    /*
//     *  As http://docs.jboss.org/drools/release/6.2.0.CR1/drools-docs/html/ch.kie.spring.html
//     *  mentions: Without the org.kie.spring.KModuleBeanFactoryPostProcessor bean definition,
//     *  the kie-spring integration will not work
//     */
//    @Bean
//    public KModuleBeanFactoryPostProcessor kiePostProcessor() {
//        return new KModuleBeanFactoryPostProcessor();
//    }
}
