package com.ajisaka.corak.model.database

import androidx.annotation.WorkerThread
import com.ajisaka.corak.model.entities.FavBatik
import kotlinx.coroutines.flow.Flow


class FavBatikRepository(private val favBatikDao: FavBatikDao) {
    @WorkerThread
    suspend fun insertFavBatikData(favBatik: FavBatik){
        favBatikDao.insertFavBatikDetails(favBatik)
    }

    val allBatikList: Flow<List<FavBatik>> = favBatikDao.getAllBatikList()
    @WorkerThread
    suspend fun updateFavBatikData(favBatik: FavBatik) {
        favBatikDao.updateFavBatikDetails(favBatik)
    }
    val favoriteBatik: Flow<List<FavBatik>> = favBatikDao.getFavoriteBatikList()
    @WorkerThread
    suspend fun deleteFavBatikData(favBatik: FavBatik) {
        favBatikDao.deleteFavDishDetails(favBatik)
    }
}