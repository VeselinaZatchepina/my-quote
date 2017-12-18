package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey


@Entity(foreignKeys = arrayOf(
        ForeignKey(
                entity = Book::class,
                parentColumns = arrayOf("bookId"),
                childColumns = arrayOf("bookIdJoin")),
        ForeignKey(
                entity = BookAuthor::class,
                parentColumns = arrayOf("authorId"),
                childColumns = arrayOf("authorIdJoin"))
))
data class BookAndBookAuthor(@PrimaryKey(autoGenerate = true) val bookAndBookAuthorId: Long,
                             val bookIdJoin: Long,
                             val authorIdJoin: Long)