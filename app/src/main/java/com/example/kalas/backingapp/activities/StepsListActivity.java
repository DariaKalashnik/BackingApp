package com.example.kalas.backingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.kalas.backingapp.R;
import com.example.kalas.backingapp.fragments.DetailsFragment;
import com.example.kalas.backingapp.fragments.StepsListFragment;
import com.example.kalas.backingapp.model.Recipe;

import java.util.ArrayList;

import static com.example.kalas.backingapp.utils.BuildConfig.RECIPE_KEY;
import static com.example.kalas.backingapp.utils.BuildConfig.SELECTED_STEP_KEY;
import static com.example.kalas.backingapp.utils.Utils.setRecipe;

public class StepsListActivity extends AppCompatActivity implements StepsListFragment.OnFragmentInteractionListener {

    private boolean mTabLayout = false;
    public static ArrayList<Recipe> sRecipeList;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_steps);

        mBundle = getIntent().getExtras();

        if (mBundle != null) {
            sRecipeList = mBundle.getParcelableArrayList(RECIPE_KEY);
            Recipe recipe = setRecipe(sRecipeList);
            setTitle(recipe.getName());
        }

        if (findViewById(R.id.landscape_detils_fragment) != null) {
            mTabLayout = true;
        }

        if (savedInstanceState == null) {
            if (mTabLayout) {
//            StepsListFragment stepFragment = new StepsListFragment();
//            //mBundle.putInt(SELECTED_STEP_KEY, 0);
//            //stepFragment.setArguments(mBundle);
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.landscape_step_list_fragment, stepFragment)
//                    .addToBackStack("steps")
//                    .commit();

                //getSupportFragmentManager().findFragmentById(R.id.recipe_list_ingredients);

                DetailsFragment stepDetailsFragment = new DetailsFragment();
                mBundle.putInt(SELECTED_STEP_KEY, 0);
                stepDetailsFragment.setArguments(mBundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.landscape_detils_fragment, stepDetailsFragment)
                        .addToBackStack("details")
                        .commit();
            }

        }
    }

    @Override
    public void onFragmentInteraction(int selectedStepId) {
//        if (mTabLayout) {
        DetailsFragment stepDetailsFragment = DetailsFragment.newInstance(sRecipeList, selectedStepId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.landscape_detils_fragment, stepDetailsFragment)
                .commit();

//        } else {
//            Intent intent = new Intent();
//            intent.setClass(this, DetailsActivity.class);
//            intent.putExtra(COUNT_STEPS_KEY, countSteps);
//            intent.putExtra(SELECTED_STEP_KEY, selectedStepId);
//            startActivity(intent);
//        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainListActivity.class));
        this.finish();
    }
}
