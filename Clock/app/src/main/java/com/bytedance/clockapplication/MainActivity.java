package com.bytedance.clockapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bytedance.clockapplication.widget.Clock;

import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    private View mRootView;
    private Clock mClockView;
    private final int MSG_RENEW = 1;

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_RENEW:
                    mClockView.invalidate();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRootView = findViewById(R.id.root);
        mClockView = findViewById(R.id.clock);

        new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    while(true) {
                        Thread.sleep(1000);
                        mHandler.sendMessage(Message.obtain(mHandler, MSG_RENEW));
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClockView.setShowAnalog(!mClockView.isShowAnalog());
            }
        });
    }

    //在mainActivity被销毁时清空消息队列，防止内存泄漏
    @Override
    protected  void onDestroy(){
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

}
