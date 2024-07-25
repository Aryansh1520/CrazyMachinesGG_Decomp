package com.xiaomi.gamecenter.sdk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* loaded from: classes.dex */
class d extends Handler {
    final /* synthetic */ Context a;
    final /* synthetic */ MiCommplatform b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public d(MiCommplatform miCommplatform, Looper looper, Context context) {
        super(looper);
        this.b = miCommplatform;
        this.a = context;
    }

    @Override // android.os.Handler
    public void dispatchMessage(Message message) {
        new AlertDialog.Builder((Activity) this.a).setTitle("提示").setCancelable(true).setMessage("您尚未安装'小米游戏安全插件'，安装后才可用小米账户登录游戏，并保证账户安全。仅需安装一次，是否立即安装？").setPositiveButton("安装", new n(this)).setNegativeButton("取消", new p(this)).setOnCancelListener(new o(this)).show();
    }
}
