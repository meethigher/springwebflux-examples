package top.meethigher.config;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 该内容主要用于粗略了解springboot自动装配时的内容。实际使用可直接通过springboot提供的配置文件来配置即可
 * 该内容的配置事项，请参考以下引用链接
 *
 * @author <a href="https://meethigher.top">chenchuancheng</a>
 * @see <a href="https://hantsy.github.io/spring-r2dbc-sample/database-client.html">使用 R2dbc DatabaseClient 处理关系数据库 | Spring R2dbc 示例</a>
 * @see <a href="https://github.com/hantsy/spring-r2dbc-sample">hantsy/spring-r2dbc-sample: Code samples for demonstrating R2dbc, Spring R2dbc, and Spring Data R2dbc.</a>
 * @since 2024/12/14 10:35
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host("10.0.0.9")
                        .database("shbh")
                        .username("postgres")
                        .password("postgres")
                        .build()
        );
    }

    @Bean
    public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.builder()
                .connectionFactory(connectionFactory)
                // true表示启用命名参数功能 类似于`where name=:name`。传false就表示只支持默认的占位符方式`where name=?`
                // org.springframework.r2dbc.core.DefaultDatabaseClientBuilder.build
                .namedParameters(true)
                .build();
    }

    /**
     * 数据库初始化
     */
    @Bean
    public ConnectionFactoryInitializer connectionFactoryInitializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        CompositeDatabasePopulator databasePopulator = new CompositeDatabasePopulator();
        databasePopulator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("sql/schema.sql")));
        initializer.setDatabasePopulator(databasePopulator);
        return initializer;
    }

    @Bean
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }
}
