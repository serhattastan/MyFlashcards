package com.cloffygames.myflashcards.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cloffygames.myflashcards.data.local.CardDao
import com.cloffygames.myflashcards.data.local.CardGroupDao
import com.cloffygames.myflashcards.data.model.Card
import com.cloffygames.myflashcards.data.model.CardGroup

// Room veritabanı tanımlaması, CardGroup ve Card entity'lerini içerir
@Database(entities = [CardGroup::class, Card::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    // DAO'ları tanımlar
    abstract fun cardGroupDao(): CardGroupDao
    abstract fun cardDao(): CardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Singleton deseni kullanarak veritabanı örneğini alır
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                // Eğer INSTANCE null ise yeni bir veritabanı oluşturur
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "flashcards_database" // Veritabanı adı
                )
                    .fallbackToDestructiveMigration() // Yıkıcı geçişlere izin verir
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}