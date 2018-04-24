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

import static com.example.kalas.backingapp.activities.IngredientWidgetActivityConfig.KEY_BUTTON_TEXT;
import static com.example.kalas.backingapp.activities.IngredientWidgetActivityConfig.SHARED_PREFS;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MainListActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            String buttonText = prefs.getString(KEY_BUTTON_TEXT + appWidgetId, "Press me");

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);
            views.setOnClickPendingIntent(R.id.example_widget_button, pendingIntent);
            views.setCharSequence(R.id.example_widget_button, "setText", buttonText);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}

