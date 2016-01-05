package com.barclays.bbilling.config;

import org.apache.camel.*;
import org.apache.camel.spring.SpringCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by alokkulkarni on 31/10/2015.
 */
@Configuration
public class CamelAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(CamelAutoConfiguration.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private RoutesBuilder[] routesBuilders;

    @Bean
    CamelContext camelContext() throws Exception {
        CamelContext camelContext = new SpringCamelContext(applicationContext);

        System.out.println("Configured Camel Configuration");

        if (routesBuilders != null) {
            for (RoutesBuilder routesBuilder : routesBuilders) {
                camelContext.addRoutes(routesBuilder);
            }
        }
        return camelContext;
    }


    @Bean
    ProducerTemplate producerTemplate() throws Exception {
        return camelContext().createProducerTemplate();
    }

    @Bean
    ConsumerTemplate consumerTemplate() throws Exception {
        return camelContext().createConsumerTemplate();
    }

    @Bean
    TypeConverter typeConverter() throws Exception {
        return camelContext().getTypeConverter();
    }

}
