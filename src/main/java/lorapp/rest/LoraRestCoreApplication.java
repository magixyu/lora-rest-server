package lorapp.rest;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages="lorapp.db")
@EntityScan(basePackages="lorapp.db")
@SpringBootApplication
public class LoraRestCoreApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(LoraRestCoreApplication.class, args);
	}
}
