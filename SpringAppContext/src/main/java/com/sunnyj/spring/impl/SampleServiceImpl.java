package com.sunnyj.spring.impl;

import com.sunnyj.spring.SampleService;

public class SampleServiceImpl implements SampleService {

	private String message;

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
