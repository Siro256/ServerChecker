import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
}

group = "dev.siro256.serverchecker"
version = "1.0.0-SNAPSHOT"

repositories {
    maven { url = uri("https://repo.siro256.dev/repository/maven-public/") }
}

dependencies {
    api(kotlin("stdlib"))

    api("io.ktor:ktor-client-core:1.6.5")
    api("io.ktor:ktor-client-cio:1.6.5")

    api("dev.siro256:kotlin-consolelib:1.0.0")
    api("dev.siro256:kotlin-eventlib:1.0.0")

    api("org.yaml:snakeyaml:1.29")
}

tasks {
    withType<KotlinCompile> {
        //Strict
        kotlinOptions.allWarningsAsErrors = true
    }

    withType<ProcessResources> {
        filteringCharset = "UTF-8"
        from(projectDir) {
            include("LICENSE")
        }
    }

    withType<Jar> {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        from(configurations.api.get().apply { isCanBeResolved = true }.map { if (it.isDirectory) it else zipTree(it) })

        manifest.attributes("Main-Class" to "dev.siro256.serverchecker.ServerChecker")
    }
}
