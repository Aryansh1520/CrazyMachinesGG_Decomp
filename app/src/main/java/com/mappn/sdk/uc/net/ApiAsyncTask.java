package com.mappn.sdk.uc.net;

import android.app.Activity;
import android.content.Context;

import com.mappn.sdk.common.net.ApiRequestListener;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.uc.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ApiAsyncTask {
    public static final int BUSINESS_ERROR = 610;
    public static final int SC_DATA_NOT_EXIST = 225;
    public static final int SC_ENCODE_ERROR = 427;
    public static final int SC_ILLEGAL_COMMENT = 232;
    public static final int SC_ILLEGAL_USER_AGENT = 421;
    public static final int SC_SERVER_DB_ERROR = 520;
    public static final int SC_XML_ERROR = 422;
    public static final int SC_XML_PARAMS_ERROR = 423;
    public static final int TIMEOUT_ERROR = 600;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final int requestType;
    private final ApiRequestListener apiRequestListener;
    private final Object requestData;
    private final Context context;

    public ApiAsyncTask(Context context, int requestType, ApiRequestListener apiRequestListener, Object requestData) {
        this.context = context;
        this.requestType = requestType;
        this.apiRequestListener = apiRequestListener;
        this.requestData = requestData;
    }

    public void execute() {
        Future<?> future = executorService.submit(() -> {
            try {
                String urlString = Api.a[requestType];
                if (!BaseUtils.isNetworkAvailable(context)) {
                    BaseUtils.I("ApiAsyncTask", "TIMEOUT_ERROR");
                    onPostExecute(TIMEOUT_ERROR);
                    return;
                }

                HttpURLConnection connection = null;
                try {
                    URL url = new URL(urlString);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);

                    // Add headers and request body if necessary
                    // connection.setRequestProperty("Content-Type", "application/json");
                    // connection.setDoOutput(true);

                    // Write request data
                    if (requestData != null) {
                        connection.setDoOutput(true);
                        try (OutputStream os = connection.getOutputStream()) {
                            // Assuming requestData is a String; adjust as needed
                            os.write(requestData.toString().getBytes("UTF-8"));
                        }
                    }

                    int statusCode = connection.getResponseCode();
                    BaseUtils.D("ApiAsyncTask", "requestUrl " + urlString + " statusCode: " + statusCode);

                    String response;
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        StringBuilder responseBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseBuilder.append(line);
                        }
                        response = responseBuilder.toString();
                    }

                    if (statusCode == 200) {
                        Object result = ApiResponseFactory.getResponse(context, requestType, connection, response);
                        onPostExecute(result != null ? result : BUSINESS_ERROR);
                    } else {
                        onPostExecute(statusCode);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            } catch (IOException e) {
                BaseUtils.D("ApiAsyncTask", "Market API encounter the IO exception", e);
                onPostExecute(TIMEOUT_ERROR);
            } catch (Exception e) {
                BaseUtils.D("ApiAsyncTask", "Market API encounter the other exception", e);
                onPostExecute(TIMEOUT_ERROR);
            }
        });
    }

    private void onPostExecute(Object result) {
        BaseUtils.D("ApiAsyncTask", "onPostExecute");
        if (apiRequestListener == null || context instanceof Activity && ((Activity) context).isFinishing()) {
            return;
        }

        if (result == null) {
            apiRequestListener.onError(requestType, BUSINESS_ERROR);
        } else if (result instanceof Integer) {
            if ((Integer) result != 200) {
                apiRequestListener.onError(requestType, (Integer) result);
            }
        } else if (result instanceof HashMap) {
            HashMap<?, ?> hashMap = (HashMap<?, ?>) result;
            if (hashMap.containsKey(Constants.RESULT_CODE)) {
                int resultCode = (Integer) hashMap.get(Constants.RESULT_CODE);
                if (resultCode < 0) {
                    apiRequestListener.onError(requestType, resultCode);
                    return;
                }
            }
            apiRequestListener.onSuccess(requestType, result);
        } else {
            apiRequestListener.onSuccess(requestType, result);
        }
    }
}
