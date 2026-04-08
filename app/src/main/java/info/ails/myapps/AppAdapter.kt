package info.ails.myapps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat

// RecyclerView Adapter to display the list of apps
class AppAdapter(private val apps: MutableList<AppInfo>) : RecyclerView.Adapter<AppAdapter.AppViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = apps[position]
        holder.bind(app)
    }

    fun updateData(newData: MutableList<AppInfo>) {
        apps.clear()
        apps.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = apps.size

    // ViewHolder for individual app items
    class AppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val appNameTextView: TextView = view.findViewById(R.id.appNameTextView)
        private val lastUpdateTextView: TextView = view.findViewById(R.id.lastUpdateTextView)
        private val firstInstallTimeTextView: TextView = view.findViewById(R.id.firstInstallTimeTextView)
        private val installSourceTextView: TextView = view.findViewById(R.id.InstallSourceTextView)

        fun bind(app: AppInfo) {
            appNameTextView.text = app.name

            // Set the install source
            installSourceTextView.text = "From: ${app.installer}"

            // Handle last update and install time display
            val installDate = DateFormat.getDateInstance().format(app.firstInstallTime)
            val lastUpdateDate = DateFormat.getDateInstance().format(app.lastUpdate)

            if (installDate == lastUpdateDate) {
                // If install and last update dates are the same, show only one
                firstInstallTimeTextView.text = "Installed"
                lastUpdateTextView.text = "$lastUpdateDate"
            } else {
                // Display both dates
                firstInstallTimeTextView.text = ""
                lastUpdateTextView.text = "Updated: $lastUpdateDate"
            }
        }
    }
}
