package com.google.ads.internal;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class AdVideoView extends FrameLayout implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {
    private static final com.google.ads.internal.a b = com.google.ads.internal.a.a.b();
    public MediaController a;
    private WeakReference<Activity> c;
    private AdWebView d;
    private long e;
    private VideoView f;
    private String g;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class a implements Runnable {
        private WeakReference<AdVideoView> a;
        private Handler b = new Handler();

        public a(AdVideoView adVideoView) {
            this.a = new WeakReference<>(adVideoView);
        }

        @Override // java.lang.Runnable
        public void run() {
            AdVideoView adVideoView = this.a.get();
            if (adVideoView == null) {
                com.google.ads.util.b.d("The video must be gone, so cancelling the timeupdate task.");
            } else {
                adVideoView.f();
                this.b.postDelayed(this, 250L);
            }
        }

        public void a() {
            this.b.postDelayed(this, 250L);
        }
    }

    public AdVideoView(Activity adActivity, AdWebView adWebView) {
        super(adActivity);
        this.c = new WeakReference<>(adActivity);
        this.d = adWebView;
        this.f = new VideoView(adActivity);
        addView(this.f, new FrameLayout.LayoutParams(-1, -1, 17));
        this.a = null;
        this.g = null;
        this.e = 0L;
        a();
        this.f.setOnCompletionListener(this);
        this.f.setOnPreparedListener(this);
        this.f.setOnErrorListener(this);
    }

    protected void a() {
        new a(this).a();
    }

    public void b() {
        if (!TextUtils.isEmpty(this.g)) {
            this.f.setVideoPath(this.g);
        } else {
            b.a(this.d, "onVideoEvent", "{'event': 'error', 'what': 'no_src'}");
        }
    }

    public void setMediaControllerEnabled(boolean enabled) {
        Activity activity = this.c.get();
        if (activity == null) {
            com.google.ads.util.b.e("adActivity was null while trying to enable controls on a video.");
            return;
        }
        if (enabled) {
            if (this.a == null) {
                this.a = new MediaController(activity);
            }
            this.f.setMediaController(this.a);
        } else {
            if (this.a != null) {
                this.a.hide();
            }
            this.f.setMediaController(null);
        }
    }

    public void setSrc(String src) {
        this.g = src;
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mp) {
        b.a(this.d, "onVideoEvent", "{'event': 'ended'}");
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mp, int what, int extra) {
        com.google.ads.util.b.e("Video threw error! <what:" + what + ", extra:" + extra + ">");
        b.a(this.d, "onVideoEvent", "{'event': 'error', 'what': '" + what + "', 'extra': '" + extra + "'}");
        return true;
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public void onPrepared(MediaPlayer mp) {
        b.a(this.d, "onVideoEvent", "{'event': 'canplaythrough', 'duration': '" + (this.f.getDuration() / 1000.0f) + "'}");
    }

    public void c() {
        this.f.pause();
    }

    public void d() {
        this.f.start();
    }

    public void a(int i) {
        this.f.seekTo(i);
    }

    public void a(MotionEvent motionEvent) {
        this.f.onTouchEvent(motionEvent);
    }

    public void e() {
        this.f.stopPlayback();
    }

    void f() {
        long currentPosition = this.f.getCurrentPosition();
        if (this.e != currentPosition) {
            b.a(this.d, "onVideoEvent", "{'event': 'timeupdate', 'time': " + (((float) currentPosition) / 1000.0f) + "}");
            this.e = currentPosition;
        }
    }
}
