/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/8.1.1/userguide/building_java_projects.html
 */

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.8.10"

    // Apply the java-library plugin for API and implementation separation.
    `java-library`
    `maven-publish`

}

group = project.findProperty("group") as String? ?: "com.ingenifi"
version = project.findProperty("version") as String? ?: "1.0.0"

val junitJupiterVersion="5.9.3"
val kieVersion="8.33.0.Final"

repositories {
    mavenCentral()
}

dependencies {
    // Use the Kotlin JUnit 5 integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    // Use the JUnit 5 integration.
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("io.kotest:kotest-runner-junit5:4.3.2")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.1-jre")

    // Add the Drools dependencies.
    implementation("org.kie:kie-api:$kieVersion")
    implementation("org.drools:drools-compiler:$kieVersion")
    implementation("org.drools:drools-mvel:$kieVersion")

    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("ch.qos.logback:logback-classic:1.4.7")

}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(19))
    }
}


tasks.named<Test>("test") {
    useJUnitPlatform()
   // systemProperty("logback.configurationFile", "src/test/resources/logback.xml")
}

tasks.register("printClasspath") {
    doLast {
        configurations["compileClasspath"].forEach { println(it) }

    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "infera"
        }
    }
}
