package com.fairyfalls.kds;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;



public class SplashActivity extends AppCompatActivity {

    private int time = 0;
    private final int maxTime = 2;
    static final String START_TIME = "time";
    private MyCountDownTimer timer =null;
    final SplashActivity thisActivity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity);
        if ( savedInstanceState != null){
            time = savedInstanceState.getInt(START_TIME);
            timer = new MyCountDownTimer((maxTime-time)*1000,1000);
        }else{
            timer = new MyCountDownTimer(maxTime*1000,1000);
        }


    }
    @Override
    protected void onStart(){
        super.onStart();
        timer.start();
    }

    @Override
    protected void onPause(){
        super.onPause();
        timer.cancel();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(START_TIME, time);

        if( timer != null ) {

            timer.cancel();

        }

        super.onSaveInstanceState(savedInstanceState);
    }

    private class MyCountDownTimer extends CountDownTimer {

        private MyCountDownTimer(long millis, long countDownInterval) {
            super(millis, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            time++;
        }

        @Override
        public void onFinish() {
            time = 0;
            Intent intent = new Intent(thisActivity, MainActivity.class);
            startActivity(intent);
            thisActivity.finish();
        }
    }
}
