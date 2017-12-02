package org.antonakospanos.iot.atlas;

import com.google.common.collect.ImmutableMap;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration//(exclude={DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"org.antonakospanos.iot.atlas"})
public class AtlasApplication extends SpringBootServletInitializer {

	private static final String CONFIG_NAME = "atlas-application";

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.properties(ImmutableMap.of("spring.config.name", CONFIG_NAME));
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder(AtlasApplication.class).
				properties(ImmutableMap.of("spring.config.name", CONFIG_NAME))
				.build()
				.run(args);
	}
}
