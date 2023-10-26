package ru.pis.sdclient

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryImage::class], exportSchema = false, version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "images.db"
                    ).allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE!!
        }

    }
}