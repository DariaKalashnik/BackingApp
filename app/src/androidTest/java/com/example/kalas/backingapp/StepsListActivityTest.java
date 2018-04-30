package com.example.kalas.backingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.kalas.backingapp.activities.StepsListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class StepsListActivityTest {


    private static final String RECIPE_STEP_NAME = "Recipe Introduction";

    @Rule
    public ActivityTestRule<StepsListActivity> mStepsActivityTestRule =
            new ActivityTestRule<>(StepsListActivity.class);


    @Test
    public void testNameOfTheStep() {
//        onView(withId(R.id.recycler_view_step)).perform(RecyclerViewActions.scrollToHolder(holderStepView(RECIPE_STEP_NAME)));
        onView(withId(R.id.recycler_view_step)).perform(RecyclerViewActions.scrollToPosition(3));
    }

//    public static Matcher<RecyclerView.ViewHolder> holderStepView(final String stepName) {
//        return new BoundedMatcher<RecyclerView.ViewHolder, StepAdapter.StepViewHolder>(StepAdapter.StepViewHolder.class) {
//
//            @Override
//            public void describeTo(org.hamcrest.Description description) {
//                description.appendText("No ViewHolder found with the text: " + stepName);
//            }
//
//            @Override
//            protected boolean matchesSafely(StepAdapter.StepViewHolder item) {
//                TextView stepTextView = item.itemView.findViewById(R.id.recipe_short_desc);
//                return stepTextView != null && stepTextView.getText().toString().contains(stepName);
//            }
//
//        };
//    }
}
