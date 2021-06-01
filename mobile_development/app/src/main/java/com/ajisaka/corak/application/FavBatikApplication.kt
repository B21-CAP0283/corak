package com.ajisaka.corak.application

import android.app.Application
import com.ajisaka.corak.model.database.FavBatikRepository
import com.ajisaka.corak.model.database.FavBatikRoomDatabase

class FavBatikApplication : Application() {

    private val database by lazy { FavBatikRoomDatabase.getDatabase((this@FavBatikApplication)) }
    val repository by lazy { FavBatikRepository(database.fabBatikDao()) }
}