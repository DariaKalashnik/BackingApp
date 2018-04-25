package com.example.kalas.backingapp.activities;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.kalas.backingapp.R;
import com.example.kalas.backingapp.model.Ingredient;
import com.example.kalas.backingapp.model.Recipe;
import com.example.kalas.backingapp.utils.Utils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.example.kalas.backingapp.utils.BuildConfig.DASH;

public class IngredientWidgetActivityConfig extends AppCompatActivity {

    public static final String SHARED_PREFS = "prefs";
    public static final String KEY_INGREDIENT_TEXT = "INGREDIENTS DATA";
    private List<Recipe> mRecipesList;
    private String mUserInput;
    private Recipe mRecipe;
    private RemoteViews mRemoteViews;
    private String[] mRecipesNames;
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
            Utils.showToast(this, "list is empty");
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
        mRemoteViews.setOnClickPendingIntent(R.id.widget_ingredient_text, pendingIntent);

        checkUserInputValue();
    }

    private void checkUserInputValue() {
        for (int i = 0; i < mRecipesList.size(); i++) {
            mRecipe = mRecipesList.get(i);
            mRecipesNames = new String[4];
            mRecipesNames[i] = mRecipe.getName().trim();
            if (mUserInput.equalsIgnoreCase(mRecipesNames[i])) {
                printIngredients();
            } else {
                Toast toast = new Toast(this);
                toast.setView(getLayoutInflater().inflate(R.layout.toast_message, (ViewGroup) findViewById(R.id.toast_msg_container)));
                toast.setGravity(Gravity.BOTTOM, 0, 50);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }

    private void printIngredients() {
        List<Ingredient> ingredients = mRecipe.getIngredients();
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for (Ingredient ingredient : ingredients) {
            builder.append(StringUtils.capitalize(ingredient.getIngredient())).append(DASH)
                    .append(String.valueOf(ingredient.getQuantity())).append(StringUtils.SPACE)
                    .append(ingredient.getMeasure().toLowerCase()).append(StringUtils.LF);
        }

        mRemoteViews.setTextViewText(R.id.widget_ingredient_text, builder.toString());

        mAppWidgetManager.updateAppWidget(appWidgetId, mRemoteViews);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_INGREDIENT_TEXT + appWidgetId, mUserInput);
        editor.apply();

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}
