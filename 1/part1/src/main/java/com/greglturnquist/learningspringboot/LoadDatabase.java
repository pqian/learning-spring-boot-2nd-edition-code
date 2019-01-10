package com.greglturnquist.learningspringboot;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.config.DownloadConfigBuilder;
import de.flapdoodle.embed.mongo.config.ExtractedArtifactStoreBuilder;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.config.store.HttpProxyFactory;
import reactor.core.publisher.Flux;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

	@Bean
	CommandLineRunner init(ChapterRepository repository) {
		return args -> {
			Flux.just(
				new Chapter("Quick Start with Java"),
				new Chapter("Reactive Web with Spring Boot"),
				new Chapter("...and more!"))
			.flatMap(repository::save)
			.subscribe(System.out::println);
		};
	}

	//@Bean
	public IRuntimeConfig embeddedMongoRuntimeConfig() {
		System.out.println("set custom proxy");
		final Command command = Command.MongoD;
		final IRuntimeConfig runtimeConfig = new RuntimeConfigBuilder().defaults(command)
				.artifactStore(new ExtractedArtifactStoreBuilder().defaults(command).download(new DownloadConfigBuilder()
						.defaultsForCommand(command).proxyFactory(new HttpProxyFactory("proxy", 8080)).build()))
						.build();
		return runtimeConfig;
	}

}
