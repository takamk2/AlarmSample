package jp.local.yukichan.alarmsample.applications;

import android.app.Application;

import java.io.IOException;

import jp.local.yukichan.alarmsample.alarm.AlarmController;
import jp.local.yukichan.alarmsample.alarm.AlarmControllerImpl;

/**
 * Created by takamk2 on 17/06/18.
 * <p>
 * The Edit Fragment of Base Class.
 */

public class AlarmSampleApplication extends Application {

    private AlarmController mAlarmController;

    @Override
    public void onCreate() {
        super.onCreate();
        mAlarmController = new AlarmControllerImpl(getApplicationContext());
        mAlarmController.startReceiver();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        try {
            mAlarmController.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AlarmController getAlarmController() {
        return mAlarmController;
    }
}
