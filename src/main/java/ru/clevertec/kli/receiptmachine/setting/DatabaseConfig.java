package ru.clevertec.kli.receiptmachine.setting;

import java.util.Map;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.clevertec.kli.receiptmachine.util.database.datasource.DataSource;
import ru.clevertec.kli.receiptmachine.util.database.datasource.impl.DataSourceImpl;

@Configuration
@PropertySource("classpath:datasource.properties")
public class DatabaseConfig {

    @Value("${datasource.url}")
    private String url;
    @Value("${datasource.user}")
    private String user;
    @Value("${datasource.password}")
    private String password;
    @Value("${datasource.driver.class}")
    private String driverClassName;

    @Bean
    public DataSource dataSource() {
        findDriver(driverClassName);
        Properties connectionInfo = getPropertiesOf(
            Map.of("user", user, "password", password));
        return new DataSourceImpl(url, connectionInfo);
    }

    private void findDriver(String driverClassName) {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Can't find driver class " + driverClassName, e);
        }
    }

    private static Properties getPropertiesOf(Map<?, ?> props) {
        Properties properties = new Properties(props.size());
        properties.putAll(props);
        return properties;
    }
}
