package com.loapulse;

import org.springframework.boot.SpringApplication;

/**
 * compose 없이 Testcontainers로 PostgreSQL과 Redis를 띄워 로컬 실행한다 ({@code ./gradlew bootTestRun}).
 */
public class TestLoaPulseApplication {

	public static void main(String[] args) {
		SpringApplication.from(LoaPulseApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
