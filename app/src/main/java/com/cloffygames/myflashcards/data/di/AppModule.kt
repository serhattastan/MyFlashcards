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

@Module // Bu anotasyon, bu sınıfın bir Dagger Hilt modülü olduğunu belirtir
@InstallIn(SingletonComponent::class) // Bu anotasyon, bu modülün SingletonComponent'e enjekte edileceğini belirtir
object AppModule {

    // FirestoreRepository bağımlılığını sağlar
    @Provides
    @Singleton
    fun provideFirestoreRepository(): FirestoreRepository {
        return FirestoreRepository()
    }

    // PreferencesRepository bağımlılığını sağlar ve context'i enjekte eder
    @Provides
    @Singleton
    fun providePreferencesRepository(@ApplicationContext context: Context): PreferencesRepository {
        return PreferencesRepository(context)
    }

    // AppDatabase bağımlılığını sağlar ve context'i enjekte eder
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getDatabase(appContext)
    }

    // CardGroupDao bağımlılığını sağlar ve AppDatabase'i enjekte eder
    @Provides
    fun provideCardGroupDao(db: AppDatabase): CardGroupDao {
        return db.cardGroupDao()
    }

    // CardDao bağımlılığını sağlar ve AppDatabase'i enjekte eder
    @Provides
    fun provideCardDao(db: AppDatabase): CardDao {
        return db.cardDao()
    }

    // CardGroupRepository bağımlılığını sağlar ve CardGroupDao'yu enjekte eder
    @Provides
    fun provideCardGroupRepository(cardGroupDao: CardGroupDao): CardGroupRepository {
        return CardGroupRepository(cardGroupDao)
    }

    // CardRepository bağımlılığını sağlar ve CardDao'yu enjekte eder
    @Provides
    fun provideCardRepository(cardDao: CardDao): CardRepository {
        return CardRepository(cardDao)
    }
}
