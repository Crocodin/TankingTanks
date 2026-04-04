package ubb.dbsm.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class JPAUtils {
    private static final Logger logger = LogManager.getLogger(JPAUtils.class);
    private static EntityManagerFactory emf;
    private static HikariDataSource dataSource;

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            logger.info("Initializing EntityManagerFactory for persistence unit 'TankingTanksPU'");
            try {
                HikariConfig hikariConfig = getHikariConfig();

                dataSource = new HikariDataSource(hikariConfig);
                logger.info("HikariCP pool initialized successfully");

                // Hand the datasource to Hibernate
                Map<String, Object> overrides = new HashMap<>();
                overrides.put("jakarta.persistence.nonJtaDataSource", dataSource);

                emf = Persistence.createEntityManagerFactory("TankingTanksPU", overrides);
                logger.info("EntityManagerFactory initialized successfully");
            } catch (Exception e) {
                logger.error("Failed to initialize EntityManagerFactory", e);
                throw new RuntimeException(e);
            }
        } else logger.debug("EntityManagerFactory already initialized, reusing existing instance");

        return emf;
    }

    private static HikariConfig getHikariConfig() {
        var props = Config.getProperties();

        // Configure HikariCP
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(props.getProperty("db.url"));
        hikariConfig.setUsername(props.getProperty("db.user"));
        hikariConfig.setPassword(props.getProperty("db.password"));
        hikariConfig.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        hikariConfig.setMinimumIdle(Integer.parseInt(props.getProperty("db.pool.minimumIdle", "2")));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.pool.maximumPoolSize", "10")));
        hikariConfig.setConnectionTimeout(Long.parseLong(props.getProperty("db.pool.connectionTimeout", "30000")));
        hikariConfig.setIdleTimeout(Long.parseLong(props.getProperty("db.pool.idleTimeout", "600000")));
        hikariConfig.setMaxLifetime(Long.parseLong(props.getProperty("db.pool.maxLifetime", "1800000")));
        return hikariConfig;
    }

    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            logger.info("Closing EntityManagerFactory");
            emf.close();
            logger.info("EntityManagerFactory closed successfully");
        } else {
            logger.warn("closeEntityManagerFactory called but EMF is already closed or null");
        }

        if (dataSource != null && !dataSource.isClosed()) {
            logger.info("Closing HikariCP connection pool");
            dataSource.close();
            logger.info("HikariCP pool closed successfully");
        }
    }
}
