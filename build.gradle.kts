import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.ssk.NCMusicDesktop"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("com.google.zxing:javase:3.3.3")
                implementation("moe.tlaster:precompose:1.3.14")
                implementation("moe.tlaster:precompose-molecule:1.3.14")
                implementation("androidx.datastore:datastore-preferences-core:1.1.0-dev01")

                implementation("com.squareup.retrofit2:retrofit:2.9.0")
                implementation("com.squareup.retrofit2:converter-gson:2.9.0")

                implementation("androidx.datastore:datastore-preferences-core:1.1.0-dev01")
                implementation("io.github.succlz123:compose-imageloader-desktop:0.0.2")
                implementation("uk.co.caprica:vlcj:4.7.3")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "NCMusicDesktop"
            packageVersion = "1.0.0"
        }
    }
}