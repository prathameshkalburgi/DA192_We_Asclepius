package `in`.asclepius.app.others

import android.content.Context

class SharedPrefsManager(context: Context) {
    val sharedPreferences =
        context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE)

    public fun setString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    public fun getString(key: String, defaultValue: String): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

}