package com.ajisaka.corak.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "fav_batik_table")
data class FavBatik (
        @ColumnInfo val image: String,
        @ColumnInfo(name = "image_source") val imasgeSource : String,
        @ColumnInfo val confidence: String,
        @ColumnInfo val audio: String,
        @ColumnInfo val name: String,
        @ColumnInfo val origin: String,
        @ColumnInfo val characteristic: String,
        @ColumnInfo val philosophy: String,
        @ColumnInfo(name = "favorite_batik") var favoriteBatik : Boolean = false,

        @PrimaryKey(autoGenerate = true) val id: Int = 0

): Parcelable