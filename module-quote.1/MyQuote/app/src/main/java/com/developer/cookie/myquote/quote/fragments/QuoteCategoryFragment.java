package com.developer.cookie.myquote.quote.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.database.QuoteDataRepository;
import com.developer.cookie.myquote.database.model.QuoteCategory;
import com.developer.cookie.myquote.quote.activities.AllQuoteCurrentCategoryActivity;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * QuoteCategoryFragment is used for presentation list of all categories (quote category) and
 * count of the quotes for every this category.
 */
public class QuoteCategoryFragment extends Fragment {

    private static final String LOG_TAG = QuoteCategoryFragment.class.getSimpleName();
    public static final String QUOTE_TYPE_BUNDLE_QCF = "com.developer.cookie.myquote.quote_type_bundle_qcf";
    View mRootView;
    QuoteDataRepository mQuoteDataRepository;

    List<Integer> mQuoteCountListOfEveryCategory;
    List<String> mListOfCategories;
    Pair<List<String>, List<Integer>> mPair;

    QuoteCategoryRecyclerViewAdapter mQuoteCategoryRecyclerViewAdapter;
    RealmResults<QuoteCategory> mQuoteCategoryList;

    String mQuoteType;

    String mCategoryForDelete;

    public QuoteCategoryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mQuoteType = savedInstanceState.getString(QUOTE_TYPE_BUNDLE_QCF);
        } else {
            mQuoteType = getActivity().getTitle().toString();
        }
        mQuoteDataRepository = new QuoteDataRepository();
        mQuoteCategoryList = mQuoteDataRepository.getListOfQuoteCategories(mQuoteType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        mQuoteCategoryList.addChangeListener(new RealmChangeListener<RealmResults<QuoteCategory>>() {
            @Override
            public void onChange(RealmResults<QuoteCategory> element) {
                    createPairObject(element);
                    mQuoteCategoryRecyclerViewAdapter = new QuoteCategoryRecyclerViewAdapter(mPair);
                    // Create and set custom adapter for recyclerview
                    RecyclerView recyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
                    registerForContextMenu(recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(mQuoteCategoryRecyclerViewAdapter);
            }
        });
        return mRootView;
    }

    /**
     * Method create Pair object for custom adapter. First parameter is name list of category,
     * second is quote count list of this categories.
     *
     * @param element list of quote category realm object.
     */
    private void createPairObject(List<QuoteCategory> element) {
        // Create parameters for mPair object
        mQuoteCountListOfEveryCategory = new ArrayList<Integer>();
        for (int i = 0; i < element.size(); i++) {
            mQuoteCountListOfEveryCategory.add(element.get(i).getQuoteCountCurrentCategory());
        }
        mListOfCategories = new ArrayList<>();
        for (int i = 0; i < element.size(); i++) {
            String currentCategory = element.get(i).getCategory();
            mListOfCategories.add(currentCategory);
        }
        mPair = new Pair<>(mListOfCategories, mQuoteCountListOfEveryCategory);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.recycler_view) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.context_menu_current_quote, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                mQuoteDataRepository.deleteAllQuotesWithCurrentCategory(mCategoryForDelete, mQuoteType);
                final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);
                Snackbar snackbarIsDeleted = Snackbar.make(coordinatorLayout, "Quote category " + mCategoryForDelete + " is deleted", Snackbar.LENGTH_LONG);
                snackbarIsDeleted.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(QUOTE_TYPE_BUNDLE_QCF, mQuoteType);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mQuoteDataRepository.closeDbConnect();
    }

    /**
     * This class is custom adapter for QuoteCategoryFragment.
     * It helps to show list of all quote categories and how quote counts belongs to every this category.
     */
    private class QuoteCategoryRecyclerViewAdapter
            extends RecyclerView.Adapter<QuoteCategoryRecyclerViewAdapter.MyViewHolder> {
        private static final int EMPTY_LIST = 0;
        private static final int NOT_EMPTY_LIST = 1;

        private List<String> listOfCategory;
        private List<Integer> quoteCountList;

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            TextView itemQuoteCategory;
            TextView itemQuoteCount;

            MyViewHolder(View container) {
                super(container);
                itemQuoteCategory = (TextView) container.findViewById(R.id.quote_category_name);
                itemQuoteCount = (TextView) container.findViewById(R.id.quote_count);
                container.setOnClickListener(this);
                container.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {
                startActivity(AllQuoteCurrentCategoryActivity.newIntent(getActivity(),
                        itemQuoteCategory.getText().toString(), mQuoteType));
            }

            @Override
            public boolean onLongClick(View view) {
                mCategoryForDelete = itemQuoteCategory.getText().toString();
                getActivity().openContextMenu(view);
                return false;
            }
        }

        /**
         * Method create custom adapter.
         *
         * @param pair object with two field List<String> and List<Integer>. First field is for list of category.
         *             Second field is for list of quote count.
         */
        public QuoteCategoryRecyclerViewAdapter(Pair<List<String>, List<Integer>> pair) {
            listOfCategory = pair.first;
            quoteCountList = pair.second;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case EMPTY_LIST:
                    View itemViewEmpty = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.fragment_empty_recycler_view, parent, false);
                    return new MyViewHolder(itemViewEmpty);
                case NOT_EMPTY_LIST:
                    View itemView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.quote_categories_recycler_view_item, parent, false);
                    return new MyViewHolder(itemView);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if (listOfCategory.size() != 0) {
                holder.itemQuoteCategory.setText(listOfCategory.get(position));
                holder.itemQuoteCount.setText(String.valueOf(quoteCountList.get(position)));
            }
        }

        @Override
        public int getItemCount() {
            if (listOfCategory.size() == 0) {
                return 1;
            }
            return listOfCategory.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (listOfCategory.size() == 0) {
                return EMPTY_LIST;
            } else {
                return NOT_EMPTY_LIST;
            }
        }

        /**
         * When adapter data is changed this method helps set new data for adapter.
         *
         * @param pair
         */
        public void changeDate(Pair<List<String>, List<Integer>> pair) {
            listOfCategory = pair.first;
            quoteCountList = pair.second;
            notifyDataSetChanged();
        }
    }
}