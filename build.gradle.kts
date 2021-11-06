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
}

tasks {
    withType<ProcessResources> {
        filteringCharset = "UTF-8"
        from(projectDir).include("LICENSE")
    }

    withType<Jar> {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        from(configurations.api.get().apply { isCanBeResolved = true }.map { if (it.isDirectory) it else zipTree(it) })

        manifest.attributes("Main-Class" to "dev.siro256.serverchecker.ServerChecker")
    }
}
