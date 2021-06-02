package com.ajisaka.corak.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ajisaka.corak.model.entities.FavBatik
import kotlinx.coroutines.flow.Flow

@Dao
interface FavBatikDao {

    @Insert
    suspend fun insertFavBatikDetails(favBatik: FavBatik)

    @Query("SELECT * FROM FAV_BATIK_TABLE ORDER BY ID")
    fun getAllBatikList(): Flow<List<FavBatik>>

    @Delete
    suspend fun deleteFavDishDetails(favBatik: FavBatik)
}