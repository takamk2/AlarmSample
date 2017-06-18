package jp.local.yukichan.alarmsample.alarm;

import android.app.PendingIntent;

import java.io.Closeable;

/**
 * Created by takamk2 on 17/06/18.
 * <p>
 * The Edit Fragment of Base Class.
 */

public interface AlarmController extends Closeable {

    public interface AlarmListener {
        /**
         * setした時間に通知を行う
         * @param time
         */
        void onAlarm(long time);
    }

    /**
     * 間隔(分)をセットする
     *
     * @param minutes
     */
    void setIntervalMinutes(int minutes);

    /**
     * 通知を受ける時間をsetする
     *
     * @param time
     */
    PendingIntent setAlarmTime(long time);

    /**
     * Broadcastを監視する
     */
    void startReceiver();

    /**
     * Broadcastの監視を終了する
     */
    void stopReceiver();

    /**
     * Broadcast監視中かを返す
     *
     * @return
     */
    boolean isStarted();

    void registerAlarmListener(AlarmListener listener);

    void unregisterAlarmListener(AlarmListener listener);
}
