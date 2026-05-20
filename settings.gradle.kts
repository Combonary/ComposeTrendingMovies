pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    // Load local.properties for GitHub credentials
    val localProperties = java.util.Properties()
    val localPropertiesFile = File(rootDir, "local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { localProperties.load(it) }
    }

    val githubActor = localProperties.getProperty("GITHUB_ACTOR") ?: System.getenv("GITHUB_ACTOR")
    val githubToken = localProperties.getProperty("GITHUB_TOKEN") ?: System.getenv("GITHUB_TOKEN")

    repositories {
        google()
        mavenCentral()
        if (!githubActor.isNullOrBlank() && !githubToken.isNullOrBlank()) {
            maven {
                url = uri("https://maven.pkg.github.com/Combonary/MoviesService")
                credentials {
                    username = githubActor.trim()
                    password = githubToken.trim()
                }
            }
        }
    }
}

rootProject.name = "ComposeTrendingMovies"
include(":app")
 