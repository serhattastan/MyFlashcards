package com.cloffygames.myflashcards.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.cloffygames.myflashcards.data.local.CardDao
import com.cloffygames.myflashcards.data.local.CardGroupDao
import com.cloffygames.myflashcards.data.model.Card
import com.cloffygames.myflashcards.data.model.CardGroup

@Database(entities = [CardGroup::class, Card::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardGroupDao(): CardGroupDao
    abstract fun cardDao(): CardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "flashcards_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}