import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.1"
	id("io.spring.dependency-management") version "1.1.4"
	id("com.google.cloud.tools.jib") version "3.3.2"
	kotlin("jvm") version "1.9.21"
	kotlin("plugin.spring") version "1.9.21"
	id("org.jlleitschuh.gradle.ktlint").version("12.1.1")
}

val imageTargetEnv =
	if (project.hasProperty("projectDataImageTargetEnv")) {
		project.property("projectDataImageTargetEnv")
	} else {
		System.getenv("SPRING_PROFILES_ACTIVE") ?: "local"
	}

group = "project"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring Setting
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.retry:spring-retry")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
	implementation("me.paulschwarz:spring-dotenv:4.0.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

	// Database
	runtimeOnly("com.mysql:mysql-connector-j")
	testRuntimeOnly("com.h2database:h2")
	implementation("org.springframework.cloud:spring-cloud-gcp-starter-sql-mysql:1.2.8.RELEASE")
	implementation("com.vladmihalcea:hibernate-types-60:2.20.0")
	// Database Redis
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("io.lettuce:lettuce-core") // 기본 Redis 클라이언트

	// Util
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	implementation("io.github.microutils:kotlin-logging:3.0.5")
	compileOnly("ch.qos.logback.contrib:logback-jackson:0.1.5")
	compileOnly("ch.qos.logback.contrib:logback-json-classic:0.1.5")
	implementation("net.datafaker:datafaker:2.0.2")
	implementation("com.google.code.gson:gson:2.8.5")
	implementation("org.zeroturnaround:zt-zip:1.8")
	implementation("net.logstash.logback:logstash-logback-encoder:7.4")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
	implementation("org.json:json:20231013")
	implementation("org.liquibase:liquibase-core")
	implementation("org.apache.httpcomponents.client5:httpclient5:5.2.1")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

jib {
	from {
		image = "amazoncorretto:17-alpine-jdk"
		platforms {
			platform {
				architecture = "amd64"
				os = "linux"
			}
			platform {
				architecture = "arm64"
				os = "linux"
			}
		}
	}
	to {
		image =
			when (imageTargetEnv) {
				"prod" -> "[hostname]/[project-name]/[project-name]/prod"
				"dev" -> "[hostname]/[gcp-project-name]/[project-name]/dev"
				else -> "docker-repo.minq.work/myapp:latest"
			}
		auth {
			username = System.getenv("REGISTRY_USER")
			password = System.getenv("REGISTRY_PASSWORD")
		}
	}
	container {
		jvmFlags =
			when (imageTargetEnv) {
				"prod" -> listOf("-XX:+UseContainerSupport", "-Dfile.encoding=UTF-8", "-Dspring.profiles.active=prod")
				"dev" -> listOf("-XX:+UseContainerSupport", "-Dfile.encoding=UTF-8", "-Dspring.profiles.active=dev")
				else -> listOf("-XX:+UseContainerSupport", "-Dfile.encoding=UTF-8", "-Dspring.profiles.active=local")
			}
		ports = listOf("8080", "80", "8088", "9090")
	}
}
