package com.doctappo;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import Config.ApiParams;
import util.CommonClass;
import util.ContextWrapper;

/**
 * Created by subhashsanghani on 5/22/17.
 */

public abstract class CommonActivity extends AppCompatActivity {

    public CommonClass common;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        common = new CommonClass(this);

        super.onCreate(savedInstanceState);
    }

    // set back arrow on actionbar and back event
    public void allowBack() {
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    // get font from assets folder
    public Typeface getCustomFont() {
        return Typeface.createFromAsset(getAssets(), "LobsterTwo-BoldItalic.ttf");
    }

    // set custom header title in actionbar
    public void setHeaderTitle(String title) {
        Typeface font = getCustomFont();

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custome_header_bar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.action_bar_title);
        mTitleTextView.setTypeface(font);
        mTitleTextView.setText(title);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    // set animation in progressbar
    public void setProgressBarAnimation(ProgressBar progressBar1) {
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar1, "progress", 0, 500); // see this max value coming back here, we animale towards that value
        animation.setDuration(5000); //in milliseconds
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setRepeatCount(AlphaAnimation.INFINITE);
        animation.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Log.i(newBase.toString(), "attachBaseContext: " + CommonClass.getLanguage(newBase));
        Locale newLocale = new Locale(CommonClass.getLanguage(newBase));
        // .. create or get your new Locale object here.
        Context context = ContextWrapper.wrap(newBase, newLocale);
        super.attachBaseContext(context);
    }

    public static boolean setFavorite(Context context, String id) {
        List<String> list = new ArrayList<String>(Arrays.asList(getFavorite(context).split(",")));

        if (list.contains(id)) {
            list.remove(id);
            context.getSharedPreferences(ApiParams.PREF_NAME, 0).edit().putString("favorite", TextUtils.join(",", list)).apply();
            return false;
        } else {
            list.add(id);
            context.getSharedPreferences(ApiParams.PREF_NAME, 0).edit().putString("favorite", TextUtils.join(",", list)).apply();
            return true;
        }
    }

    public static String getFavorite(Context context) {
        return context.getSharedPreferences(ApiParams.PREF_NAME, 0).getString("favorite", "");
    }

    public static int DpToPx(Context context, Float dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }

    public static int spToPx(Context context, Float dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, context.getResources().getDisplayMetrics()));
    }


}
