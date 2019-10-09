package com.example.stickynotes.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.stickynotes.DB.Note;
import com.example.stickynotes.R;

import java.util.List;

public class NotesRemoteViewsService extends RemoteViewsService {

    public static void updateWidget(Context context, List<Note> notes) {
        Prefs.saveNotes(context, notes);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, NoteProviderWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
        NoteProviderWidget.updateAppWidgets(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        return new TasksRemoteViewsFactory(getApplicationContext());
    }

    class TasksRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context mContext;
        private List<Note> mNotesList;

        TasksRemoteViewsFactory(Context context) {
            mContext = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            mNotesList = Prefs.loadNotes(mContext);
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mNotesList == null || mNotesList.isEmpty())
                return 0;
            else
                return mNotesList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
            Note note = mNotesList.get(position);
            remoteViews.setTextViewText(R.id.list_view_text_item, note.getTitle());
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
