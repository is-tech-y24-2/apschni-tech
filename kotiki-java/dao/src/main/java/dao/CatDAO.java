package dao;

import model.Cat;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.UUID;

public class CatDAO {
    private final Session session;

    public CatDAO() {
        session = HibernateUtil.getSession();
    }

    public void save(Cat cat){
        session.saveOrUpdate(cat);
    }

    public Cat findById(UUID id){
        return session.get(Cat.class, id);
    }

    public void delete(Cat cat){
        session.delete(cat);
    }
}
