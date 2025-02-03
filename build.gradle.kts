plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2"
}

group = "com.dylanmiska"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.25")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.25")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.2")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.4.2")
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.2")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.4")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")
    implementation("org.liquibase:liquibase-core:4.31.0")
    runtimeOnly("com.h2database:h2:2.3.232")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.2")
    testImplementation("io.mockk:mockk:1.13.16")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.9.25")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.4")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
