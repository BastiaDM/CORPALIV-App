package com.example.corpalivapp.data.security

import android.content.Context
import android.content.SharedPreferences


object SessionManager {

    private const val PREFS = "corpaliv_session"
    private const val KEY_TOKEN = "jwt_token"

    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.applicationContext
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        }
    }

    fun token(): String? = prefs?.getString(KEY_TOKEN, null)

    fun guardarToken(token: String) {
        prefs?.edit()?.putString(KEY_TOKEN, token)?.apply()
    }

    fun cerrarSesion() {
        prefs?.edit()?.remove(KEY_TOKEN)?.apply()
    }

    fun haySesion(): Boolean = !token().isNullOrBlank()
}
