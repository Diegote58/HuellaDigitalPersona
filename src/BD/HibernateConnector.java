/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BD;

import java.sql.Connection;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author chandu
 */
public class HibernateConnector {

    //Variables de Conexión
    public String puerto ="3306";
    public String servidor ="localhost";
    public String db ="huelladigital";
    public String user = "root";
    public String pass = "yz450f";
    
    
    //Configutación Hibernate
    private static HibernateConnector conn;
    private Configuration cfg;
    private SessionFactory sessionFactory;
    
    
    

    public HibernateConnector() throws HibernateException {

        // build the config
        cfg = new Configuration();

        /**
         * Connection Information..
         */
        cfg.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        cfg.setProperty("hibernate.connection.url", "jdbc:mysql://"+servidor+":"+puerto+"/"+db);
        cfg.setProperty("hibernate.connection.username", user);
        cfg.setProperty("hibernate.connection.password", pass);
        cfg.setProperty("hibernate.show_sql", "true");

        /**
         * Mapping Resources..
         */
        cfg.addResource("Pojo/Usuario.hbm.xml");
        cfg.addResource("Pojo/Huelladigital.hbm.xml");

        sessionFactory = cfg.buildSessionFactory();
    }

    public static synchronized HibernateConnector getInstance() throws HibernateException {
        if (conn == null) {
            conn = new HibernateConnector();
        }

        return conn;
    }

    public Session getSession() throws HibernateException {
        Session session = sessionFactory.openSession();
        if (!session.isConnected()) {
            this.reconnect();
        }
        return session;
    }

    private void reconnect() throws HibernateException {
        this.sessionFactory = cfg.buildSessionFactory();
    }
}
