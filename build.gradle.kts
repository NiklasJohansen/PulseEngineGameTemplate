plugins {
    kotlin("jvm") version "2.2.0"
    id("edu.sc.seis.launch4j") version "3.0.6"
}

group = "org.example"
version = "1.0"

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.repsy.io/mvn/njoh/public")
    }
}

dependencies {
    implementation("no.njoh:pulse-engine:0.11.0")
}

kotlin {
    jvmToolchain(23)
}

////////////////////////////////////////////////////////////////////////////////// Windows release

val releaseName = "$name-$version"
val releaseBuildDir = "$buildDir/$releaseName"
val mainClass = "GameTemplateKt"

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    exclude("macos/**", "linux/**") // Exclude natives for Mac and Linux when creating Windows exe
    manifest { attributes("Main-Class" to mainClass) }
}

launch4j {
    bundledJrePath = "jre"
    mainClassName = mainClass
    outputDir = releaseBuildDir
}

tasks.register<Zip>("buildWin64Release") {
    group = "release"
    dependsOn("createExe")
    doFirst {
        delete("$releaseBuildDir/lib")
        copy {
            from(zipTree("jre/jre23-win64.zip"))
            into("$releaseBuildDir/jre")
        }
    }
    from(releaseBuildDir)
    destinationDirectory.set(file("release/win64"))
    archiveFileName.set("${releaseName}.zip")
}