import org.jetbrains.gradle.ext.Application
import org.jetbrains.gradle.ext.Gradle
import org.jetbrains.gradle.ext.RunConfigurationContainer

plugins {
  id("java")
  id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.8"
  id("com.gtnewhorizons.retrofuturagradle") version "2.0.2"
  id("com.diffplug.spotless") version "8.10.3"
}

// Project properties
val modId = providers.gradleProperty("mod_id").get()
val modName = providers.gradleProperty("mod_name").get()
val archivesBaseName = providers.gradleProperty("archives_name").get()
val packageGroup = providers.gradleProperty("package_group").get()
val minecraftVersion = providers.gradleProperty("mc_version").get()
val forgeVersion = providers.gradleProperty("forge_version").get()

// Version from GitHub Actions
val modVersion = System.getenv("GITHUB_REF_NAME") ?: "0.0.0"

group = packageGroup
version = "$minecraftVersion-$modVersion"

base {
  archivesName.set(archivesBaseName)
}

// Set the toolchain version to decouple the Java we run Gradle with from the Java used to compile and run the mod
java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(8))
    // Azul covers the most platforms for Java 8 toolchains, crucially including MacOS arm64
    vendor.set(org.gradle.jvm.toolchain.JvmVendorSpec.AZUL)
  }
}

// Most RFG configuration lives here, see the JavaDoc for com.gtnewhorizons.retrofuturagradle.MinecraftExtension
minecraft {
  mcVersion.set(minecraftVersion)

  // Username for client run configurations
  username.set("Developer")

  // Generate MOD_ID, MOD_NAME and VERSION fields in the injected Tags class
  injectedTags.put("MOD_ID", modId)
  injectedTags.put("MOD_NAME", modName)
  injectedTags.put("VERSION", modVersion)

  // Enable assertions in the mod's package when running the client or server
  extraRunJvmArguments.add("-ea:${project.group}")
}

// Generates a class named taintedmagic.Tags with the mod metadata in it
tasks.injectTags.configure {
  outputClassName.set("$packageGroup.Tags")
}

// Put the mod metadata from gradle into mcmod.info
tasks.processResources.configure {
  // Local copies needed for configuration cache to work
  val replacements = mapOf(
    "mod_id" to modId,
    "mod_name" to modName,
    "mod_version" to modVersion,
    "mc_version" to minecraftVersion
  )
  inputs.properties(replacements)

  filesMatching(listOf("**/*.info", "**/*.properties")) {
    expand(replacements)
  }
}

tasks.jar.configure {
  val builtOn = "$minecraftVersion-$forgeVersion"
  manifest {
    attributes(
      "Built-By" to System.getProperty("user.name"),
      "Created-By" to "${System.getProperty("java.vm.version")} (${System.getProperty("java.vm.vendor")})",
      "Implementation-Title" to modName,
      "Implementation-Version" to project.version,
      "Built-On" to builtOn
    )
  }
}

// Dependencies
dependencies {
  // Thaumcraft and Baubles (already deobfuscated) from libs/
  implementation(files("libs/Thaumcraft-deobf-1.7.10-4.2.3.5.jar", "libs/Baubles-deobf-1.7.10-1.0.1.10.jar"))
}

// Code formatting, run ./gradlew spotlessApply to format
spotless {
  java {
    target("src/*/java/**/*.java")
    palantirJavaFormat("2.101.0")
  }
}

// IDE Settings
idea {
  module {
    isDownloadJavadoc = true
    isDownloadSources = true
    inheritOutputDirs = true // Fix resources in IJ-Native runs
  }
  project {
    this.withGroovyBuilder {
      "settings" {
        "runConfigurations" {
          val self = this.delegate as RunConfigurationContainer
          self.add(Gradle("1. Run Client").apply {
            setProperty("taskNames", listOf("runClient"))
          })
          self.add(Gradle("2. Run Server").apply {
            setProperty("taskNames", listOf("runServer"))
          })
          self.add(Gradle("3. Run Obfuscated Client").apply {
            setProperty("taskNames", listOf("runObfClient"))
          })
          self.add(Gradle("4. Run Obfuscated Server").apply {
            setProperty("taskNames", listOf("runObfServer"))
          })
        }
        "compiler" {
          val self = this.delegate as org.jetbrains.gradle.ext.IdeaCompilerConfiguration
          afterEvaluate {
            self.javac.moduleJavacAdditionalOptions = mapOf(
              (project.name + ".main") to
                tasks.compileJava.get().options.compilerArgs.map { '"' + it + '"' }.joinToString(" ")
            )
          }
        }
      }
    }
  }
}

tasks.processIdeaSettings.configure {
  dependsOn(tasks.injectTags)
}
