package com.cloffygames.myflashcards.data.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// MyApp sınıfı, Hilt tarafından sağlanan bağımlılıkları kullanmak için HiltAndroidApp ile işaretlenmiştir
@HiltAndroidApp
class MyApp : Application() {
    // Bu sınıf, uygulamanın ana Application sınıfıdır
    // Hilt'in bağımlılık enjeksiyonunu gerçekleştirebilmesi için bu sınıf HiltAndroidApp ile işaretlenmelidir
}