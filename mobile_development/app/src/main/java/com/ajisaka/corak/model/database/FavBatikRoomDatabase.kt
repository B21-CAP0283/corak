package com.ajisaka.corak.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ajisaka.corak.model.entities.FavBatik

@Database(entities = [FavBatik::class], version = 1)
abstract class FavBatikRoomDatabase : RoomDatabase() {

    abstract fun fabBatikDao(): FavBatikDao

    companion object{
        @Volatile
        private var INSTANCE: FavBatikRoomDatabase? = null

        fun getDatabase(context: Context): FavBatikRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavBatikRoomDatabase::class.java,
                    "fav_batik_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}