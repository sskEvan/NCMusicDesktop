package util

import java.util.*

object EnvUtil {
    val osName = System.getProperty("os.name", "generic")

    fun isMac() = osName.lowercase(Locale.getDefault()).contains("mac")

    fun isWindows() = osName.contains("indows")

    fun isLinux() = osName.contains("nix") || osName.contains("nux") || osName.contains("aix")
}