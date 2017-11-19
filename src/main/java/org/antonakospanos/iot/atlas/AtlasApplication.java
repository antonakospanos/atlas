package org.antonakospanos.iot.atlas;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		JmsAutoConfiguration.class,
		EmbeddedServletContainerAutoConfiguration.class
})
@ComponentScan(basePackages = { "com.upstreamsystems.curry" })
@ImportResource("/WEB-INF/curry-interface-context.xml")
public class AtlasApplication extends SpringBootServletInitializer {
	
	private static final String CONFIG_NAME = "curry-interface-application";

	@Value("${application.configuration.file-location}")
	String fileLocation;

	@Value("${application.configuration.smpp-channels-file-location}")
	String smppChannelsFileLocation;
	
	@Value("${application.configuration.queue-management-file-location}")
	String queueManagementFileLocation;

	public static void main(String[] args) {
		new SpringApplicationBuilder(CurryApiApplication.class).
				properties(ImmutableMap.of("spring.config.name", CONFIG_NAME))
				.build()
				.run(args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.properties(ImmutableMap.of("spring.config.name", CONFIG_NAME));
	}

	@Bean
	public FlowsManager flowsManager() {
		FlowsManager flowsManager = new FlowsYamlManager(this.fileLocation);
		return flowsManager;
	}

	@Bean
	public SmppChannelsManager smppChannelsManager() {
		SmppChannelsManager smppChannelsManager = new SmppChannelsYamlManager(this.smppChannelsFileLocation);
		return smppChannelsManager;
	}
	
	@Bean
	public QueueManagementManager queueManagementManager() {
		QueueManagementManager queueManagementManager = new QueueManagementYamlManager(this.queueManagementFileLocation);
		return queueManagementManager;
	}
	
	/**
	 * @return MultipartFile Resolver
	 */
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(5000000);
		
		return multipartResolver;
	}
	
	/**
	* @return File Upload Filter
	*/
	@Bean
	public FilterRegistrationBean openEntityManagerFilterRegistrationBean() {
		final MultipartFilter multipartFilter = new MultipartFilter();
		final FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(multipartFilter);
		filterRegistrationBean.addInitParameter("multipartResolverBeanName", "commonsMultipartResolver");

		return filterRegistrationBean;
	}

	public String getFileLocation() {
		return this.fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
}
