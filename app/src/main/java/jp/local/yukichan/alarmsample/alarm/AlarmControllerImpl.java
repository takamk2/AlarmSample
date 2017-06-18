package jp.local.yukichan.alarmsample.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static android.os.DropBoxManager.EXTRA_TIME;

/**
 *
 */
public class AlarmControllerImpl implements AlarmController {

    private static final String LOGTAG = AlarmControllerImpl.class.getSimpleName();

    private static final String ACTION_ALARM_EVENT =
            "jp.local.yukichan.alarmsample.action.ALARM_EVENT";

    private final Context mContext;
    private final AlarmManager mAlarmManager;
    private boolean mIsStarted = false;

    private int mIntervalMinutes = 5;

    private PendingIntent mPendingIntent = null;

    private List<AlarmListener> mListeners = new CopyOnWriteArrayList<>();

    private BroadcastReceiver mAlarmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOGTAG, "onReceive - DEBUG: in");
            long time = intent.getLongExtra(EXTRA_TIME, 0L);
            notifyAlarm(time);
            mPendingIntent = setAlarmTime(getNextAlarmTime());
            Log.d(LOGTAG, "onReceive - DEBUG: out time=" + timeToStr(time));
        }
    };

    public AlarmControllerImpl(Context context) {
        Log.d(LOGTAG, "AlarmControllerImpl - DEBUG: in");
        mContext = context;
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        startReceiver();
        Log.d(LOGTAG, "AlarmControllerImpl - DEBUG: out");
    }

    @Override
    public void setIntervalMinutes(int minutes) {
        Log.d(LOGTAG, "setIntervalMinutes - DEBUG: in");
        if (mIntervalMinutes == minutes) {
            return;
        }
        if (mPendingIntent != null) {
            mAlarmManager.cancel(mPendingIntent);
        }
        mIntervalMinutes = minutes;
        mPendingIntent = setAlarmTime(getNextAlarmTime());
        Log.d(LOGTAG, "setIntervalMinutes - DEBUG: out");
    }

    @Override
    public PendingIntent setAlarmTime(long time) {
        Log.d(LOGTAG, "setAlarmTime - DEBUG: in");
        if (mPendingIntent != null) {
            mAlarmManager.cancel(mPendingIntent);
            mPendingIntent = null;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);
        intent.setAction(ACTION_ALARM_EVENT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 1, intent, 0);
        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        Log.d(LOGTAG, "setAlarmTime - DEBUG: out");
        return pendingIntent;
    }

    @Override
    public void startReceiver() {
        Log.d(LOGTAG, "startReceiver - DEBUG: in");
        mIsStarted = true;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_ALARM_EVENT);
        mContext.registerReceiver(mAlarmReceiver, intentFilter);
        mPendingIntent = setAlarmTime(getNextAlarmTime());
        Log.d(LOGTAG, "startReceiver - DEBUG: out");
    }

    @Override
    public void stopReceiver() {
        Log.d(LOGTAG, "stopReceiver - DEBUG: in");
        mContext.unregisterReceiver(mAlarmReceiver);
        mIsStarted = false;
        Log.d(LOGTAG, "stopReceiver - DEBUG: out");
    }

    @Override
    public boolean isStarted() {
        Log.d(LOGTAG, "isStarted - DEBUG: in");
        Log.d(LOGTAG, "isStarted - DEBUG: out");
        return mIsStarted;
    }

    @Override
    public void registerAlarmListener(AlarmListener listener) {
        Log.d(LOGTAG, "registerAlarmListener - DEBUG: in");
        mListeners.add(listener);
        Log.d(LOGTAG, "registerAlarmListener - DEBUG: out");
    }

    @Override
    public void unregisterAlarmListener(AlarmListener listener) {
        Log.d(LOGTAG, "unregisterAlarmListener - DEBUG: in");
        mListeners.remove(listener);
        Log.d(LOGTAG, "unregisterAlarmListener - DEBUG: out");
    }

    @Override
    public void close() throws IOException {
        Log.d(LOGTAG, "close - DEBUG: in");
        stopReceiver();
        Log.d(LOGTAG, "close - DEBUG: out");
    }

    private long getNextAlarmTime() {
        Log.d(LOGTAG, "getNextAlarmTime - DEBUG: in");
        Calendar now = Calendar.getInstance();
        long nowTime = now.getTimeInMillis();
        long interval = mIntervalMinutes * 60 * 1000;
        long nextIntervalCount = (long) (Math.floor(nowTime / interval)) + 1;
        long nextTime = nextIntervalCount * interval;

        Log.d(LOGTAG, "getNextTime - DEBUG: nowTime = " + nowTime + "(" + timeToStr(nowTime) + ") nextIntervalCount=" + nextIntervalCount + " nextTime=" + nextTime + "(" + timeToStr(nextTime) + ")");
        Log.d(LOGTAG, "getNextAlarmTime - DEBUG: out");
        return nextTime;
    }

    private String timeToStr(long time) {
        Log.d(LOGTAG, "timeToStr - DEBUG: in");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        Log.d(LOGTAG, "timeToStr - DEBUG: out");
        return String.format("%02d/%02d %02d:%02d:%02d.%03d", cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DATE), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND), cal.get(Calendar.MILLISECOND));
    }

    private void notifyAlarm(long time) {
        for (AlarmListener listener : mListeners) {
            listener.onAlarm(time);
        }
    }
}
