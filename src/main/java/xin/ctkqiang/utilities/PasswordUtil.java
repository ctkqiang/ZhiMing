package xin.ctkqiang.utilities;

import org.hibernate.Session;
import org.hibernate.Transaction;

import xin.ctkqiang.model.Password;

public class PasswordUtil {
    public String databaseName;
    public String tableName;

    public PasswordUtil(String databaseName, String tableName) {
        this.databaseName = databaseName;
        this.tableName = tableName;
    }


    public void savePassword(String pass) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Password password = new Password();
           
            password.setPassword(pass);

            session.persist(password);

            transaction.commit();

             session.createQuery(
                "from Password", 
                Password.class
            ).list().forEach(p -> System.out.println("密码: " + p.getPassword()));

        } finally {
            HibernateUtil.shutdown();
        }
    }
}
