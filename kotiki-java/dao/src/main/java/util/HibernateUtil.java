package util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .configure()
                    .buildSessionFactory();
        } catch (HibernateException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}