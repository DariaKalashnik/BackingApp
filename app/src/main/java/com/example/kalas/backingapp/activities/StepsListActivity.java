package com.example.kalas.backingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.kalas.backingapp.R;
import com.example.kalas.backingapp.fragments.DetailsFragment;
import com.example.kalas.backingapp.fragments.StepsListFragment;
import com.example.kalas.backingapp.model.Recipe;
import com.example.kalas.backingapp.utils.Utils;

import java.util.ArrayList;

import static com.example.kalas.backingapp.utils.BuildConfig.RECIPES_KEY;
import static com.example.kalas.backingapp.utils.BuildConfig.SELECTED_STEP_KEY;

public class StepsListActivity extends AppCompatActivity implements StepsListFragment.OnFragmentInteractionListener {

    public static boolean sTabLayout = false;
    public static ArrayList<Recipe> sRecipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_steps);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            sRecipeList = bundle.getParcelableArrayList(RECIPES_KEY);
            Recipe recipe = Utils.setRecipe(sRecipeList);
            setTitle(recipe.getName());
        }

        if (findViewById(R.id.details_fragment) != null) {
            sTabLayout = true;
        }

        if (savedInstanceState == null) {
            if (sTabLayout) {
                DetailsFragment stepDetailsFragment = new DetailsFragment();
                if (bundle != null) {
                    bundle.putInt(SELECTED_STEP_KEY, 0); // 0 index indicates first element in the list
                }

                stepDetailsFragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.details_fragment, stepDetailsFragment)
                        .commit();
            }

        }
    }

    @Override
    public void onFragmentInteraction(int selectedStepId) {
        if (sTabLayout) {
        DetailsFragment stepDetailsFragment = DetailsFragment.newInstance(sRecipeList, selectedStepId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.details_fragment, stepDetailsFragment)
                .commit();

        } else {
            Intent intent = new Intent();
            intent.setClass(this, DetailsActivity.class);
            intent.putParcelableArrayListExtra(RECIPES_KEY, sRecipeList);
            intent.putExtra(SELECTED_STEP_KEY, selectedStepId);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainListActivity.class));
        this.finish();
    }
}
