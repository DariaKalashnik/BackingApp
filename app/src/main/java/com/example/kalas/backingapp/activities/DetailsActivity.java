package com.example.kalas.backingapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.kalas.backingapp.R;
import com.example.kalas.backingapp.model.Recipe;
import com.example.kalas.backingapp.utils.Utils;

import java.util.ArrayList;

import static com.example.kalas.backingapp.utils.BuildConfig.RECIPES_KEY;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ArrayList<Recipe> recipes = getIntent().getParcelableArrayListExtra(RECIPES_KEY);
        Recipe recipe = Utils.setRecipe(recipes);
        setTitle(recipe.getName());
    }
}
