package com.example.packitupandroid.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.packitupandroid.model.Item
import com.example.packitupandroid.ui.utils.DATABASE_NAME


/**
 * Database class with singleton Instance object.
 */

// version - increase version number whenever database table schema changes.
// exportSchema - set as false as to not keep schema version history backup
@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the Instance is not null, return it, otherwise create a new
            // database instance.
            // synchronized block means that only one thread of execution at
            // time can enter this block of code, which makes sure database only
            // gets initialized once.
            return Instance ?: synchronized(this) {
                Room
                    .databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    /**
                     * Setting option in app's database builder means that Room
                     * permanently deletes all data from database tables when it
                     * attempts to perform a migration with no defined migration path.
                     */
                    // required migration strategy
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
