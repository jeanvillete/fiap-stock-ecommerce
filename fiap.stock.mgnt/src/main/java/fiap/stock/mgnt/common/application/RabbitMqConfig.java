package fiap.stock.mgnt.common.application;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Channel rabbitMqChannel(
            @Value("${rabbitmq.host}") String host,
            @Value("${rabbitmq.port}") Integer port,
            @Value("${rabbitmq.username}") String username,
            @Value("${rabbitmq.password}") String password,
            @Value("${rabbitmq.virtual-host}") String virtualHost) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);

        Connection connection = factory.newConnection();
        return connection.createChannel();
    }

}
