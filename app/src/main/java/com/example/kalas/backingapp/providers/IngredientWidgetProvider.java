package com.example.kalas.backingapp.providers;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.kalas.backingapp.R;
import com.example.kalas.backingapp.activities.MainListActivity;

import static com.example.kalas.backingapp.utils.BuildConfig.KEY_INGREDIENT_TEXT;
import static com.example.kalas.backingapp.utils.BuildConfig.SHARED_PREFS;

public class IngredientWidgetProvider extends AppWidgetProvider {

    private void updateIngredientWidgets(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        Intent intent = new Intent(context, MainListActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        String ingredientText = prefs.getString(KEY_INGREDIENT_TEXT + appWidgetId, context.getString(R.string.default_widget_text));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);

        views.setTextViewText(R.id.widget_ingredient_text, ingredientText);

        views.setOnClickPendingIntent(R.id.example_widget_button, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateIngredientWidgets(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Action performed when one or more AppWidget instances have been deleted
    }

    @Override
    public void onEnabled(Context context) {
        // Action performed action when an AppWidget for this provider is instantiated
    }

    @Override
    public void onDisabled(Context context) {
        // Action performed when the last AppWidget instance for this provider is deleted
    }
}

