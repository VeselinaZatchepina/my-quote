package com.developer.cookie.myquote.quote.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.quote.fragments.AddQuoteFragment;

public class AddQuoteActivity extends SingleFragmentActivity {
    public static final String QUOTE_TEXT_ID = "quote_text_id";

    @Override
    public Fragment createFragment() {
        return new AddQuoteFragment();
    }

    @Override
    public int setFabImageResourceId() {
        return R.drawable.ic_done_white_24dp;
    }

    @Override
    public void toDoWhenFabIsPressed() {
        AddQuoteFragment addQuoteFragment =  ((AddQuoteFragment)currentFragment);
        if (!addQuoteFragment.isEditTextEmpty() && !addQuoteFragment.isSpinnerSelectedItemHint()) {
            addQuoteFragment.createMapOfQuoteProperties();
            this.finish();
        }
        if (addQuoteFragment.isSpinnerSelectedItemHint()) {
            Toast.makeText(this, "Choose category", Toast.LENGTH_LONG).show();
        }
    }

    public static Intent newIntent(Context context, Long currentQuoteTextId) {
        Intent intent = new Intent(context, AddQuoteActivity.class);
        intent.putExtra(QUOTE_TEXT_ID, currentQuoteTextId);
        return intent;
    }
}
