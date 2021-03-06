package com.augmentum.note.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import com.augmentum.note.NoteApplication;
import com.augmentum.note.R;
import com.augmentum.note.activity.NoteListActivity;
import com.augmentum.note.dao.impl.NoteDaoImpl;
import com.augmentum.note.model.Note;

public class NoteWidget2x2 extends AppWidgetProvider {

    public static final String WIDGET_UPDATE = "com.augmentum.note.WIDGET_UPDATE_2X2";
    public static final String TAG = "NoteWidget2x2";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.v(TAG, "onUpdate");
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Note note;

        for (int appWidgetId : appWidgetIds) {
            note = NoteDaoImpl.getInstance().getByWidgetId(appWidgetId);
            updateContent(context, appWidgetId, note);
        }

        NoteApplication.sWidgetType = TAG;
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.v(TAG, "onDelete");
        super.onDeleted(context, appWidgetIds);

        Note note;

        for (int appWidgetId : appWidgetIds) {
            note = NoteDaoImpl.getInstance().getByWidgetId(appWidgetId);
            note.setWidgetId(AppWidgetManager.INVALID_APPWIDGET_ID);
            NoteDaoImpl.getInstance().update(note);
        }

    }

    @Override
    public void onDisabled(Context context) {
        Log.v(TAG, "onDisabled");
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        Log.v(TAG, "onEnabled");
        super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.v(TAG, "onReceive");

        if (WIDGET_UPDATE.equals(intent.getAction())) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                Note note = NoteDaoImpl.getInstance().getByWidgetId(appWidgetId);
                updateContent(context, appWidgetId, note);
            }
        }
    }

    private void updateContent(Context context, int appWidgetId, Note note) {
        if (null != note) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            initBackground(note, views);
            views.setTextViewText(R.id.widget_text, note.getContent());
            Intent callbackIntent = new Intent(context, NoteListActivity.class);
            callbackIntent.putExtra(Note.TAG, note.getId());
            callbackIntent.putExtra("widget_type", NoteWidget2x2.TAG);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, callbackIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_layout_main, pendingIntent);
            AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, views);
        }
    }

    private void initBackground(Note mNote, RemoteViews views) {
        switch (mNote.getColor()) {
            case Color.YELLOW:
                views.setInt(R.id.widget_layout_main, "setBackgroundResource", R.drawable.widget_small_yellow);
                break;
            case Color.BLUE:
                views.setInt(R.id.widget_layout_main, "setBackgroundResource", R.drawable.widget_small_blue);
                break;
            case Color.RED:
                views.setInt(R.id.widget_layout_main, "setBackgroundResource", R.drawable.widget_small_red);
                break;
            case Color.GREEN:
                views.setInt(R.id.widget_layout_main, "setBackgroundResource", R.drawable.widget_small_green);
                break;
            case Color.GRAY:
                views.setInt(R.id.widget_layout_main, "setBackgroundResource", R.drawable.widget_small_gray);
                break;
        }
    }
}
