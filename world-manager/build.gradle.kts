plugins {
    `extension-conventions`
}

version = "0.1.0"

dependencies {
    implementation(project(":util"))
    implementation("dev.hollowcube:polar:1.14.2")
    implementation("org.spongepowered:configurate-yaml:4.0.0")
}

tasks.runHexServer {
    addExtensionFromTask(":luckperms-as-extension:shadowJar")
}