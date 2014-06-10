package com.sunnyj.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

import com.sunnyj.spring.SampleService;
import com.sunnyj.spring.impl.SampleServiceImpl;

@Configuration
@Import(PropertyConfig.class)
@ComponentScan("com.sunnyj.spring.impl")
@PropertySource("classpath:app.constants")
@ImportResource("classpath:applicationContext.xml")
public class AppConfig {

	@Autowired
	private PropertyConfig propertyConfig;

	/**
	 * To enable this template we have <context:property-placeholder/> in xml file 
	 */
	@Value("${HELLO_WORLD}")
	private String message;

	@Bean(name = "sampleService")
	public SampleService sampleService() {
		SampleServiceImpl sampleServiceImpl = new SampleServiceImpl();
		sampleServiceImpl.setMessage("Hello from AppConfig");
		return sampleServiceImpl;
	}

	@Bean(name = "sampleServiceMsgFromProp")
	public SampleService sampleServiceMessageFromPropertyFile() {
		SampleServiceImpl sampleServiceImpl = new SampleServiceImpl();
		sampleServiceImpl.setMessage(propertyConfig.env.getProperty("message"));
		return sampleServiceImpl;
	}

	@Bean(name = "sampleServiceMsgFromPropUsingValueAnno")
	public SampleService sampleServiceMessageFromPropertyFileUsingValueAnnotation() {
		SampleServiceImpl sampleServiceImpl = new SampleServiceImpl();
		sampleServiceImpl.setMessage(message);
		return sampleServiceImpl;
	}

}
