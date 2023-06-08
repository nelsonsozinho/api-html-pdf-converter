plugins {
	java
	id("org.springframework.boot") version "3.1.0"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "com.shire42"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation ("com.google.guava:guava:31.1-jre")

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	implementation ("software.amazon.awssdk:ses:2.20.67")
	implementation ("software.amazon.awssdk:sesv2:2.20.67")
	implementation ("software.amazon.awssdk:protocol-core:2.20.67")

	implementation ("com.sun.mail:jakarta.mail:2.0.1")
	implementation ("javax.activation:activation:1.1.1")
	implementation ("com.itextpdf:itextpdf:5.5.13.3")
	implementation ("com.itextpdf:html2pdf:5.0.0")
	implementation ("org.bouncycastle:bcprov-jdk15on:1.49")
	implementation ("commons-io:commons-io:2.12.0")

	testCompileOnly ("org.projectlombok:lombok:1.18.26")
	testAnnotationProcessor ("org.projectlombok:lombok:1.18.26")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
