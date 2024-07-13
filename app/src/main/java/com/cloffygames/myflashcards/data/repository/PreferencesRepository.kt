package com.cloffygames.myflashcards.data.repository

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

// PreferencesRepository sınıfı, singleton olarak tanımlanmış ve Dagger Hilt tarafından enjekte ediliyor
@Singleton
class PreferencesRepository @Inject constructor(context: Context) {

    // SharedPreferences örneği oluşturuluyor
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        // SharedPreferences dosyasının adı
        private const val PREFS_NAME = "com.cloffygames.myflashcards.PREFS"
        // Kullanıcının oturum açma durumunu saklamak için anahtar
        private const val PREFS_KEY_USER_LOGGED_IN = "user_logged_in"
    }

    // Kullanıcı oturumunu kaydetme fonksiyonu
    fun saveUserSession() {
        // Kullanıcı oturum açma durumunu true olarak kaydeder
        sharedPreferences.edit().putBoolean(PREFS_KEY_USER_LOGGED_IN, true).apply()
    }

    // Kullanıcının oturum açma durumunu kontrol etme fonksiyonu
    fun isUserLoggedIn(): Boolean {
        // Kullanıcının oturum açma durumunu döner, varsayılan olarak false
        return sharedPreferences.getBoolean(PREFS_KEY_USER_LOGGED_IN, false)
    }

    // Kullanıcı oturumunu temizleme fonksiyonu
    fun clearUserSession() {
        // Kullanıcı oturum açma durumunu sharedPreferences'tan kaldırır
        sharedPreferences.edit().remove(PREFS_KEY_USER_LOGGED_IN).apply()
    }
}