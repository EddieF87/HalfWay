package xyz.eddief.halfway.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import xyz.eddief.halfway.BuildConfig
import xyz.eddief.halfway.data.dao.LocationDao
import xyz.eddief.halfway.data.dao.UserDao
import xyz.eddief.halfway.data.models.LocationObject
import xyz.eddief.halfway.data.models.User

@Database(entities = [User::class, LocationObject::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun locationDao(): LocationDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .also {
                    if (BuildConfig.DEBUG) {
                        it.fallbackToDestructiveMigration()
                    }
                }
                .build()

        private const val DATABASE_NAME = "halfway-db"
    }
}