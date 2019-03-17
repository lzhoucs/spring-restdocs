/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.junit5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SampleJUnit5Application {

	public static void main(String[] args) {
		new SpringApplication(SampleJUnit5Application.class).run(args);
	}

	@RestController
	private static class SampleController {

		@RequestMapping("/hello")
		public HelloModel index() {
			return new HelloModel("hello", "hello world!", 1.0);
		}
	}

	public static class HelloModel {
		private String name;
		private String message;
		private double version;

		public HelloModel(String name, String message, double version)
		{
			this.name = name;
			this.message = message;
			this.version = version;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getMessage()
		{
			return message;
		}

		public void setMessage(String message)
		{
			this.message = message;
		}

		public double getVersion()
		{
			return version;
		}

		public void setVersion(double version)
		{
			this.version = version;
		}
	}

}
