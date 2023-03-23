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
        javaHome = "/Users/anmin83/Library/Java/JavaVirtualMachines/corretto-17.0.6/Contents/Home"
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "NCMusicDesktop"
            packageVersion = "1.0.0"
//            includeAllModules = true
            modules("java.instrument", "java.sql", "jdk.unsupported")

            macOS {
                // a version for all macOS distributables
                packageVersion = "1.0.0"
                // a version only for the dmg package
                dmgPackageVersion = "1.0.0"
                // a version only for the pkg package
                pkgPackageVersion = "1.0.0"
                // 显示在菜单栏、“关于”菜单项、停靠栏等中的应用程序名称
                dockName = "NCMusicDesktop"

                // a build version for all macOS distributables
                packageBuildVersion = "1.0.0"
                // a build version only for the dmg package
                dmgPackageBuildVersion = "1.0.0"
                // a build version only for the pkg package
                pkgPackageBuildVersion = "1.0.0"
                // 设置图标
                iconFile.set(project.file("launcher/icon.icns"))
            }

            windows {
                // a version for all Windows distributables
                packageVersion = "1.0.0"
                // a version only for the msi package
                msiPackageVersion = "1.0.0"
                // a version only for the exe package
                exePackageVersion = "1.0.0"
                // 设置图标
                iconFile.set(project.file("launcher/icon.ico"))
            }
        }

        buildTypes.release.proguard {
            obfuscate.set(false)
            configurationFiles.from(project.file("proguard-rules.pro"))
        }
    }
}