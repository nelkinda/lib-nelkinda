plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "lib-nelkinda"

include(
    "lib-nelkinda",
    "lib-nelkinda-test",
    "lib-nelkinda-test-selenium",
)
