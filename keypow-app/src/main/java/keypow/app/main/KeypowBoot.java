package keypow.app.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import keypow.app.model.Keypow;


@SpringBootApplication
public class KeypowBoot {
	public static void main(String[] args) {
		SpringApplication.run(KeypowBoot.class, args);
	}
	
	@Bean
	public Vertx vertx() {
		return Vertx.vertx();
	}
	
	@Bean(initMethod="init")
	public KeypowApp keypowApp(Vertx vertx, Keypow keypow) {
		return new KeypowApp(vertx, keypow);
	}
	
	@Bean
	public Keypow keypow(KeypowConfig config) {
		return new Keypow(config.getWalletUrl(), config.getGameUrl());
	}
}