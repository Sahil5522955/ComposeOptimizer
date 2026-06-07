import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.intellij.platform")
    id("org.jetbrains.changelog")
}

dependencies {
    testImplementation("junit:junit:4.13.2")

    intellijPlatform {
        intellijIdea("2025.2.6.2")
        testFramework(TestFrameworkType.Platform)

        bundledPlugin("org.jetbrains.kotlin")
    }
}

intellijPlatform {
    pluginConfiguration {
        name = "Compose Optimizer"
    }
}

tasks {
    runIde {
        // These system arguments instruct the 2025 sandbox runtime environment
        // to bypass strict K2 verification checks for local debugging
        jvmArgs("-Didea.kotlin.plugin.k2=false", "-Dkotlin.k2.modules.as.k1=true")
    }
}