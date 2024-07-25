package com.vivamedia.CMTablet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.view.Display;
import com.vivamedia.cmGGTHD.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: classes.dex */
public class SplashActivity extends Activity {
    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        if (checkScreen()) {
            handlePostScreenCheck();
        } else {
            showScreenWarning();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePostScreenCheck() {
        if (checkSpace()) {
            handlePostSpaceCheck();
        } else {
            showSpaceWarning();
        }
    }

    private void handlePostSpaceCheck() {
        new Thread(new Runnable() { // from class: com.vivamedia.CMTablet.SplashActivity.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SplashActivity.this.runOnUiThread(new Runnable() { // from class: com.vivamedia.CMTablet.SplashActivity.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        SplashActivity.this.cacheStreamData();
                        SplashActivity.this.startGame();
                    }
                });
            }
        }).start();
    }

    private boolean checkScreen() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            display.getSize(size);
        } catch (NoSuchMethodError e) {
            size.x = display.getWidth();
            size.y = display.getHeight();
        }
        int width = size.x;
        int height = size.y;
        int largeedge = width;
        int shortedge = height;
        if (height > largeedge) {
            largeedge = height;
            shortedge = width;
        }
        float aspect = largeedge / shortedge;
        return aspect <= 2.0f && ((double) aspect) >= 1.2d && largeedge >= 800;
    }

    private boolean checkSpace() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        long spaceleft = availableBlocks * blockSize;
        return spaceleft > 5242880;
    }

    private void showScreenWarning() {
        AlertDialog displaysizeWarning = new AlertDialog.Builder(this).create();
        displaysizeWarning.setCancelable(false);
        displaysizeWarning.setTitle("Display Size Warning");
        displaysizeWarning.setMessage("Your display dimensions are not recommended for this game.");
        displaysizeWarning.setButton("OK", new DialogInterface.OnClickListener() { // from class: com.vivamedia.CMTablet.SplashActivity.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                SplashActivity.this.handlePostScreenCheck();
            }
        });
        displaysizeWarning.show();
    }

    private void showSpaceWarning() {
        AlertDialog displaysizeWarning = new AlertDialog.Builder(this).create();
        displaysizeWarning.setCancelable(false);
        displaysizeWarning.setTitle("Not Enough Memory");
        displaysizeWarning.setMessage("You need at least 5 MB space available to run this application.");
        displaysizeWarning.setButton("Exit", new DialogInterface.OnClickListener() { // from class: com.vivamedia.CMTablet.SplashActivity.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                SplashActivity.this.setResult(-1);
                SplashActivity.this.finish();
            }
        });
        displaysizeWarning.show();
    }

    private void copyFile(String filename) {
        AssetManager assetManager = getAssets();
        try {
            InputStream in = assetManager.open(filename);
            String newFileName = "/data/data/" + getPackageName() + "/files/" + filename;
            File myDir = new File("/data/data/" + getPackageName() + "/files/");
            myDir.mkdirs();
            OutputStream out = new FileOutputStream(newFileName);
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    int read = in.read(buffer);
                    if (read != -1) {
                        out.write(buffer, 0, read);
                    } else {
                        in.close();
                        out.flush();
                        out.close();
                        return;
                    }
                }
            } catch (Exception e) {
            }
        } catch (Exception e2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cacheStreamData() {
        File file = new File("/data/data/" + getPackageName() + "/files/", "streaming.dat");
        if (!file.exists()) {
            copyFile("streaming.dat");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startGame() {
        new Handler().postDelayed(new Runnable() { // from class: com.vivamedia.CMTablet.SplashActivity.4
            @Override // java.lang.Runnable
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, (Class<?>) MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, 1000L);
    }
}
