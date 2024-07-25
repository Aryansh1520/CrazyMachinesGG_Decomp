package com.mappn.sdk.common.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/* loaded from: classes.dex */
public class GfanAlertDialog {

    /* loaded from: classes.dex */
    public interface GfanAlertDialogListener {
        void onButtonClick();
    }

    public static void show(Context context, String str, String str2, String str3, String str4, GfanAlertDialogListener gfanAlertDialogListener, GfanAlertDialogListener gfanAlertDialogListener2) {
        AlertDialog create = new AlertDialog.Builder(context).create();
        create.show();
        Window window = create.getWindow();
        window.setContentView(BaseUtils.get_R_Layout(context, "gfan_view_alertdialog"));
        window.setLayout(-1, -2);
        Button button = (Button) window.findViewById(BaseUtils.get_R_id(context, "button1"));
        Button button2 = (Button) window.findViewById(BaseUtils.get_R_id(context, "button2"));
        TextView textView = (TextView) window.findViewById(BaseUtils.get_R_id(context, "title"));
        TextView textView2 = (TextView) window.findViewById(BaseUtils.get_R_id(context, "msg"));
        textView.setText(str);
        textView2.setLinkTextColor(-24576);
        textView2.setMovementMethod(LinkMovementMethod.getInstance());
        textView2.setText(Html.fromHtml(str2));
        if (str3 == null) {
            button.setVisibility(8);
        } else {
            button.setText(str3);
            button.setOnClickListener(new a(create, gfanAlertDialogListener));
        }
        if (str4 == null) {
            button2.setVisibility(8);
        } else {
            button2.setText(str4);
            button2.setOnClickListener(new b(create, gfanAlertDialogListener2));
        }
    }
}
