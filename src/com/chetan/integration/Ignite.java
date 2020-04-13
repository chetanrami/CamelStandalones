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

public class Ignite {
    private static final String LOG = "com.chetan.integration.App";
    private static final Logger LOGGER = Logger.getLogger(Ignite.class.getName());

    private static Main main;

    public static void main(String args[]) throws Exception {

        runRoutes();

    }

    private static void runRoutes() throws Exception {

        DataSource dataSource = setupDataSource();
        SimpleRegistry registry = new SimpleRegistry();
        registry.put("mysqlDS", dataSource);

        long beforeMillis = System.currentTimeMillis();
        CamelContext context = new DefaultCamelContext(registry);
        context.addRoutes(new RouteBuilder() {
            public void configure() {
                from("timer:foo?period=7s")
                  .setBody(constant("every seven seconds"))
                  .log(LoggingLevel.INFO, LOG, "Response of rest call : ${body.class}\n ---> ${body}\n ---> ${headers}")
                  .setBody(constant("select * from license"))
                  .to("jdbc:mysqlDS")
                  .log(LoggingLevel.INFO, LOG, "Response of rest call : ${body.class}\n ---> ${body}\n ---> ${headers}");
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

    private static DataSource setupDataSource() {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setUser("root");
        ds.setPassword("password");
        ds.setUrl("jdbc:mysql://127.0.0.1/ignite");
        return ds;
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
//camel-jdbc-2.20.0.jar
//mysql-connector-java-5.1.44.jar
