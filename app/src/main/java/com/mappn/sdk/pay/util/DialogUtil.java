package com.mappn.sdk.pay.util;

import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.mappn.sdk.common.utils.BaseUtils;

/* loaded from: classes.dex */
public class DialogUtil {

    /* loaded from: classes.dex */
    public interface CheckBoxWarningDialogListener {
        void onWarningDialogCancel(int i);

        void onWarningDialogOK(int i, boolean z);
    }

    /* loaded from: classes.dex */
    public interface EditTextDialogListener {
        void onEditTextDialogCancel(int i);

        void onEditTextDialogOK(int i, String str);
    }

    /* loaded from: classes.dex */
    public interface InfoDialogListener {
        void onInfoDialogOK(int i);
    }

    /* loaded from: classes.dex */
    public interface InputDialogListener {
        void onInputDialogCancel(int i);

        void onInputDialogOK(int i, String str);
    }

    /* loaded from: classes.dex */
    public interface ListCheckboxDialogListener {
        void onListDialogCancel(int i, CharSequence[] charSequenceArr);

        void onListDialogOK(int i, CharSequence[] charSequenceArr, int i2, int i3);
    }

    /* loaded from: classes.dex */
    public interface ListDIalogListener {
        void onListDialogOK(int i, int i2);
    }

    /* loaded from: classes.dex */
    public interface ListDialogListener2 {
        void onListDialogCancel2(int i, Object[] objArr);

        void onListDialogOK2(int i, Object[] objArr, int i2);
    }

    /* loaded from: classes.dex */
    public interface ProgressDialogListener {
        void onProgressDialogCancel(int i);
    }

    /* loaded from: classes.dex */
    public interface RatingDialogListener {
        void onRatingDialogCancel();

        void onRatingDialogOK(int i, float f);
    }

    /* loaded from: classes.dex */
    public interface RegisterDialogListener {
        void onRegisterDialogCancel(int i);

        void onRegisterDialogOK(int i, String str, String str2, String str3);
    }

    /* loaded from: classes.dex */
    public interface UserPwdDialogListener {
        void onUserPwdDialogCancel(int i);

        void onUserPwdDialogOK(int i, String str, String str2, boolean z);

        void onUserPwdDialogRegister(int i);
    }

    /* loaded from: classes.dex */
    public interface WarningDialogListener {
        void onWarningDialogCancel(int i);

        void onWarningDialogOK(int i);
    }

    public static ProgressDialog createDeterminateProgressDialog(Context context, int i, String str, boolean z, ProgressDialogListener progressDialogListener) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIcon(R.drawable.ic_dialog_info);
        progressDialog.setTitle(str);
        progressDialog.setProgressStyle(1);
        if (z) {
            progressDialog.setButton(context.getString(BaseUtils.get_R_String(context, "cancel")), new a(context, i, progressDialogListener));
            progressDialog.setOnCancelListener(new c(context, i, progressDialogListener));
        }
        if (!z) {
            progressDialog.setOnDismissListener(new d(context, i));
        }
        return progressDialog;
    }

    public static Dialog createOKWarningDialog(Context context, int i, CharSequence charSequence, WarningDialogListener warningDialogListener) {
        return createOKWarningDialog(context, i, null, charSequence, warningDialogListener);
    }

    public static Dialog createOKWarningDialog(Context context, int i, CharSequence charSequence, CharSequence charSequence2, WarningDialogListener warningDialogListener) {
        AlertDialog.Builder cancelable = new AlertDialog.Builder(context).setCancelable(true);
        if (TextUtils.isEmpty(charSequence)) {
            charSequence = Constants.ERROR_TITLE;
        }
        return cancelable.setTitle(charSequence).setMessage(charSequence2).setPositiveButton(Constants.TEXT_OK, new f(context, i, warningDialogListener)).setOnCancelListener(new e(context, i, warningDialogListener)).create();
    }

    public static Dialog createOKWarningDialogSupportLink(Context context, int i, String str, String str2, WarningDialogListener warningDialogListener) {
        TextView textView = new TextView(context);
        textView.setPadding(10, 10, 10, 10);
        textView.setTextAppearance(context, R.style.TextAppearance.Medium.Inverse);
        textView.setBackgroundColor(-1);
        textView.setLinkTextColor(-24576);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(Html.fromHtml(str2));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.addView(textView);
        ScrollView scrollView = new ScrollView(context);
        scrollView.setVerticalFadingEdgeEnabled(false);
        scrollView.addView(linearLayout);
        return new AlertDialog.Builder(context).setTitle(str).setView(scrollView).setPositiveButton(Constants.TEXT_OK, (DialogInterface.OnClickListener) null).setPositiveButton(Constants.TEXT_OK, new b(context, i, warningDialogListener)).setOnCancelListener(new j(context, i, warningDialogListener)).create();
    }

    public static Dialog createTwoButtonsDialog(Context context, int i, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, WarningDialogListener warningDialogListener) {
        return new AlertDialog.Builder(context).setTitle(Constants.TEXT_WARNING).setMessage(charSequence).setPositiveButton(charSequence2, new i(context, i, warningDialogListener)).setNegativeButton(charSequence3, new h(context, i, warningDialogListener)).setOnCancelListener(new g(context, i)).create();
    }

    public static Dialog createYesNoDialog(Context context, int i, CharSequence charSequence, WarningDialogListener warningDialogListener) {
        return createTwoButtonsDialog(context, i, charSequence, Constants.TEXT_OK, "取消", warningDialogListener);
    }
}
