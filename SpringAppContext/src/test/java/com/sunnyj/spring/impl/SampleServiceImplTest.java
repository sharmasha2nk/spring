package com.sunnyj.spring.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sunnyj.spring.SampleService;
import com.sunnyj.spring.config.AppConfig;
import com.sunnyj.spring.config.PropertyConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class SampleServiceImplTest {

	@Autowired
	private PropertyConfig propertyConfig;

	@Test
	public void testGetMessage() {
		try (ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:applicationContext.xml")) {
			SampleService sampleServiceImpl = applicationContext
					.getBean(SampleServiceImpl.class);
			assertEquals("Hello World", sampleServiceImpl.getMessage());
		}
	}

	@Test
	public void testGetMessageRuntimeRegisterBean() {
		try (ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:applicationContext.xml")) {
			BeanDefinitionBuilder builder = BeanDefinitionBuilder
					.rootBeanDefinition(SampleServiceImpl.class);
			builder.addPropertyValue("message", "Hello World Again");
			DefaultListableBeanFactory factory = (DefaultListableBeanFactory) applicationContext
					.getBeanFactory();
			factory.registerBeanDefinition("runtimeSampleServiceImpl",
					builder.getBeanDefinition());
			SampleService sampleServiceImpl = applicationContext.getBean(
					"runtimeSampleServiceImpl", SampleServiceImpl.class);
			assertEquals("Hello World Again", sampleServiceImpl.getMessage());
		}
	}

	@Test
	public void testGetMessageRuntimeAddAnotherContextFile() {
		try (ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:applicationContext.xml");
				ClassPathXmlApplicationContext applicationContextTest = new ClassPathXmlApplicationContext(
						"classpath:applicationContext-test.xml");) {
			applicationContext.getBeanFactory().setParentBeanFactory(
					applicationContextTest.getBeanFactory());
			SampleService sampleServiceImpl = applicationContext.getBean(
					"sampleServiceTestImpl", SampleServiceImpl.class);
			assertEquals("Test Hello World", sampleServiceImpl.getMessage());
		}
	}

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void testGetMessageRuntimeAddAnotherContextFileRemoveOldBeans() {
		try (ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:applicationContext.xml");
				ClassPathXmlApplicationContext applicationContextTest = new ClassPathXmlApplicationContext(
						"classpath:applicationContext-test.xml");) {
			ConfigurableListableBeanFactory beanFactory = applicationContext
					.getBeanFactory();
			BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext
					.getAutowireCapableBeanFactory();
			beanDefinitionRegistry.removeBeanDefinition("sampleServiceImpl");
			beanFactory.setParentBeanFactory(applicationContextTest
					.getBeanFactory());
			SampleService sampleServiceImpl = applicationContext.getBean(
					"sampleServiceTestImpl", SampleServiceImpl.class);
			assertEquals("Test Hello World", sampleServiceImpl.getMessage());
			applicationContext.getBean("sampleServiceImpl",
					SampleServiceImpl.class);
		}
	}

	@Test
	public void testGetMessageUsingAutowiredUsingAppConfigClass() {
		try (AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
				AppConfig.class);) {
			SampleService sampleServiceImpl = applicationContext.getBean(
					"sampleAutowiredService", SampleServiceAutowiredImpl.class);
			assertEquals("Hello in annotation world",
					sampleServiceImpl.getMessage());
		}
	}

	@Test
	public void testGetMessageUsingBeanDefinedInAppConfigClass() {
		try (AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
				AppConfig.class);) {
			SampleService sampleServiceImpl = applicationContext.getBean(
					"sampleService", SampleServiceImpl.class);
			assertEquals("Hello from AppConfig", sampleServiceImpl.getMessage());
		}
	}

	@Test
	public void testGetMessageFromPropertyFileUsingBeanDefinedInAppConfigClass() {
		try (AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
				AppConfig.class);) {
			SampleService sampleServiceImpl = applicationContext.getBean(
					"sampleServiceMsgFromProp", SampleServiceImpl.class);
			assertEquals("Hello from property file",
					sampleServiceImpl.getMessage());
		}
	}

	@Test
	public void testGetMessageFromPropertyFileUsingValueAnnotationUsingBeanDefinedInAppConfigClass() {
		try (AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
				AppConfig.class);) {
			SampleService sampleServiceImpl = applicationContext.getBean(
					"sampleServiceMsgFromPropUsingValueAnno",
					SampleServiceImpl.class);
			assertEquals(propertyConfig.env.getProperty("HELLO_WORLD"),
					sampleServiceImpl.getMessage());
		}
	}

	@Test
	public void testGetMessageUsingAppConfigClassBeanDefinedInAppContextXml() {
		try (AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
				AppConfig.class);) {
			SampleService sampleServiceImpl = applicationContext.getBean(
					"sampleServiceImpl", SampleServiceImpl.class);
			assertEquals("Hello World", sampleServiceImpl.getMessage());
		}
	}
}
