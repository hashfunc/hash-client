import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"

    application

    id("com.diffplug.spotless") version "6.10.0"
}

group = "io.hashfunc"
version = "0.1.0"

repositories {
    mavenCentral()
}

val upstreamPath = "hafen-client"

dependencies {
    implementation(fileTree("${upstreamPath}/lib") { include("**/*.jar") })

    annotationProcessor(files("${upstreamPath}/lib/jglob.jar"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.processResources {
    with(copySpec {
        from("${upstreamPath}/etc")
        include(
            "authsrv.crt",
            "icon.png",
            "res-bgload",
            "res-preload",
            "ressrv.crt",
        )
        rename { "haven/${it}" }
    })
    with(copySpec {
        from("${upstreamPath}/etc")
        include("ansgar-config.properties")
        rename { it.replace("ansgar", "haven") }
        into("../../classes/java")
    })
}

sourceSets {
    main {
        java {
            srcDir("${upstreamPath}/src")
        }
    }
}

application {
    mainClass.set("MainKt")
}

spotless {
    kotlin {
        ktlint("0.46.1")
    }
}
