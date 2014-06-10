package com.sunnyj.spring.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.sunnyj.spring.SampleService;

@Primary
@Service("sampleAutowiredService")
public class SampleServiceAutowiredImpl implements SampleService {

	@Override
	public String getMessage() {
		return "Hello in annotation world";
	}

}
