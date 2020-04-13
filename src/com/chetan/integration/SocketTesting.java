package com.chetan.integration;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.main.Main;

import javax.sql.DataSource;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketTesting {
    private static final String LOG = "com.chetan.integration.App";
    private static final Logger LOGGER = Logger.getLogger(SocketTesting.class.getName());

    private static Main main;

    public static void main(String args[]) throws Exception {

        runRoutes();
    }

    private static void runRoutes() throws Exception {

        SimpleRegistry registry = new SimpleRegistry();

        long beforeMillis = System.currentTimeMillis();
        CamelContext context = new DefaultCamelContext(registry);

        context.addRoutes(new RouteBuilder() {
            public void configure() {

                from("timer:foo?period=7s")
                  .setBody(constant("Hello Netty")).convertBodyTo(String.class)
                  .to("netty4:tcp://localhost:77?textline=true&sync=false&clientMode=true")
                  .convertBodyTo(String.class)
                  .log(LoggingLevel.INFO, LOG, "Response of socket tcpIP call : \n ---> ${body.class}\n ---> ${body}\n ---> ${headers}");
            }
        });

        context.start();

        LOGGER.log(Level.INFO, "Press enter to continue and stop the camel context...");
        Scanner keyboard = new Scanner(System.in);
        keyboard.nextLine();
        keyboard.close();

        context.stop();

        long afterMillis = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "Total time ran: {0} milliseconds", (afterMillis - beforeMillis));
    }

    static Processor testProcessor1 = exchange -> {
        Message msg = exchange.getIn();
    };
}

//--> Jars and versions to use:
//camel-core-2.17.0.redhat-630224.jar
//camel-mina2-2.17.0.redhat-630224.jar
//camel-netty4-2.17.0.redhat-630224.jar
//camel-spring-2.17.0.redhat-630224.jar
//commons-lang3-3.7.jar
//commons-pool-1.6.jar
//ehcache-2.10.4.jar
//mina-core-3.0.0-M2.jar
//netty-all-4.1.19.Final.jar
//spring-core-5.0.0.RELEASE.jar
//slf4j-api-1.8.0-alpha2.jar
//slf4j-simple-1.8.0-alpha2.jar
