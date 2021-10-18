package com.restoo.machinetest

import androidx.room.*


@Dao
interface DataDao {
    @Query("SELECT * FROM productdata")
    fun getAll(): List<ProductData?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(nasanfo: ProductData?)

    @Delete
    fun delete(nasanfo: ProductData?)

    @Update
    fun update(nasanfo: ProductData?)
}
