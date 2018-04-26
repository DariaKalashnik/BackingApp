package com.example.kalas.backingapp.activities;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;

import com.example.kalas.backingapp.R;
import com.example.kalas.backingapp.model.Recipe;
import com.example.kalas.backingapp.utils.Utils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.example.kalas.backingapp.utils.BuildConfig.KEY_INGREDIENT_TEXT;
import static com.example.kalas.backingapp.utils.BuildConfig.SHARED_PREFS;

public class WidgetActivityConfig extends AppCompatActivity {

    private List<Recipe> mRecipesList;
    private String mUserInput;
    private Recipe mRecipe;
    private RemoteViews mRemoteViews;
    private AppWidgetManager mAppWidgetManager;
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private EditText editTextButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_widget_config);

        if (MainListActivity.sRecipesList != null) {
            mRecipesList = MainListActivity.sRecipesList;
        } else {
            Utils.showToast(this, getResources().getString(R.string.error_recipes_list_is_empty));
        }

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

        editTextButton = findViewById(R.id.widget_user_input);
    }

    public void confirmConfiguration(View v) {
        mAppWidgetManager = AppWidgetManager.getInstance(this);

        Intent intent = new Intent(this, MainListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        mUserInput = editTextButton.getText().toString();

        mRemoteViews = new RemoteViews(this.getPackageName(), R.layout.ingredient_widget);

        checkUserInputValue();

        mRemoteViews.setOnClickPendingIntent(R.id.example_widget_button, pendingIntent);

    }

    private void checkUserInputValue() {
        for (int i = 0; i < mRecipesList.size(); i++) {
            mRecipe = mRecipesList.get(i);
            String[] mRecipesNames = new String[4];
            mRecipesNames[i] = mRecipe.getName().trim();

            if (mUserInput.equalsIgnoreCase(mRecipesNames[i])) {
                printIngredients(mRecipesNames[i]);
            } else {
                Utils.showCustomToast(this, WidgetActivityConfig.this);
            }
        }

    }

    private void printIngredients(String recipeName) {

        SpannableStringBuilder builder = new SpannableStringBuilder();
        Utils.displayIngredientData(mRecipe, builder);

        String ingredientText = recipeName.concat(StringUtils.LF).concat(builder.toString());
        mRemoteViews.setTextViewText(R.id.widget_ingredient_text, ingredientText);

        mAppWidgetManager.updateAppWidget(appWidgetId, mRemoteViews);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_INGREDIENT_TEXT + appWidgetId, ingredientText);
        editor.apply();

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}
