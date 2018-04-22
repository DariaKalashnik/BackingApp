package com.example.kalas.backingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.kalas.backingapp.R;
import com.example.kalas.backingapp.fragments.DetailsFragment;
import com.example.kalas.backingapp.model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.kalas.backingapp.utils.BuildConfig.COUNT_STEPS_KEY;
import static com.example.kalas.backingapp.utils.BuildConfig.RECIPE_KEY;
import static com.example.kalas.backingapp.utils.Utils.setRecipe;

public class DetailsActivity extends AppCompatActivity {

    public static int COUNT_STEPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            DetailsFragment stepDetailsFragment = new DetailsFragment();
            //stepDetailsFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.portrait_detils_fragment, stepDetailsFragment)
                    .commit();
        }

        COUNT_STEPS = getIntent().getIntExtra(COUNT_STEPS_KEY, 0);
        ArrayList<Recipe> recipes = getIntent().getParcelableArrayListExtra(RECIPE_KEY);
        Recipe recipe = setRecipe(recipes);
        setTitle(recipe.getName());

    }

    @Override
    public void onBackPressed() {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(this, StepsListActivity.class);
//            bundle.putParcelableArrayList(RECIPE_KEY, recipes);
//            intent.putExtras(bundle);
            startActivity(intent);
            this.finish();
    }
}
