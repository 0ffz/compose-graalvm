plugins {
    kotlin("jvm") version "2.1.0"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0"
    id("org.jetbrains.compose") version "1.7.1"
    id("org.graalvm.buildtools.native") version "0.10.4"
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(compose.desktop.currentOs)
    compileOnly("org.graalvm.nativeimage:library-support:24.1.1")
}

kotlin {
    jvmToolchain(21)
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}

graalvmNative {
    toolchainDetection.set(false)
    binaries {
        named("main") {
            fallback = false
            mainClass.set("MainKt")
            resources
            imageName.set("compose-graalvm")
            buildArgs(
                "-Ob", // faster builds, remove for prod
                "-Djava.awt.headless=false",
                "--strict-image-heap", // Enabled by default on GraalVM 23, however needed on 21 for some libraries running Kotlin 2.0
                "-march=native",
                "-R:MaxHeapSize=150M",
            )
        }
    }

    agent {
        defaultMode.set("standard")

        metadataCopy {
            inputTaskNames.add("run") // Tasks previously executed with the agent attached.
            outputDirectories.add("src/main/resources/META-INF/native-image")
            mergeWithExisting.set(true)
        }
    }
}

tasks {
    val projectName = "compose-graalvm"

    val copyLibjawt = task<ProcessResources>("copyLibjawt") {
        val source = "build/compose/binaries/main/app/$projectName/lib/runtime/lib/libjawt.so"
        val target = "build/native/nativeCompile/lib"
        dependsOn("createDistributable")
        from(source)
        into(target)
    }

    named("nativeCompile") {
        dependsOn(copyLibjawt)
    }
}
