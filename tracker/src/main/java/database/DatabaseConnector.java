package database;

import exceptions.ConfigFileNotFoundException;
import database.model.Node;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

public class DatabaseConnector {
    private static volatile DatabaseConnector instance;
    private Properties properties;
    private Connection connection;
    private  Configuration configuration;
    private StandardServiceRegistry serviceRegistry;

    private DatabaseConnector() {
        try {
            this.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized public static DatabaseConnector getInstance() {
        if (instance == null)
            instance = new DatabaseConnector();

        return instance;
    }


    private void connect() throws Exception {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            this.properties = new Properties();
            this.properties.load(input);
        } catch (Exception ex) {
            throw new ConfigFileNotFoundException();
        }

        this.configuration = new Configuration().addPackage("database.model").addAnnotatedClass(Node.class);
        this.serviceRegistry = new StandardServiceRegistryBuilder().applySettings(this.properties).build();
        this.connection = serviceRegistry.getService(ConnectionProvider.class).getConnection();



    }

    public <T> String save(T object){
        Session session = configuration.buildSessionFactory(serviceRegistry).openSession();
        Transaction transaction = session.beginTransaction();
        String id = (String) session.save(object);
        transaction.commit();
        return id;
    }
    public <T> List<T> loadAll(Class<T> type){
        Session session = configuration.buildSessionFactory(serviceRegistry).openSession();
        Transaction transaction = session.beginTransaction();
        String query = "FROM " + type.getName(); //+ " t where t.address='TestAddress1'";
        List list = session.createQuery(query).list();
        transaction.commit();
        return list;
    }

    public <T> void addFile(Node object, String name){
        Session session = configuration.buildSessionFactory(serviceRegistry).openSession();
        Transaction transaction = session.beginTransaction();
        object = session.get(Node.class, object.getAddress());
        object.addServedFile(name);
        transaction.commit();
    }
    public <T> void update(T object, String id){
        Session session = configuration.buildSessionFactory(serviceRegistry).openSession();
        Transaction transaction = session.beginTransaction();

        session.merge(id, object);
        transaction.commit();
    }
    public <T> T loadOneById(Class<T> type, String id){
        Session session = configuration.buildSessionFactory(serviceRegistry).openSession();
        Transaction transaction = session.beginTransaction();
        T result = session.get(type, id);
        transaction.commit();
        return result;
    }
    public <T> boolean exists(Class<T> type, String id){
        Session session = configuration.buildSessionFactory(serviceRegistry).openSession();
        Transaction transaction = session.beginTransaction();
        T result = session.get(type, id);
        transaction.commit();
        return result!=null;
    }
    public <T> T loadOneWhere(Class<T> type, String condition){
        Session session = configuration.buildSessionFactory(serviceRegistry).openSession();
        Transaction transaction = session.beginTransaction();
        char c = type.getSimpleName().charAt(0);
        String query = "FROM " + type.getName() + " " + c+ "  where c."+condition;
        T result = (T) session.createQuery(query).list().get(0);
        transaction.commit();
        return result;
    }

    public <T>void clear(Class<T> type){
        Session session = configuration.buildSessionFactory(serviceRegistry).openSession();
        Transaction transaction = session.beginTransaction();

        String query = "DELETE FROM " + type.getName(); //+ " t where t.address='TestAddress1'";
        session.createQuery(query).executeUpdate();
        transaction.commit();
    }
}
