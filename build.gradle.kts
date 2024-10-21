import xyz.jpenilla.resourcefactory.bukkit.Permission

plugins {
    id("java")
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.2.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "net.radstevee.readycheck"
version = "0.0.0"

val READY_CHECK_PERMISSION = "readycheck"
val RECOLLECT_PLAYERS_PERMISSION = "readycheck.recollect_players"

val pluginLibraries = mutableListOf<String>()

fun DependencyHandlerScope.library(notation: Any) {
    compileOnly(notation)
    pluginLibraries.add(notation.toString())
}

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.1-R0.1-SNAPSHOT")
    library("org.spongepowered:configurate-yaml:4.1.2")
}

tasks.runServer {
    minecraftVersion("1.21.1")
}

bukkitPluginYaml {
    this.main = "net.radstevee.readycheck.ReadyCheck"
    this.name = "readycheck"
    this.author = "radstevee"
    this.version = getVersion().toString()
    this.apiVersion = "1.21"

    this.libraries.addAll(pluginLibraries)

    commands {
        register("ready-check") {
            permission = READY_CHECK_PERMISSION
            description = "Executes a ready check."
            aliases.add("rc")
        }

        register("ready") {
            description = "Marks you as ready."
            aliases.add("r")
        }

        register("recollect-players") {
            description = "Recollects all players that should be participating in the ready check."
            permission = RECOLLECT_PLAYERS_PERMISSION
            aliases.add("rcp")
        }
    }

    permissions {
        register(READY_CHECK_PERMISSION) {
            description = "/ready-check"
            default = Permission.Default.OP
        }

        register(RECOLLECT_PLAYERS_PERMISSION) {
            description = "/recollect-players"
            default = Permission.Default.OP
        }
    }
}