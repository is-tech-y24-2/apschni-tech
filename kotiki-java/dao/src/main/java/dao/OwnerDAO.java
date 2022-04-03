package dao;

import model.Owner;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.UUID;

public class OwnerDAO {
    private final Session session;

    public OwnerDAO() {
        session = HibernateUtil.getSession();
    }

    public void save(Owner owner){
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(owner);
        transaction.commit();
    }

    public Owner findById(UUID id){
        return session.get(Owner.class, id);
    }

    public void delete(Owner owner){
        session.delete(owner);
    }
}
