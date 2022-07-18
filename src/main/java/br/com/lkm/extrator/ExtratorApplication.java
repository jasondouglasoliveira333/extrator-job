package br.com.lkm.extrator;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExtratorApplication extends SpringBootServletInitializer {

	public static void main(String... args) {
		System.out.println(System.getProperties());
		System.out.println("currentDir:" + new File(".").getAbsolutePath());
		SpringApplication.run(ExtratorApplication.class);
	}
}
