package jobs;

import javax.persistence.EntityManager;

import org.hibernate.Session;

import models.person.Admin;
import play.db.jpa.JPA;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class StartUp extends Job {
    
    @Override
    public void doJob() throws Exception {
        initData();
        updateColumn();
    }
    
    private static void initData() {
        final Session s = (Session) JPA.em().getDelegate();
        if (!s.getTransaction().isActive()) {
            s.getTransaction().begin();
        }
        Admin.init();
        s.getTransaction().commit();
    }
    
    private static void updateColumn() {
        EntityManager em = JPA.em();
        Session s = (Session) em.getDelegate();
        if (!s.getTransaction().isActive())
            s.getTransaction().begin();
        // em.createNativeQuery("").executeUpdate();
        s.getTransaction().commit();
    }
}
