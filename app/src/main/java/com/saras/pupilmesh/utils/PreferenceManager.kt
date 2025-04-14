package com.saras.pupilmesh.utils

import android.content.Context
import androidx.core.content.edit

object PreferenceManager {
    fun isUserSignedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        return prefs.getBoolean("signed_in", false)
    }

    fun setUserSignedIn(context: Context, signedIn: Boolean) {
        val prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        prefs.edit { putBoolean("signed_in", signedIn) }
    }
}