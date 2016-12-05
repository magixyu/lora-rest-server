package lorapp.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"lora.db", "lora.rest"})
public class LoraRestCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoraRestCoreApplication.class, args);
	}
}
