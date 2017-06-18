package jp.local.yukichan.alarmsample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;

import jp.local.yukichan.alarmsample.alarm.AlarmController;
import jp.local.yukichan.alarmsample.applications.AlarmSampleApplication;

public class MainActivity extends Activity {

    private AlarmSampleApplication mApplication;

    private AlarmController mAlarmController;

    private AlarmController.AlarmListener mAlarmListener = new AlarmListenerImpl();

    private TextView mTvInfo1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApplication = (AlarmSampleApplication) getApplication();
        mAlarmController = mApplication.getAlarmController();
        mAlarmController.registerAlarmListener(mAlarmListener);
        mAlarmController.setIntervalMinutes(1);

        mTvInfo1 = (TextView) findViewById(R.id.info1);
    }

    @Override
    protected void onDestroy() {
        mAlarmController.unregisterAlarmListener(mAlarmListener);
        super.onDestroy();
    }

    private class AlarmListenerImpl implements AlarmController.AlarmListener {

        @Override
        public void onAlarm(long time) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(time);
            mTvInfo1.setText("Time(min)=" + cal.get(Calendar.MINUTE));
        }
    }
}
