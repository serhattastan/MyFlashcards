package com.cloffygames.myflashcards.data.di

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.cloffygames.myflashcards.data.repository.PreferencesRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

// MyApp sınıfı, Hilt tarafından sağlanan bağımlılıkları kullanmak için HiltAndroidApp ile işaretlenmiştir
@HiltAndroidApp
class MyApp : Application() {
    // Bu sınıf, uygulamanın ana Application sınıfıdır
    // Hilt'in bağımlılık enjeksiyonunu gerçekleştirebilmesi için bu sınıf HiltAndroidApp ile işaretlenmelidir

    @Inject // Hilt'in PreferencesRepository bağımlılığını enjekte etmesini sağlar
    lateinit var preferencesRepository: PreferencesRepository

    override fun onCreate() {
        super.onCreate()
        // Uygulama başlatıldığında, kullanıcının tercihlerine göre karanlık modu uygular
        applyDarkMode(preferencesRepository.isDarkModeEnabled())
    }

    // Karanlık modu uygular
    private fun applyDarkMode(isDarkModeEnabled: Boolean) {
        val mode = if (isDarkModeEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
