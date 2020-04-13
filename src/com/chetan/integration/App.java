package com.chetan.integration;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
//import org.apache.camel.component.cache.CacheConstants;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private static final String LOG = "com.chetan.integration.App";
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    private static final String cache = "cache://testAppCacheStandalone";

    public static void main(String args[]) throws Exception {
        long beforeMillis = System.currentTimeMillis();
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            public void configure() {
//            from("timer:foo?period=7s")
//                    .setHeader(CacheConstants.CACHE_OPERATION, constant(CacheConstants.CACHE_OPERATION_GET))
//                    .setHeader(CacheConstants.CACHE_KEY, constant(body().convertToString()))
//                    .setBody(constant("1")).to(cache)
//                    .choice()
//                        .when(header(CacheConstants.CACHE_ELEMENT_WAS_FOUND).isNull())
//                            .setBody(constant("PayIQAuthPostRestResponse"))
//                            .log(LoggingLevel.INFO, LOG, "Response of rest call : ${body.class}\n ---> ${body}\n ---> ${headers}")
//                            .setHeader(CacheConstants.CACHE_OPERATION, constant(CacheConstants.CACHE_OPERATION_ADD))
//                            .setHeader(CacheConstants.CACHE_KEY, constant(body().convertToString()))
//                            .log(LoggingLevel.INFO, LOG, "logInfo('Adding element not found in cache : ${body.class}\n ---> ${body}\n ---> ${headers}")
//                            .to(cache)
//                        .endChoice()
//                        .otherwise()
//                            .process(testProcessor1)
//                            .convertBodyTo(String.class)
//                            .process(testProcessor2)
//                            .log(LoggingLevel.INFO, LOG, "After checking in Cache found: ${body.class}\n ---> ${body}\n ---> ${headers}")
//                        .endChoice()
//                    .end()
//                    .log(LoggingLevel.INFO, LOG, "Running camel route every 7 seconds");
            }
        });

        context.start();

        LOGGER.log(Level.INFO, "Press enter to continue and stop the camel context...");
        Scanner keyboard = new Scanner(System.in);
        keyboard.nextLine();
        keyboard.close();

        context.stop();

        long afterMillis = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "Total time ran: {0} milliseconds", (afterMillis-beforeMillis));
    }

    static Processor testProcessor1 = exchange -> {
        Message msg = exchange.getIn();
    };

    static Processor testProcessor2 = exchange -> {
        Message msg = exchange.getIn();
    };
}

//--> Jars and versions to use:
//camel-core-2.19.1.jar
//slf4j-api-1.8.0-alpha2.jar
//slf4j-simple-1.8.0-alpha2.jar
