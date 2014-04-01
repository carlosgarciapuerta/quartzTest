package es.kgp.quartzTest;

import es.kgp.quartzTest.config.InDatabaseApplicationConfig;
import es.kgp.quartzTest.config.InMemoryApplicationConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: carlos.garcia
 * Date: 19/03/14
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) {
        //ApplicationContext ctx = new AnnotationConfigApplicationContext(InMemoryApplicationConfig.class);
        ApplicationContext ctx = new AnnotationConfigApplicationContext(InDatabaseApplicationConfig.class);
    }

}
