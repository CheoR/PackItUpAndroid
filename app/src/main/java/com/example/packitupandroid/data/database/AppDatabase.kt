package com.example.packitupandroid.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.packitupandroid.data.database.dao.BoxDao
import com.example.packitupandroid.data.database.dao.CollectionDao
import com.example.packitupandroid.data.database.dao.ItemDao
import com.example.packitupandroid.data.database.dao.SummaryDao
import com.example.packitupandroid.data.database.entities.BoxEntity
import com.example.packitupandroid.data.database.entities.CollectionEntity
import com.example.packitupandroid.data.database.entities.ItemEntity
import com.example.packitupandroid.utils.Converters
import com.example.packitupandroid.utils.DATABASE_NAME


/**
 * Database class with singleton Instance object.
 *
 * @Database annotation:
 * - version: Increase version number whenever the database table schema changes.
 * - exportSchema: Set to false to avoid keeping schema version history backup.
 *
 * @TypeConverters annotation:
 * - Specifies the Converters class to handle custom data types.
 */
// version - increase version number whenever database table schema changes.
// exportSchema - set as false as to not keep schema version history backup
@Database(entities = [ItemEntity::class, BoxEntity::class, CollectionEntity::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to the ItemDao.
     *
     * @return The ItemDao instance.
     */
    abstract fun itemDao(): ItemDao

    /**
     * Provides access to the BoxDao.
     *
     * @return The BoxDao instance.
     */
    abstract fun boxDao(): BoxDao

    /**
     * Provides access to the CollectionDao.
     *
     * @return The CollectionDao instance.
     */
    abstract fun collectionDao(): CollectionDao

    /**
     * Provides access to the SummaryDao.
     *
     * @return The SummaryDao instance.
     */
    abstract fun summaryDao(): SummaryDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        /**
         * Returns the singleton instance of the AppDatabase.
         *
         * @param context The context used to create the database.
         * @return The singleton instance of the AppDatabase.
         */
        fun getDatabase(
            context: Context,
            useMockData: Boolean,
        ): AppDatabase {
            // If the Instance is not null, return it, otherwise create a new
            // database instance.
            // Synchronized block ensures that only one thread of execution at
            // a time can enter this block of code, which ensures the database
            // only gets initialized once.
            return Instance ?: synchronized(this) {
                val builder = Room
                    .databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    /**
                     * Setting this option in the app's database builder means that Room
                     * permanently deletes all data from database tables when it
                     * attempts to perform a migration with no defined migration path.
                     */
                    .fallbackToDestructiveMigration()

                if (useMockData) {
                    builder.createFromAsset("database/$DATABASE_NAME")
                }

                builder
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
