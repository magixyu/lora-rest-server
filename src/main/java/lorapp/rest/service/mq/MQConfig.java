package lorapp.rest.service.mq;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author MoseC
 * @Desc
 * @Date 2016/12/16
 */
@Configuration
@Component
public class MQConfig{
    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.username}")
    private String userName;

    @Value("${spring.rabbitmq.password}")
    private String passwd;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    private ConnectionFactory connectionFactory;
    private RabbitAdmin rabbitAdmin;

    private Map<String, TopicExchange> topicExchangeMap = new ConcurrentHashMap<>();
    private Map<String, Queue> queueMap = new ConcurrentHashMap<>();
    private Map<String, AbstractMessageListenerContainer> queueListenerMap = new HashMap<>();

    public MQConfig(){
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setAddresses(host);
        cachingConnectionFactory.setUsername(userName);
        cachingConnectionFactory.setPassword(passwd);
        cachingConnectionFactory.setVirtualHost(virtualHost);
        cachingConnectionFactory.setPublisherConfirms(true); //必须要设置
        connectionFactory = cachingConnectionFactory;

        rabbitAdmin = new RabbitAdmin(connectionFactory);
    }


    public void addNewQueueAndListener(String queueName, String exchangeName,String bindKey, MessageListener msgListener){
        Queue queue = null;
        if(queueMap.get(queueName) == null){
            queue = new Queue(queueName, false);
            rabbitAdmin.declareQueue(queue);
            queueMap.put(queueName, queue);
        }else{
            queue = queueMap.get(queueName);
        }

        TopicExchange topicExchange = null;
        if(topicExchangeMap.get(exchangeName) == null){
            topicExchange = new TopicExchange(exchangeName, false, false);
            rabbitAdmin.declareExchange(topicExchange);
            topicExchangeMap.put(exchangeName, topicExchange);
        }else{
            topicExchange = topicExchangeMap.get(exchangeName);
        }
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(topicExchange).with(bindKey));

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(queue);
        container.setMessageListener(msgListener);
        container.start();

        queueListenerMap.put(queueName, container);
    }


    public void delQueueAndListener(String queueName){
        AbstractMessageListenerContainer listenerContainer = queueListenerMap.get(queueName);
        queueListenerMap.remove(queueName);
        listenerContainer.stop();
        listenerContainer.destroy();

        Queue queue = queueMap.get(queueName);
        queueMap.remove(queueName);
        rabbitAdmin.deleteQueue(queueName);
    }


    public Map<String, TopicExchange> getTopicExchangeMap() {
        return topicExchangeMap;
    }
    public Map<String, Queue> getQueueMap() {
        return queueMap;
    }
    public Map<String, AbstractMessageListenerContainer> getQueueListenerMap() {
        return queueListenerMap;
    }
}

