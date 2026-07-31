package com.neueda.rest;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.boot.SpringApplication.run;

@SpringBootTest
class RestApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainStartsApplicationWhenArgsProvided() {
		ConfigurableApplicationContext context = mock(ConfigurableApplicationContext.class);
		String[] args = new String[] {"--server.port=0"};

		try (MockedStatic<org.springframework.boot.SpringApplication> springApplication = mockStatic(org.springframework.boot.SpringApplication.class)) {
			springApplication.when(() -> run(RestApplication.class, args)).thenReturn(context);

			RestApplication.main(args);

			springApplication.verify(() -> run(RestApplication.class, args));
		}
	}

	@Test
	void mainStartsApplicationWhenArgsAreEmpty() {
		ConfigurableApplicationContext context = mock(ConfigurableApplicationContext.class);
		String[] args = new String[] {};

		try (MockedStatic<org.springframework.boot.SpringApplication> springApplication = mockStatic(org.springframework.boot.SpringApplication.class)) {
			springApplication.when(() -> run(RestApplication.class, args)).thenReturn(context);

			RestApplication.main(args);

			springApplication.verify(() -> run(RestApplication.class, args));
		}
	}

	@Test
	void mainPassesNullArgsToSpringApplication() {
		ConfigurableApplicationContext context = mock(ConfigurableApplicationContext.class);

		try (MockedStatic<org.springframework.boot.SpringApplication> springApplication = mockStatic(org.springframework.boot.SpringApplication.class)) {
			springApplication.when(() -> run(eq(RestApplication.class), nullable(String[].class))).thenReturn(context);

			RestApplication.main(null);

			springApplication.verify(() -> run(eq(RestApplication.class), nullable(String[].class)));
		}
	}

	@Test
	void mainPropagatesStartupFailure() {
		RuntimeException startupFailure = new RuntimeException("startup failed");

		try (MockedStatic<org.springframework.boot.SpringApplication> springApplication = mockStatic(org.springframework.boot.SpringApplication.class)) {
			springApplication.when(() -> run(eq(RestApplication.class), any(String[].class))).thenThrow(startupFailure);

			RuntimeException thrown = assertThrows(RuntimeException.class, () -> RestApplication.main(new String[] {"--debug"}));

			org.junit.jupiter.api.Assertions.assertEquals(startupFailure, thrown);
		}
	}

}
