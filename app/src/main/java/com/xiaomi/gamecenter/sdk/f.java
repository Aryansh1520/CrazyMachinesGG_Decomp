package com.xiaomi.gamecenter.sdk;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.widget.PopupWindow;
import android.widget.RemoteViews;

/* loaded from: classes.dex */
class f implements Runnable {
    final /* synthetic */ RemoteViews a;
    final /* synthetic */ Activity b;
    final /* synthetic */ MiCommplatform c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(MiCommplatform miCommplatform, RemoteViews remoteViews, Activity activity) {
        this.c = miCommplatform;
        this.a = remoteViews;
        this.b = activity;
    }

    @Override // java.lang.Runnable
    public void run() {
        Context context;
        RemoteViews remoteViews = this.a;
        context = this.c.ctx;
        PopupWindow popupWindow = new PopupWindow(remoteViews.apply(context, null), -2, -2, true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(this.b.getWindow().getDecorView(), 48, 0, 0);
    }
}
