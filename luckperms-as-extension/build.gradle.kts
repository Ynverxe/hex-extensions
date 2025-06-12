import io.github.ynverxe.hexserver.plugin.run.RunHexServerTask

plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta13"
    `extension-conventions`
}

version = "0.1.0"

dependencies {
    implementation("dev.lu15:luckperms-minestom:5.4-SNAPSHOT")
}

tasks.named<RunHexServerTask>("runHexServer") {
    workingDir("run")
    serverJarProviderTask("generateLauncher")
    addExtensionFromTask("shadowJar")
    standardInput = System.`in`
}