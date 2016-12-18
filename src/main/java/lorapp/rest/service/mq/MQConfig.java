package lorapp.rest.service.mq;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
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
@Component(value = "mqConfig")
public class MQConfig {
	@Value("${spring.rabbitmq.host}")
	private String host;

	@Value("${spring.rabbitmq.username}")
	private String userName;

	@Value("${spring.rabbitmq.password}")
	private String passwd;

	@Value("${spring.rabbitmq.virtual-host}")
	private String virtualHost;

	@Value("${lora.rawdata.exchange}")
	private String exch;

	@Autowired
	RabbitAdmin rabbitAdmin;
	@Autowired
	ConnectionFactory connectionFactory;

	private Map<String, TopicExchange> topicExchangeMap = new ConcurrentHashMap<>();
	private Map<String, Queue> queueMap = new ConcurrentHashMap<>();
	private Map<String, AbstractMessageListenerContainer> queueListenerMap = new HashMap<>();

	public MQConfig() {
		/*
		 * CachingConnectionFactory cachingConnectionFactory = new
		 * CachingConnectionFactory();
		 * cachingConnectionFactory.setAddresses(host);
		 * cachingConnectionFactory.setUsername(userName);
		 * cachingConnectionFactory.setPassword(passwd);
		 * cachingConnectionFactory.setVirtualHost(virtualHost);
		 * cachingConnectionFactory.setPublisherConfirms(true);
		 * connectionFactory = cachingConnectionFactory;
		 * 
		 * rabbitAdmin = new RabbitAdmin(connectionFactory);
		 */
	}

	@Bean
	RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}

	public void addNewQueueAndListener(String queueName, String exchangeName, String bindKey,
			MessageListener msgListener) {
		TopicExchange topicExchange = null;
		if (topicExchangeMap.get(exchangeName) == null) {
			topicExchange = new TopicExchange(exchangeName, true, false);
			rabbitAdmin.declareExchange(topicExchange);
			topicExchangeMap.put(exchangeName, topicExchange);
		} else {
			topicExchange = topicExchangeMap.get(exchangeName);
		}
		Queue queue = null;
		if (queueMap.get(queueName) == null) {
			queue = new Queue(queueName, true);
			rabbitAdmin.declareQueue(queue);
			queueMap.put(queueName, queue);

			bindListener2Queue(queue, msgListener);
		}

		rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(topicExchange).with(bindKey));
	}

	public void delQueueAndListener(String queueName) {
		AbstractMessageListenerContainer listenerContainer = queueListenerMap.get(queueName);
		queueListenerMap.remove(queueName);
		listenerContainer.stop();
		listenerContainer.destroy();

		Queue queue = queueMap.get(queueName);
		queueMap.remove(queueName);
		rabbitAdmin.deleteQueue(queueName);
	}

	private void bindListener2Queue(Queue queue, MessageListener msgListener) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueues(queue);
		container.setMessageListener(msgListener);
		container.start();
		queueListenerMap.put(queue.getName(), container);
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

	/*
	 * public String getHost() { return host; }
	 * 
	 * public String getUserName() { return userName; }
	 * 
	 * public String getPasswd() { return passwd; }
	 * 
	 * public String getVirtualHost() { return virtualHost; }
	 */
	public String getExch() {
		return exch;
	}

	public ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public RabbitAdmin getRabbitAdmin() {
		return rabbitAdmin;
	}
}
