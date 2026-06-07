package com.example.gestionrendez_vous.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gestionrendez_vous.data.models.Appointment
import com.example.gestionrendez_vous.data.models.Client
import com.example.gestionrendez_vous.data.models.Task
import com.example.gestionrendez_vous.data.models.User

@Database(entities = [User::class, Client::class, Appointment::class, Task::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rendez_vous_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
