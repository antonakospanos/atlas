package org.antonakospanos.iot.atlas.web.configuration;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
// @EnableAutoConfiguration
@EnableSwagger2
public class SwaggerConfiguration {

	@Bean
	public Docket newsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Internet-of-Things")
				.apiInfo(apiInfo())
				.select()
				.paths(paths())
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Atlas API")
				.contact("itac developments")
				.description("Atlas API handles events from Internet of Things (IoT) devices")
				.build();
	}

	private Predicate<String> paths() {
		return regex(".*/(devices|events|actions).*");
		// return or(regex("/*"));
	}
}