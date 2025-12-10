package xin.ctkqiang.utilities;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate工具类，用于管理SessionFactory生命周期
 */
public class HibernateUtil {
    /** 会话工厂实例，线程安全 */
    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * 构建SessionFactory
     * @return SessionFactory实例
     * @throws ExceptionInInitializerError 当初始化失败时抛出
     */
    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("创建SessionFactory失败：" + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * 获取全局SessionFactory实例
     * @return SessionFactory单例
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * 关闭SessionFactory，释放资源
     */
    public static void shutdown() {
        getSessionFactory().close();
    }
}
