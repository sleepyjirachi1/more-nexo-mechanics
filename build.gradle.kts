plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "me.Autumn"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.compileJava {
    options.encoding = "UTF-8"
    sourceCompatibility = "21"
    targetCompatibility = "21"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.nexomc.com/releases")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
    compileOnly("net.kyori:adventure-api:4.25.0")
    compileOnly("com.nexomc:nexo:1.15.0")

    // Kotlin runtime
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")

    // Helper libraries
    compileOnly("org.jetbrains:annotations:26.0.2")
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
}

tasks.shadowJar {
    archiveBaseName.set("more-nexo-mechanics")
    archiveClassifier.set("")
    archiveVersion.set(version.toString())
}
