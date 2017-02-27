package com.developer.cookie.myquote.quote.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.quote.fragments.CurrentQuoteFragment;

import java.util.ArrayList;

public class CurrentQuotePagerActivity extends AppCompatActivity {
    public static final String QUOTE_ID_LIST = "com.developer.cookie.myquote.quote_list";
    public static final String CURRENT_ID = "com.developer.cookie.myquote.current_position";
    private ViewPager viewPager;
    ArrayList<Long> quoteIdList;
    long currentQuoteTextId;
    long quoteTextIdForIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_pager);
        quoteIdList = (ArrayList<Long>) getIntent().getSerializableExtra(QUOTE_ID_LIST);
        currentQuoteTextId = (long) getIntent().getSerializableExtra(CURRENT_ID);

        viewPager = (ViewPager) findViewById(R.id.quote_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                quoteTextIdForIntent = quoteIdList.get(position);
                return CurrentQuoteFragment.newInstance(quoteIdList.get(position));
            }
            @Override
            public int getCount() {
                return quoteIdList.size();
            }
        });

        // set viewPager on position of current clicked quote
        for (int i = 0; i <quoteIdList.size(); i++) {
            if (quoteIdList.get(i) == currentQuoteTextId) {
                viewPager.setCurrentItem(i);
            }
        }

        FloatingActionButton editFab = (FloatingActionButton) findViewById(R.id.edit_fab);
        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddQuoteActivity.newIntent(CurrentQuotePagerActivity.this, quoteTextIdForIntent);
                startActivity(intent);
            }
        });
    }

    public static Intent newIntent(Context context, ArrayList<Long> quoteList, long currentId) {
        Intent intent = new Intent(context, CurrentQuotePagerActivity.class);
        intent.putExtra(QUOTE_ID_LIST, quoteList);
        intent.putExtra(CURRENT_ID, currentId);
        return intent;
    }
}
