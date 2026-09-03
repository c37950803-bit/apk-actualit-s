package com.example.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.core.util.Constants
import com.example.data.local.dao.NewsDao
import com.example.data.local.entity.NewsEntity

/**
 * ============================================================================
 * COUCHE DATA / LOCAL : BASE DE DONNÉES ROOM
 * ============================================================================
 * Instance centrale SQLite avec pattern Singleton pour éviter les fuites
 * de mémoire et les blocages de concurrence.
 */
@Database(
    entities = [NewsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object {
        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getInstance(context: Context): NewsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
