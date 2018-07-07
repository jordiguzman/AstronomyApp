package appkite.jordiguzman.com.astronomyapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.transition.Transition;

import appkite.jordiguzman.com.astronomyapp.R;
import appkite.jordiguzman.com.astronomyapp.mainUi.Splash;


public class WidgetAstronomyApp extends AppWidgetProvider {

    public static String name, url;
    public static Bitmap bitmap;


    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, WidgetAstronomyApp.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        readSharedPreferences(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_astronomy_app);
        AppWidgetTarget appWidgetTarget = new AppWidgetTarget(context, R.id.iv_widget, views, appWidgetIds) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                super.onResourceReady(resource, transition);
            }
        };

         GlideApp.with(context)
                 .asBitmap()
                 .load(url)
                 .into(appWidgetTarget);
         pushWidgetUpdate(context, views);
        views.setTextViewText(R.id.tv_title_widget, name);
        Intent intent = new Intent(context, Splash.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.layout_widget, pendingIntent);

    }


    public static void readSharedPreferences(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("name", null);
        url = sharedPreferences.getString("url", null);
    }



    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

