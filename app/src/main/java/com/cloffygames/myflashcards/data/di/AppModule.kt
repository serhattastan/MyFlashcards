package com.cloffygames.myflashcards.data.di

import android.content.Context
import com.cloffygames.myflashcards.data.AppDatabase
import com.cloffygames.myflashcards.data.local.CardDao
import com.cloffygames.myflashcards.data.local.CardGroupDao
import com.cloffygames.myflashcards.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirestoreRepository(): FirestoreRepository {
        return FirestoreRepository()
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(@ApplicationContext context: Context): PreferencesRepository {
        return PreferencesRepository(context)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getDatabase(appContext)
    }

    @Provides
    fun provideCardGroupDao(db: AppDatabase): CardGroupDao {
        return db.cardGroupDao()
    }

    @Provides
    fun provideCardDao(db: AppDatabase): CardDao {
        return db.cardDao()
    }

    @Provides
    fun provideCardGroupRepository(cardGroupDao: CardGroupDao): CardGroupRepository {
        return CardGroupRepository(cardGroupDao)
    }

    @Provides
    fun provideCardRepository(cardDao: CardDao): CardRepository {
        return CardRepository(cardDao)
    }
}
