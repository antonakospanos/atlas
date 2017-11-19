package org.antonakospanos.iot.atlas.configuration;

import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
// @EnableAutoConfiguration
@EnableSwagger2
@ComponentScan(basePackages = { "com.upstreamsystems.curry.api" })
public class SwaggerConfiguration {

	@Autowired
	Environment env;

	// @Bean
	// public Docket swaggerSpringMvcPlugin() {
	// return new Docket(DocumentationType.SWAGGER_2)
	// .groupName("curry-interface")
	// .apiInfo(apiInfo());
	// }

	@Bean
	public Docket newsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("curry-interface")
				.apiInfo(apiInfo())
				.select()
				.paths(paths())
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Curry Interface")
				//.contact("Curry Dev Team")
				.description("An interface to Curry Gateway's configuration")
				.build();
	}

	private Predicate<String> paths() {
		return regex(".*/(flows|file|forms|schemas|version|throttling|queues).*");
		// return or(regex("/*"));
	}
}