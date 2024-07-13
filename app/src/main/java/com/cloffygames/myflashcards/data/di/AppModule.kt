package com.cloffygames.myflashcards.data.di

import android.content.Context
import com.cloffygames.myflashcards.data.repository.FirestoreRepository
import com.cloffygames.myflashcards.data.repository.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Dagger Hilt modülü olarak işaretlenmiş bir sınıf
@Module
@InstallIn(SingletonComponent::class) // Bu modülün SingletonComponent'e enjekte edileceğini belirtir
object AppModule {

    // FirestoreRepository sağlayıcı metodu
    @Provides
    @Singleton // Singleton anotasyonu, FirestoreRepository'nin uygulama boyunca tek bir örneğe sahip olmasını sağlar
    fun provideFirestoreRepository(): FirestoreRepository {
        return FirestoreRepository() // FirestoreRepository örneği döner
    }

    // PreferencesRepository sağlayıcı metodu
    @Provides
    @Singleton // Singleton anotasyonu, PreferencesRepository'nin uygulama boyunca tek bir örneğe sahip olmasını sağlar
    fun providePreferencesRepository(@ApplicationContext context: Context): PreferencesRepository {
        return PreferencesRepository(context) // PreferencesRepository örneği döner
    }
}