package com.mikecoding.buttonclicker;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;



public class Timer {
    private TextView textView;
    private CountDownTimer timer;
    private String timeText;
    private String CountDown;
    private long time;
    private Context context;

    public Timer(TextView textView, String CountDown, Context context) {
        this.textView = textView;
        this.CountDown = CountDown;
        this.context = context;
    }

    public void getTimer() {

        timer = new CountDownTimer(7000,1000) {

        public void onTick ( long millisUntilFinished){
            time = millisUntilFinished / 1000;
            timeText = String.valueOf(time);
            textView.setText(CountDown + " " + timeText);
        }
        public void onFinish() {
            textView.setText(R.string.timer_end);
            ((MainActivity) context).timerFinished();
        }
        };
    }
    public void startTimer(){
        timer.start();
    }
}
