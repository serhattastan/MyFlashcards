package com.cloffygames.myflashcards.data.repository

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Bu anotasyon, bu sınıfın uygulama boyunca tek bir örneğinin kullanılmasını sağlar
class PreferencesRepository @Inject constructor(context: Context) {

    // SharedPreferences örneği, uygulama içindeki verileri kalıcı olarak saklamak için kullanılır
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "com.cloffygames.myflashcards.PREFS" // SharedPreferences dosya adı
        private const val PREFS_KEY_USER_LOGGED_IN = "user_logged_in" // Kullanıcının giriş yapıp yapmadığını tutan anahtar
        private const val PREFS_KEY_DARK_MODE = "dark_mode" // Karanlık mod tercihlerini tutan anahtar
    }

    // Kullanıcı oturumunu kaydeder
    fun saveUserSession() {
        sharedPreferences.edit().putBoolean(PREFS_KEY_USER_LOGGED_IN, true).apply()
    }

    // Kullanıcının giriş yapıp yapmadığını kontrol eder
    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(PREFS_KEY_USER_LOGGED_IN, false)
    }

    // Kullanıcı oturumunu siler
    fun clearUserSession() {
        sharedPreferences.edit().remove(PREFS_KEY_USER_LOGGED_IN).apply()
    }

    // Karanlık mod tercihlerini ayarlar
    fun setDarkMode(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(PREFS_KEY_DARK_MODE, enabled).apply()
    }

    // Karanlık modun etkin olup olmadığını kontrol eder
    fun isDarkModeEnabled(): Boolean {
        return sharedPreferences.getBoolean(PREFS_KEY_DARK_MODE, false)
    }
}
