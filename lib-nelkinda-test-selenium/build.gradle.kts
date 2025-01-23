plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    api(libs.commons.math3)
    implementation(libs.guava)
    implementation(project(":lib-nelkinda-test"))
    implementation("commons-io:commons-io:2.18.0")
    implementation("io.cucumber:cucumber-java:7.20.1")
    implementation("io.cucumber:cucumber-junit-platform-engine:7.20.1")
    implementation("io.cucumber:cucumber-spring:7.20.1")
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")
    implementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
    implementation("org.seleniumhq.selenium:selenium-java:4.28.1") {
        exclude(group = "io.opentelemetry")
        exclude(group = "net.bytebuddy")
    }
    implementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    implementation("org.springframework:spring-context:6.2.1")
    implementation("org.springframework.boot:spring-boot:3.4.1")

    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.11.4")
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
