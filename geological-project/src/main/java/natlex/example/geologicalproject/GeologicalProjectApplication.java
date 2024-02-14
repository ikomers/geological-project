package natlex.example.geologicalproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GeologicalProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeologicalProjectApplication.class, args);
	}

}
