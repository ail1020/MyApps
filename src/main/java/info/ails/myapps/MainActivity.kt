package info.ails.myapps

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var appList: MutableList<AppInfo>
    private lateinit var filteredAppList: MutableList<AppInfo>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AppAdapter
    private var hideSystem = true
    private var isAppNameAscending = true
    private var isAppDateAscending = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch all installed apps once when the activity is created
        appList = getInstalledApps()

        // Filter apps based on the hideSystem flag
        filteredAppList = filterApps(appList)

        // Update the ActionBar title with the number of apps
        updateActionBarTitle(filteredAppList.size)

        adapter = AppAdapter(filteredAppList)
        recyclerView.adapter = adapter
    }

    private fun getInstalledApps(): MutableList<AppInfo> {
        val pm = packageManager
        val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        val appInfoList = mutableListOf<AppInfo>()

        for (app in apps) {
            try {
                val pacInfo = pm.getPackageInfo(app.packageName, 0)
                val lastUpdateTime = pacInfo.lastUpdateTime
                val firstInstallTime = pacInfo.firstInstallTime
                val installerPackage = pm.getInstallSourceInfo(app.packageName).installingPackageName
                    ?: "Unknown"
                val installer = getInstallerLabel(pm, installerPackage)

                appInfoList.add(
                    AppInfo(
                        app.loadLabel(pm).toString(),
                        lastUpdateTime,
                        firstInstallTime,
                        installer,
                        pacInfo.applicationInfo?.flags ?: 0
                    )
                )
            } catch (e: PackageManager.NameNotFoundException) {
                // Handle the case where package info is not found
            }
        }

        appInfoList.sortBy { it.name }
        return appInfoList
    }

    private fun getInstallerLabel(pm: PackageManager, packageName: String): String {
        return if (packageName != "Unknown") {
            val installerAppInfo = pm.getApplicationInfo(packageName, 0)
            installerAppInfo.loadLabel(pm).toString()
        } else {
            "Unknown"
        }
    }

    private fun filterApps(appList: MutableList<AppInfo>): MutableList<AppInfo> {
        if (hideSystem) {
            return appList.filter { it.flags and ApplicationInfo.FLAG_SYSTEM == 0 }.toMutableList()
        } else {
            return appList.toMutableList()
        }
    }

    private fun updateActionBarTitle(appCount: Int) {
        supportActionBar?.title = "MyApps ($appCount)"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_by_name -> {
                sortAppsByName()
                return true
            }
            R.id.sort_by_date -> {
                sortAppsByDate()
                return true
            }
            R.id.toggle_system -> {
                hideSystem = !hideSystem
                filteredAppList = filterApps(appList)
                adapter.updateData(filteredAppList)
                updateActionBarTitle(filteredAppList.size)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sortAppsByName() {
        if (isAppNameAscending) {
            appList.sortBy { it.name }
        } else {
            appList.sortByDescending { it.name }
        }
        isAppNameAscending = !isAppNameAscending
        adapter.updateData(filterApps(appList))
    }

    private fun sortAppsByDate() {
        if (isAppDateAscending) {
            appList.sortBy { it.lastUpdate }
        } else {
            appList.sortByDescending { it.lastUpdate }
        }
        isAppDateAscending = !isAppDateAscending
        adapter.updateData(filterApps(appList))
    }
}

