package com.github.veselinazatchepina.myquotes.bookcategories

import com.github.veselinazatchepina.myquotes.BasePresenter
import com.github.veselinazatchepina.myquotes.BaseView
import com.github.veselinazatchepina.myquotes.data.local.entity.QuoteCategory
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote


interface BookCategoriesContract {

    interface View : BaseView<Presenter> {
        fun showBookCategoriesList(bookCategories: List<QuoteCategory>)


    }

    interface Presenter : BasePresenter {
        fun getBookCategoriesList()

        fun getQuotesByCategory(categoryName: String): List<Quote>


    }
}