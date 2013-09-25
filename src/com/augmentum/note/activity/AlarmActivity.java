package com.augmentum.note.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import com.augmentum.note.R;
import com.augmentum.note.fragment.ConfirmDialogFragment;
import com.augmentum.note.model.Note;

import java.util.Random;

public class AlarmActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setVibrate(new long[] {1000, 1000, 1000, 1000, 1000});
        builder.setLights(Color.RED, 0, 1);

        Notification notification = builder.build();

        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;

        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final int NOTIFICATION_REF = new Random(System.currentTimeMillis()).nextInt();
        notificationManager.notify(NOTIFICATION_REF, notification);

        Note note = (Note) getIntent().getSerializableExtra(Note.NOTE_TAG);
        ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment();
        confirmDialogFragment.setTitle(R.string.app_name);
        confirmDialogFragment.setMessage(note.getContent());
        confirmDialogFragment.setPositiveMessage(R.string.note_alert_ok);
        confirmDialogFragment.setPositiveClickListener(new ConfirmDialogFragment.OnPositiveClickListener() {
            @Override
            public void onClick() {
                notificationManager.cancel(NOTIFICATION_REF);
                finish();
            }
        });
        confirmDialogFragment.setNegativeMessage(R.string.note_alert_enter);
        confirmDialogFragment.setNegativeClickListener(new ConfirmDialogFragment.OnNegativeClickListener() {
            @Override
            public void onClick() {
                notificationManager.cancel(NOTIFICATION_REF);
                Intent intent = getIntent();
                intent.setClass(AlarmActivity.this, NoteEditActivity.class);
                startActivity(intent);
            }
        });
        confirmDialogFragment.show(getSupportFragmentManager(), ConfirmDialogFragment.TAG);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}