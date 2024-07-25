package com.mappn.sdk.pay.chargement.alipay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import com.mappn.sdk.pay.chargement.ChargeActivity;
import com.mappn.sdk.pay.util.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class BaseHelper {
    public static void chmod(String str, String str2) {
        try {
            Process exec = Runtime.getRuntime().exec("chmod " + str + " " + str2);
            if (exec != null) {
                do {
                } while (new BufferedReader(new InputStreamReader(exec.getInputStream())).readLine() != null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String convertStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        sb.append(readLine);
                    } else {
                        try {
                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
        inputStream.close();
        return sb.toString();
    }

    public static void log(String str, String str2) {
        Log.d(str, str2);
    }

    public static void showDialog(Activity activity, String str, String str2, int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(i);
        builder.setTitle(str);
        builder.setMessage(str2);
        builder.setPositiveButton(Constants.TEXT_OK, (DialogInterface.OnClickListener) null);
        builder.show();
    }

    public static ProgressDialog showProgress(Activity activity, CharSequence charSequence, CharSequence charSequence2, boolean z, boolean z2) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(charSequence);
        progressDialog.setMessage(charSequence2);
        progressDialog.setIndeterminate(z);
        progressDialog.setCancelable(false);
        progressDialog.setOnCancelListener(new ChargeActivity.AlixOnCancelListener(activity));
        progressDialog.show();
        return progressDialog;
    }

    public static JSONObject string2JSON(String str, String str2) {
        JSONObject jSONObject = new JSONObject();
        try {
            String[] split = str.split(str2);
            for (int i = 0; i < split.length; i++) {
                String[] split2 = split[i].split("=");
                jSONObject.put(split2[0], split[i].substring(split2[0].length() + 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject;
    }
}
