import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.intellij.platform") // Removed the version here to fix the classpath error
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        // Targets your exact sandbox version
        intellijIdeaCommunity("2025.2.6.2")
        testFramework(TestFrameworkType.Platform)

        // CRITICAL: This tells Gradle to download the K2 developer API libraries
        // and add them to your IDE project classpath
        bundledPlugin("org.jetbrains.kotlin")
    }
}

intellijPlatform {
    pluginConfiguration {
        id = "com.sahiljaroli.composeoptimizer"
        name = "Compose Optimizer"
        vendor {
            name = "SahilJaroli"
            email = "sahiljaroli159@gmail.com"
        }
    }
    // MAPS SECURE TOKENS DIRECTLY TO SYSTEM ENVIRONMENT PATHS
    publishing {
        token.set(providers.environmentVariable("PUBLISH_TOKEN"))
        channels.set(listOf("default"))
    }
}