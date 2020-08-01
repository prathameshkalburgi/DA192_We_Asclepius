package `in`.asclepius.app.others

import android.content.Context

class SharedPrefsManager(context: Context) {
    val sharedPreferences =
        context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE)

    public fun setString(value: String, key: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    public fun getString(defaultValue: String, key: String): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

}