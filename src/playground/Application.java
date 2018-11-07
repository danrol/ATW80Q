package playground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"elements"})
@PropertySource("classpath:resources/application.properties")
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	//test_DANIEL
//	public static void main(String[] args) {
//		
//		// TODO Spring IoC should instantiate this
//		MessageGenerator generator = new SimpleMessageGenerator();
//		
//		// TODO Spring IoC should instantiate this and injects generator
//		UI ui = new UI (generator);
//		
//		// TODO Spring will invoke this method after initialization
//		ui.showMessage();
//	}
}
