import io.github.ynverxe.hexserver.plugin.GenerateLauncherTask
import io.github.ynverxe.hexserver.plugin.HexPlugin
import io.github.ynverxe.hexserver.plugin.file.DefaultConfiguration
import io.github.ynverxe.hexserver.plugin.run.RunHexServerTask

plugins {
    id("java")
}

val minestomCoordinates = "net.minestom:minestom-snapshots:ebaa2bbf64"

apply(plugin = "com.github.johnrengelman.shadow")
apply<HexPlugin>()

tasks.named<GenerateLauncherTask>("generateLauncher") {
    serverFiles {
        handleConfiguration(DefaultConfiguration.MINESTOM_SOURCE) {
            coordinates(minestomCoordinates)
            mavenCentral()
        }
    }
}

tasks.named<RunHexServerTask>("runHexServer") {
    workingDir("run")
    serverJarProviderTask("generateLauncher")
    addExtensionFromTask("shadowJar")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.hypera.dev/snapshots/")
}

dependencies {
    compileOnly("io.github.ynverxe:hex-core:.")
    compileOnly(minestomCoordinates)
    compileOnly("org.jetbrains:annotations:24.0.0")
}