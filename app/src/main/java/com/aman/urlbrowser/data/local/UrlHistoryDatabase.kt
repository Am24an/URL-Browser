package com.aman.urlbrowser.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UrlHistoryEntity::class], version = 1, exportSchema = false)
abstract class UrlHistoryDatabase : RoomDatabase() {
    abstract fun urlHistoryDao(): UrlHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: UrlHistoryDatabase? = null

        fun getDatabase(context: Context): UrlHistoryDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UrlHistoryDatabase::class.java,
                    "url_history_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }
}