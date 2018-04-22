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

import static com.example.kalas.backingapp.utils.BuildConfig.RECIPE_KEY;

public class MainListActivity extends AppCompatActivity implements ConnectivityReceiverListener, OnFragmentInteractionListener {

    private MainListFragment mListFragment;
    private boolean mIsConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);

        checkInternetConnection();

        mListFragment = (MainListFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_list_items);
    }

    private void checkInternetConnection() {
        mIsConnected = ConnectivityReceiver.isOnline();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isOnline) {
        if (mIsConnected) {
            mListFragment.loadRecipes();
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
        bundle.putParcelableArrayList(RECIPE_KEY, recipesList);

        Intent intent = new Intent(MainListActivity.this, StepsListActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

