package com.mappn.sdk.pay.weight;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.SpinnerAdapter;

/* loaded from: classes.dex */
public class TitleSpinner extends Button implements DialogInterface.OnClickListener {
    private CharSequence a;
    private SpinnerAdapter b;
    private int c;
    private DialogInterface.OnClickListener d;

    public TitleSpinner(Context context) {
        super(context);
        a();
    }

    public TitleSpinner(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        a();
    }

    private void a() {
        this.c = -1;
        setGravity(19);
        setBackgroundResource(R.drawable.btn_dropdown);
    }

    public int getSelectedItemPosition() {
        return this.c;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        setSelection(i);
        dialogInterface.dismiss();
        if (this.d != null) {
            this.d.onClick(dialogInterface, i);
        }
    }

    @Override // android.view.View
    public boolean performClick() {
        boolean performClick = super.performClick();
        if (!performClick) {
            performClick = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            if (this.a != null) {
                builder.setTitle(this.a);
            }
            builder.setSingleChoiceItems(new a(this.b), getSelectedItemPosition(), this).show();
        }
        return performClick;
    }

    public void setAdapter(SpinnerAdapter spinnerAdapter) {
        this.b = spinnerAdapter;
    }

    public void setOnClickListener(DialogInterface.OnClickListener onClickListener) {
        this.d = onClickListener;
    }

    public void setPrompt(CharSequence charSequence) {
        this.a = charSequence;
    }

    public void setSelection(int i) {
        this.c = i;
        setText(this.b.getItem(i).toString());
    }
}
