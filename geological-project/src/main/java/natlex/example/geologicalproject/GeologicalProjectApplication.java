package natlex.example.geologicalproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableAsync
@ComponentScan("natlex")
public class GeologicalProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeologicalProjectApplication.class, args);
	}

}
