package lorapp.rest;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages="lorapp.db")
@EntityScan(basePackages="lorapp.db")
@SpringBootApplication
public class LoraRestCoreApplication {
/*

	static ConnectionFactory connectionFactory;
	static RabbitAdmin rabbitAdmin;
	public static TopicExchange topicExchange;
	public static Queue rawdataABC;

	static  {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
		cachingConnectionFactory.setAddresses("mqtt.hackathonqingdao.com");
		cachingConnectionFactory.setUsername("lora");
		cachingConnectionFactory.setPassword("lora");
		cachingConnectionFactory.setPublisherConfirms(true); //必须要设置
		connectionFactory = cachingConnectionFactory;
		rabbitAdmin = new RabbitAdmin(connectionFactory);
		topicExchange = new TopicExchange("lora-rawdata-exchange", true, false);
		rawdataABC = new Queue("rawdataABC", true);
	}
*/


	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(LoraRestCoreApplication.class, args);

/*


		rabbitAdmin.declareQueue(rawdataABC);
		rabbitAdmin.declareExchange(topicExchange);
		Binding binding = BindingBuilder.bind(rawdataABC).to(topicExchange).with("key1.key2");
		rabbitAdmin.declareBinding(binding);


		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueues(rawdataABC);

		SimpleMessageConverter msgConvert = new SimpleMessageConverter();

		container.setMessageListener(new MessageListener() {
			public void onMessage(Message message) {
				System.out.println("0 received: " + message);
				System.out.println(message.getBody().toString());
				Object obj = msgConvert.fromMessage(message);
				int i=0;
			}
		});
		container.start();




		Queue rawdataDEF = new Queue("rawdataDEF", true);
		TopicExchange exchangedef = new TopicExchange("lora-rawdata-exchangedef", true, false);
		rabbitAdmin.declareQueue(rawdataDEF);
		rabbitAdmin.declareExchange(exchangedef);
		rabbitAdmin.declareBinding(BindingBuilder.bind(rawdataDEF).to(exchangedef).with("key3.key4"));

		SimpleMessageListenerContainer container1 = new SimpleMessageListenerContainer();
		container1.setConnectionFactory(connectionFactory);
		container1.setQueues(rawdataDEF);
		container1.setMessageListener(new MessageListener() {
			public void onMessage(Message message) {
				System.out.println("1 received: " + message);
			}
		});
		container1.start();


		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		int i=0;
		while(true){
			Thread.sleep(2000);
			template.convertAndSend("lora-rawdata-exchange", "app.dev", "hello rabbit"+ i);
			i++;
		}

*/




	}
}
