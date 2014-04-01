package es.kgp.quartzTest.service;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: carlos.garcia
 * Date: 19/03/14
 * Time: 16:47
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DefaultDatabaseCleanService implements DatabaseCleanService {

    @Override
    public void cleanExpiredSessions(){
        System.out.println("do something");
    }

}
