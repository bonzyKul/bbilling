package com.barclays.bbilling.domain;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by alokkulkarni on 31/10/2015.
 */
@Component
public class FileRouter extends RouteBuilder {

    private static final Logger log = LoggerFactory.getLogger(FileRouter.class);


    @Override
    public void configure() throws Exception {
        from("file:/Users/alokkulkarni/workspace/camelWeb/target/messages/india?noop=true").to("file:/Users/alokkulkarni/Documents/files/transferedFiles");
        from("file:/Users/alokkulkarni/workspace/camelWeb/target/messages/uk?noop=true").to("file:/Users/alokkulkarni/Documents/files/transferedFiles");
    }
}
