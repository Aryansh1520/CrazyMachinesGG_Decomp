package com.mappn.sdk.common.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.widget.TextView;
import java.util.Timer;

/* loaded from: classes.dex */
public class GfanProgressDialog extends ProgressDialog {
    public static final String TAG = "ProgressDialog";
    private static GfanProgressDialog d;
    private static TextView e;
    private static String f;
    private long a;
    private OnTimeOutListener b;
    private Timer c;
    private Handler g;

    /* loaded from: classes.dex */
    public interface OnTimeOutListener {
        void onTimeOut(GfanProgressDialog gfanProgressDialog);
    }

    public GfanProgressDialog(Context context) {
        super(context);
        this.a = 0L;
        this.b = null;
        this.c = null;
        this.g = new c(this);
    }

    public static GfanProgressDialog createProgressDialog(Context context, String str, Boolean bool, long j, OnTimeOutListener onTimeOutListener) {
        BaseUtils.D(TAG, "createProgressDialog" + str);
        if (d != null) {
            dismissDialog();
        }
        GfanProgressDialog gfanProgressDialog = new GfanProgressDialog(context);
        d = gfanProgressDialog;
        gfanProgressDialog.setCancelable(bool.booleanValue());
        f = str;
        if (j != 0) {
            GfanProgressDialog gfanProgressDialog2 = d;
            gfanProgressDialog2.a = j;
            if (onTimeOutListener != null) {
                gfanProgressDialog2.b = onTimeOutListener;
            }
        }
        return d;
    }

    public static void dismissDialog() {
        BaseUtils.D(TAG, "dismissDialog");
        if (d != null) {
            d.dismiss();
        }
    }

    @Override // android.app.ProgressDialog, android.app.Dialog
    public void onStart() {
        super.onStart();
        if (this.a != 0) {
            this.c = new Timer();
            this.c.schedule(new d(this), this.a);
        }
    }

    @Override // android.app.ProgressDialog, android.app.Dialog
    protected void onStop() {
        super.onStop();
        if (this.c != null) {
            this.c.cancel();
            this.c = null;
        }
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        BaseUtils.D(TAG, "onWindowFocusChanged:" + z);
    }

    @Override // android.app.ProgressDialog, android.app.AlertDialog
    public void setMessage(CharSequence charSequence) {
        if (e != null) {
            e.setText(charSequence);
        }
    }

    @Override // android.app.Dialog
    public void show() {
        super.show();
        if (d != null) {
            d.setContentView(BaseUtils.get_R_Layout(getContext(), "gfan_progress_dialog"));
            TextView textView = (TextView) findViewById(BaseUtils.get_R_id(getContext(), "textView"));
            e = textView;
            textView.setText(f);
            e.setTextColor(-16777216);
        }
    }
}
