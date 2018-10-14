package org.antonakospanos.iot.atlas.web.configuration;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
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
//				.contact("iotac")
//				.description("Atlas Administration Interface ")
//				.build();
//	}

	@Bean(name = "Atlas API")
	public Docket atlasApiv1() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Atlas API")
				.apiInfo(atlasInfo())
				.select()
				.paths(apiV1Paths())
				.build();
	}

	@Bean(name = "Atlas API v2")
	public Docket atlasApiv2() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Atlas API v2")
				.apiInfo(atlasInfo())
				.select()
				.paths(apiV2Paths())
				.build();
	}

	private ApiInfo atlasInfo() {
		return new ApiInfoBuilder()
				.title("Atlas API")
				.contact("iotac")
				.description("Integration with Internet of Things (IoT) Devices")
				.build();
	}

	private Predicate<String> apiV1Paths() {
		return or(regex("/api/(accounts|devices|actions|alerts|events).*"));
	}

	private Predicate<String> apiV2Paths() {
		return or(regex("/api/v2/.*"));
	}
}