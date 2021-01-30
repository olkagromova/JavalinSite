package org.example;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateSessionFactory {
    private static SessionFactory sessionFactory =
            build();
    protected static SessionFactory build(){
        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata().buildSessionFactory();
        }catch (Exception e){
            System.out.println(e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
        return sessionFactory;
    }

    public static SessionFactory getSession() {
        return sessionFactory;
    }
    public void shutdown(){
        getSession().close();
    }
}
