package ilia.nemankov.conf;

import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.admin.RMQDestination;
import com.rabbitmq.jms.client.RMQMessageConsumer;
import ilia.nemankov.service.RecommendationsConsumer;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.*;

@Configuration
public class QueueConfig {

    @Value("${spring.rabbitmq.custom.username}")
    private String queueUsername;
    @Value("${spring.rabbitmq.custom.password}")
    private String queuePassword;
    @Value("${spring.rabbitmq.custom.host}")
    private String queueHost;
    @Value("${spring.rabbitmq.custom.port}")
    private Integer queuePort;
    @Value("${spring.rabbitmq.custom.virtualHost}")
    private String queueVirtualHost;
    @Value("${spring.rabbitmq.custom.timeout}")
    private Integer queueTimeout;

    @Bean
    public Queue recommendations() {
        return new Queue("recommendations");
    }

    @Bean
    public Destination jmsDestination() {
        RMQDestination jmsDestination = new RMQDestination();
        jmsDestination.setAmqp(true);
        jmsDestination.setAmqpQueueName("recommendations");
        return jmsDestination;
    }

    @Bean
    public ConnectionFactory jmsConnectionFactory(Destination jmsDestination, RecommendationsConsumer recommendationsConsumer) throws JMSException {
        RMQConnectionFactory connectionFactory = new RMQConnectionFactory();
        connectionFactory.setUsername(queueUsername);
        connectionFactory.setPassword(queuePassword);
        connectionFactory.setHost(queueHost);
        connectionFactory.setPort(queuePort);
        connectionFactory.setVirtualHost(queueVirtualHost);
        connectionFactory.setOnMessageTimeoutMs(queueTimeout);

        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        RMQMessageConsumer consumer = (RMQMessageConsumer) session.createConsumer(jmsDestination);
        consumer.setMessageListener(recommendationsConsumer);
        return connectionFactory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory jmsConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(jmsConnectionFactory);
        factory.setConcurrency("1-1");
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);

        return factory;
    }

}
