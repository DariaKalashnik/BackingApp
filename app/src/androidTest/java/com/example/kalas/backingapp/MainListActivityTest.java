package com.example.kalas.backingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.kalas.backingapp.activities.MainListActivity;
import com.example.kalas.backingapp.adapter.RecipeAdapter;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainListActivityTest {


    private static final String RECIPE_NAME = "Nutella Pie";

    @Rule
    public ActivityTestRule<MainListActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainListActivity.class);

    public static Matcher<RecyclerView.ViewHolder> holderRecipeView(final String recipeName) {
        return new BoundedMatcher<RecyclerView.ViewHolder, RecipeAdapter.RecipeViewHolder>(RecipeAdapter.RecipeViewHolder.class) {

            @Override
            protected boolean matchesSafely(RecipeAdapter.RecipeViewHolder item) {
                TextView recipeTextView = item.itemView.findViewById(R.id.recipe_name);
                return recipeTextView != null && recipeTextView.getText().toString().contains(recipeName);
            }

            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("No ViewHolder found with the text: " + recipeName);
            }

        };
    }

    @Test
    public void testNameOfTheStep() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToHolder(holderRecipeView(RECIPE_NAME)));
    }
}
