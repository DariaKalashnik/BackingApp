package com.example.kalas.backingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.kalas.backingapp.MyApplication;
import com.example.kalas.backingapp.R;
import com.example.kalas.backingapp.fragments.MainListFragment;
import com.example.kalas.backingapp.fragments.MainListFragment.OnFragmentInteractionListener;
import com.example.kalas.backingapp.model.Recipe;
import com.example.kalas.backingapp.utils.ConnectivityReceiver;
import com.example.kalas.backingapp.utils.ConnectivityReceiver.ConnectivityReceiverListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.kalas.backingapp.utils.BuildConfig.RECIPES_KEY;

/**
 * Created by Daria Kalashnikova (25/04/2018)
 *
 * Used resources:
 * https://chantisandroid.blogspot.hu/2017/03/check-internet-connection-using.html
 * https://www.androidhive.info/2012/07/android-detect-internet-connection-status/
 * https://inducesmile.com/android/how-to-set-recycleview-item-row-background-color-in-android/
 * https://codelabs.developers.google.com/codelabs/exoplayer-intro/index.html?index=..%2F..%2Findex#2
 * https://commons.apache.org/proper/commons-lang/
 * https://www.youtube.com/watch?time_continue=155&v=5BzYH6Vq6ZU
 * http://alexander-thiele.blogspot.hu/2016/01/espresso-ui-tests-and-recyclerview.html
 * https://github.com/shohrabuddin/Espresso
 * http://www.vogella.com/tutorials/AndroidTestingEspresso/article.html
 */

public class MainListActivity extends AppCompatActivity implements ConnectivityReceiverListener, OnFragmentInteractionListener {

    public static List<Recipe> sRecipesList;
    private boolean mIsConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);

        checkInternetConnection();
    }

    private void checkInternetConnection() {
        mIsConnected = ConnectivityReceiver.isOnline();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isOnline) {
        if (mIsConnected) {
            MainListFragment mainListFragment = (MainListFragment) getSupportFragmentManager().findFragmentById(R.id.recipes_list);
            mainListFragment.loadRecipeData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onFragmentInteraction(Recipe recipe) {
        Bundle bundle = new Bundle();
        ArrayList<Recipe> recipesList = new ArrayList<>();
        recipesList.add(recipe);

        bundle.putParcelableArrayList(RECIPES_KEY, recipesList);
        Intent intent = new Intent(MainListActivity.this, StepsListActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

