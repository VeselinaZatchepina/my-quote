package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.github.veselinazatchepina.myquotes.data.local.entity.BookCategory
import io.reactivex.Flowable


@Dao
interface BookCategoryDao {

    @Insert
    fun insertBookCategory(bookCategory: BookCategory)

    @Query("SELECT * FROM BookCategory")
    fun getAllBookCategories() : Flowable<List<BookCategory>>
}