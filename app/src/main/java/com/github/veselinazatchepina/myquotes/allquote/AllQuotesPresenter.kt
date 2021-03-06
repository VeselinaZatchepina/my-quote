package com.github.veselinazatchepina.myquotes.allquote

import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber


class AllQuotesPresenter(val quoteDataSource: QuoteDataSource,
                         val allQuotesView: AllQuotesContract.View) : AllQuotesContract.Presenter {

    private var compositeDisposable: CompositeDisposable

    init {
        allQuotesView.setPresenter(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun getAllQuotes() {
        compositeDisposable.add(quoteDataSource.getAllQuotes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<Quote>>() {
                    override fun onNext(list: List<Quote>?) {
                        if (list != null) {
                            allQuotesView.showQuotes(list)
                        }
                    }

                    override fun onComplete() {

                    }

                    override fun onError(t: Throwable?) {

                    }

                }))
    }

    override fun getQuotesByType(quoteType: String) {
        compositeDisposable.add(quoteDataSource.getQuotesByType(quoteType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<Quote>>() {
                    override fun onNext(list: List<Quote>?) {
                        if (list != null) {
                            allQuotesView.showQuotes(list)
                        }
                    }

                    override fun onComplete() {

                    }

                    override fun onError(t: Throwable?) {

                    }

                }))
    }

    override fun getQuotesByTypeAndCategory(quoteType: String, quoteCategory: String) {
        compositeDisposable.add(quoteDataSource.getQuotesByTypeAndCategory(quoteType, quoteCategory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<Quote>>() {
                    override fun onNext(list: List<Quote>?) {
                        if (list != null) {
                            allQuotesView.showQuotes(list)
                        }
                    }

                    override fun onComplete() {

                    }

                    override fun onError(t: Throwable?) {

                    }

                }))
    }

    override fun getQuotesByTextIfContains(quoteText: String) {
        compositeDisposable.add(quoteDataSource.getQuotesByQuoteTextIfContains(quoteText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<Quote>>() {
                    override fun onNext(list: List<Quote>?) {
                        if (list != null) {
                            allQuotesView.showQuotesFromSearchView(list)
                        }
                    }

                    override fun onComplete() {

                    }

                    override fun onError(t: Throwable?) {

                    }

                }))
    }

    override fun getQuotesByTypeAndTextIfContains(quoteType: String, text: String) {
        compositeDisposable.add(quoteDataSource.getQuotesByTypeAndTextIfContains(quoteType, text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<Quote>>() {
                    override fun onNext(list: List<Quote>?) {
                        if (list != null) {
                            allQuotesView.showQuotesFromSearchView(list)
                        }
                    }

                    override fun onComplete() {

                    }

                    override fun onError(t: Throwable?) {

                    }

                }))
    }

    override fun getQuotesByTypeAndCategoryAndTextIfContains(quoteType: String, quoteCategory: String, text: String) {
        compositeDisposable.add(quoteDataSource.getQuotesByTypeAndCategoryAndTextIfContains(quoteType, quoteCategory, text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<Quote>>() {
                    override fun onNext(list: List<Quote>?) {
                        if (list != null) {
                            allQuotesView.showQuotesFromSearchView(list)
                        }
                    }

                    override fun onComplete() {

                    }

                    override fun onError(t: Throwable?) {

                    }

                }))
    }

    override fun deleteQuote(quoteId: Long) {
        compositeDisposable.add(Flowable.fromCallable {
            quoteDataSource.deleteQuote(quoteId)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                }, {

                }, {

                }))
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}