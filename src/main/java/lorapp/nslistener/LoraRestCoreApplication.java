package lorapp.nslistener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootApplication
@WebAppConfiguration
public class LoraRestCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoraRestCoreApplication.class, args);
	}
}
