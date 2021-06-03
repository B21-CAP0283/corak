package com.ajisaka.corak.model.database

import androidx.room.*
import com.ajisaka.corak.model.entities.FavBatik
import kotlinx.coroutines.flow.Flow

@Dao
interface FavBatikDao {

    @Insert
    suspend fun insertFavBatikDetails(favBatik: FavBatik)
    @Update
    suspend fun updateFavBatikDetails(favBatik: FavBatik)
    @Query("SELECT * FROM FAV_BATIK_TABLE ORDER BY ID")
    fun getAllBatikList(): Flow<List<FavBatik>>

    @Query("SELECT * FROM FAV_BATIK_TABLE WHERE favorite_batik = 1")
    fun getFavoriteBatikList(): Flow<List<FavBatik>>

    @Delete
    suspend fun deleteFavDishDetails(favBatik: FavBatik)
}