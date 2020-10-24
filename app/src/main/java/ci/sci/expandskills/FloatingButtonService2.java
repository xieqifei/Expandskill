package ci.sci.expandskills;

import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by dongzhong on 2018/5/30.
 */

public class FloatingButtonService2 extends Service {
    public static boolean isStarted = false;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private Button button2;

    @Override
    public void onCreate() {
        super.onCreate();
        isStarted = true;
        Log.i("TAG", "onCreate: 2");
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = 200;
        layoutParams.height = 200;
        layoutParams.x = 300;
        layoutParams.y = 300;
//        layoutParams.alpha = 120;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showFloatingWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    private void showFloatingWindow() {
        if (Settings.canDrawOverlays(this)) {
            button2 = new Button(getApplicationContext());
            button2.setText("装");
//            button.setTextAppearance(R.style.button1);
            button2.setBackgroundColor(Color.argb(120,0,255,0));
            windowManager.addView(button2, layoutParams);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    send();
                }
            });
            button2.setOnTouchListener(new FloatingOnTouchListener());

        }
    }
    private void send() {
        //开启线程，发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                char value = '3';
                String path = "http://192.168.0.109:9797/?skill="+value;
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    InputStream in = conn.getInputStream();
                    conn.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                default:
                    break;
            }
            if (event.getEventTime ( ) - event.getDownTime ( ) < 200)
            {
                return false; // 设置为false，使OnClickListener获取到监听
            }
            else
            {
                return true;// 设置为true，使OnClickListener不能获取到监听
            }
        }
    }
}
