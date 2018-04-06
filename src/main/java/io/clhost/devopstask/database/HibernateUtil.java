package io.clhost.devopstask.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;


public class HibernateUtil {
    private static SessionFactory factory;

    public static void start() {
        try {
            Properties properties = new Properties();
            properties.load(new InputStreamReader(HibernateUtil.class.getResourceAsStream("/hibernate.properties")));

            factory = new Configuration()
                    .addProperties(properties)
                    .addAnnotatedClass(Report.class)
                    .buildSessionFactory();

        } catch (Exception e) {
            //logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public static Session getSession() {
        return factory.openSession();
    }

    public static void shutdown() {
        factory.close();
    }

    public static void setNamesUTF8() {
        Session session = factory.openSession();
        session.beginTransaction();

        session.createNativeQuery("SET NAMES UTF8").executeUpdate();

        session.getTransaction().commit();
        session.close();
    }
}
