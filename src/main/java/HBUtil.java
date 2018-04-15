import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HBUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        return buildSessionFactory();
    }

    public static SessionFactory buildSessionFactory() {
        if (sessionFactory == null) {
            try {
                registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
                MetadataSources sources = new MetadataSources(registry);
                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                e.printStackTrace();
                shutdown();
            }
        }
        return sessionFactory;
    }

    public static Session getSession() {
        Session hibernateSession = getSessionFactory().openSession();
        return hibernateSession;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
