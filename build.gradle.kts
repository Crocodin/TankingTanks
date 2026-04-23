plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.1.0"
    application

    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
}

group = "ubb.dbsm"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Test
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Lombok
    implementation("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    // SQL Server
    implementation("com.microsoft.sqlserver:mssql-jdbc:13.2.1.jre11")

//    // Source: https://mvnrepository.com/artifact/org.hibernate.orm/hibernate-core
//    implementation("org.hibernate.orm:hibernate-core:6.5.2.Final")
//
//    // Source: https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core
//    implementation("org.apache.logging.log4j:log4j-core:2.25.3")
//
//    // Source: https://mvnrepository.com/artifact/com.zaxxer/HikariCP
//    implementation("com.zaxxer:HikariCP:5.1.0")

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // EhCache
    implementation("org.ehcache:ehcache:3.10.8")
    implementation("javax.cache:cache-api:1.1.1")
}

javafx {
    version = "21.0.9"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("ubb.dbsm.Main")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}