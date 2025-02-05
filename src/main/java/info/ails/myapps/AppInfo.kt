package info.ails.myapps

data class AppInfo(
    val name: String,        // App name
    val lastUpdate: Long,     // Last updated date (in milliseconds)
    val firstInstallTime: Long, // First install date (in milliseconds)
    val installer: String,   // Package name of the installer
    val flags: Int = 0
)
