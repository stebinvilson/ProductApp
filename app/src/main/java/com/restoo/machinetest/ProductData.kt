package com.restoo.machinetest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ProductData")
class ProductData(@PrimaryKey(autoGenerate = true)
               var id: Int?,
               @ColumnInfo(name = "title") var title: String,
               @ColumnInfo(name = "description") var description: String,
               @ColumnInfo(name = "price") var price: String,
               @ColumnInfo(name = "imageurl") var imageurl: String,
               @ColumnInfo(name = "product") var product: String)
