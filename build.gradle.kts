plugins {
    val kotlinVersion = "1.5.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.8.0-RC"
}

group = "org.laolittle.plugin"
version = "1.0.2"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}
