package com.example.kalas.backingapp.activities;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;

import com.example.kalas.backingapp.R;
import com.example.kalas.backingapp.fragments.MainListFragment;
import com.example.kalas.backingapp.model.Ingredient;
import com.example.kalas.backingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class IngredientWidgetActivityConfig extends AppCompatActivity {

    public static final String SHARED_PREFS = "prefs";
    public static final String KEY_BUTTON_TEXT = "keyButtonText";
    private ArrayList<Recipe> mRecipesList;
    private List<Ingredient> mIngredients;
    private String mUserInput;
    private Recipe mRecipe;

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private EditText editTextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_widget_config);

        mRecipesList = MainListFragment.sRecipesList;

        Intent configIntent = getIntent();
        Bundle extras = configIntent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        editTextButton = findViewById(R.id.edit_text_button);
    }

    public void confirmConfiguration(View v) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        Intent intent = new Intent(this, MainListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        mUserInput = editTextButton.getText().toString();

        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.ingredient_widget);
        views.setOnClickPendingIntent(R.id.example_widget_button, pendingIntent);

        checkUserInputValue(mUserInput);

        views.setCharSequence(R.id.widget_ingredient_text, "setText", mUserInput);

        appWidgetManager.updateAppWidget(appWidgetId, views);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_BUTTON_TEXT + appWidgetId, mUserInput);
        editor.apply();

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    private void checkUserInputValue(String userInput) {
        for (int i = 0; i < mRecipesList.size(); i++) {
            mRecipe = mRecipesList.get(i);
        }

        mIngredients = mRecipe.getIngredients();
        for (Ingredient ingredient : mIngredients) {
            if (userInput.equals(mRecipe.getName().trim())) {
                userInput = ingredient.getIngredient();
            }
        }

    }
}
