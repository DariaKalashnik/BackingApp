package com.example.kalas.backingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.kalas.backingapp.activities.StepsListActivity;

import org.junit.Rule;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class StepListActivityTest {


    @Rule
    public ActivityTestRule<StepsListActivity> mStepsActivityTestRule =
            new ActivityTestRule<>(StepsListActivity.class);

}