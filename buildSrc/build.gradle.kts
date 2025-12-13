plugins {
    kotlin("jvm") version "2.0.21"
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
}

group = "org.example"
version = "1.0"
gradlePlugin {
    plugins.create("greeting") {
        id = "org.example.aoc"
        implementationClass = "AOCPlugin"
    }
}