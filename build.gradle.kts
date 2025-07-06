subprojects.forEach { subproject ->
    subproject.group = "io.github.ynverxe.hex-extensions"

    subproject.plugins.withId("java") {
        subproject.tasks.named<ProcessResources>("processResources") {
            filteringCharset = "UTF-8"

            filesMatching("extension.conf") {
                expand(
                    "version" to subproject.version
                )
            }
        }
    }
}