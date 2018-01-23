package com.github.veselinazatchepina.myquotes.currentquote

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.ShareActionProvider
import android.view.*
import com.github.veselinazatchepina.myquotes.R
import com.github.veselinazatchepina.myquotes.data.local.entity.BookAuthor
import com.github.veselinazatchepina.myquotes.data.local.entity.BookReleaseYear
import com.github.veselinazatchepina.myquotes.data.local.pojo.AllQuoteData
import kotlinx.android.synthetic.main.fragment_current_quote.view.*
import java.io.Serializable


class CurrentQuoteFragment : Fragment(), CurrentQuoteContract.View {

    private var currentQuotePresenter: CurrentQuoteContract.Presenter? = null
    lateinit var quote: AllQuoteData
    lateinit var rootView: View
    private var shareIntent: Intent? = null

    companion object {
        private const val ALL_QUOTE_DATA_BUNDLE = "all_quote_data_bundle"

        fun createInstance(quote: AllQuoteData): CurrentQuoteFragment {
            val bundle = Bundle()
            bundle.putSerializable(ALL_QUOTE_DATA_BUNDLE, quote as Serializable)
            val fragment = CurrentQuoteFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        quote = arguments?.getSerializable(ALL_QUOTE_DATA_BUNDLE) as AllQuoteData
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_current_quote, container, false)
        currentQuotePresenter?.getBookAuthors(quote.authorsId?.map {
            it.authorIdJoin
        } ?: emptyList())
        currentQuotePresenter?.getBookReleaseYear(quote.yearsId?.map {
            it.yearIdJoin
        } ?: emptyList())
        showQuote(quote)
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_current_quote, menu)
        setShareAction(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setShareAction(menu: Menu?) {
        val shareItem = menu?.findItem(R.id.menu_item_share)
        val shareActionProvider = MenuItemCompat.getActionProvider(shareItem) as ShareActionProvider?
        shareIntent = Intent(Intent.ACTION_SEND).apply {
            this.type = "text/plain"
            this.putExtra(android.content.Intent.EXTRA_SUBJECT, "It is great quote! Listen!")
            this.putExtra(android.content.Intent.EXTRA_TEXT, quote.quote?.quoteText)
        }
        shareActionProvider?.setShareIntent(shareIntent)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_item_share -> {
                startActivity(Intent.createChooser(shareIntent, "Select conversation"))
            }
            R.id.menu_item_delete_quote -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setPresenter(presenter: CurrentQuoteContract.Presenter) {
        currentQuotePresenter = presenter
    }

    override fun showBookAuthors(authors: List<BookAuthor>) {
        rootView.current_author_name.text = getAllAuthorString(authors)
    }

    override fun showBookReleaseYears(years: List<BookReleaseYear>) {
        rootView.current_year_number.text = isEmptyString(getAllYearString(years))
    }

    private fun showQuote(allQuoteData: AllQuoteData) {
        rootView.current_quote_text.text = getString(R.string.quote_text_format, allQuoteData.quote?.quoteText)
        rootView.currentCategory.text = allQuoteData.category?.first()?.categoryName

        rootView.current_book_name.text = isDataEmpty(allQuoteData.book!!.bookName, allQuoteData.book!!.bookId, "NoBookName")
        rootView.current_publisher_name.text = isDataEmpty(allQuoteData.publishingOffice!!.first().officeName,
                allQuoteData.publishingOffice!!.first().officeId,
                "NoPublishingOfficeName")

        rootView.current_page_number.text = isEmptyString(allQuoteData.quote?.pageNumber?.toString() ?: "")
        rootView.quote_creation_date.text = isEmptyString(allQuoteData.quote?.creationDate ?: "")
        rootView.comments.text = isEmptyString(allQuoteData.quote?.comments ?: "")
    }

    private fun isEmptyString(currentValue: String): String = if (currentValue == "") {
        "-"
    } else {
        currentValue
    }

    private fun isDataEmpty(currentValue: String, id: Long, pattern: String): String {
        return if (currentValue == pattern + id) {
            "-"
        } else {
            currentValue
        }
    }


    private fun getAllAuthorString(authors: List<BookAuthor>): String {
        var author = ""
        for (i in 0 until authors.size) {
            if (isDataEmpty(authors[i].name, authors[i].authorId, "NoAuthorName") == "-") {
                continue
            }
            author = author.plus("${authors[i].name} " + "${authors[i].surname} " + "${authors[i].patronymic} ")
            if (i != authors.size - 1) {
                author = author.plus(",\n")
            }
        }
        return if (author == "") {
            "-"
        } else {
            author
        }
    }

    private fun getAllYearString(years: List<BookReleaseYear>): String {
        var year = ""
        for (i in 0 until years.size) {
            year = year.plus("${years[i].year}")
            if (i != years.size - 1) {
                year = year.plus(",\n")
            }
        }
        return year
    }
}