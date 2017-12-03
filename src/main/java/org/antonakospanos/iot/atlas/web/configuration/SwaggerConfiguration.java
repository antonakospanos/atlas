package org.antonakospanos.iot.atlas.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

//	@Bean(name = "Admin API")
//	public Docket adminApi() {
//		return new Docket(DocumentationType.SWAGGER_2)
//				.groupName("Admin API")
//				.apiInfo(adminInfo())
//				.select()
//				.paths(regex(".*/(admin).*"))
//				.build();
//	}
//
//	private ApiInfo adminInfo() {
//		return new ApiInfoBuilder()
//				.title("Admin API")
//				.contact("IoTac")
//				.description("Atlas Administration Interface ")
//				.build();
//	}

	@Bean(name = "Atlas API")
	public Docket atlasApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Atlas API")
				.apiInfo(atlasInfo())
				.select()
				.paths(regex(".*/(devices|actions|events).*"))
				.build();
	}

	private ApiInfo atlasInfo() {
		return new ApiInfoBuilder()
				.title("Atlas API")
				.contact("IoTac")
				.description("Integration with Internet of Things (IoT) Devices")
				.build();
	}

//	private Predicate<String> paths() {
//		 return (regex("/*"));
//	}
}