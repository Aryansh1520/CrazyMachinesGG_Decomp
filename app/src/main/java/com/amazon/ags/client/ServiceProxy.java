package com.amazon.ags.client;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import com.amazon.ags.api.AmazonGamesFeature;
import com.amazon.ags.api.AmazonGamesStatus;
import com.amazon.ags.client.authentication.AuthenticationServiceProxy;
import com.amazon.ags.client.util.YesNoMaybe;
import com.amazon.ags.constants.AuthorizeKeys;
import com.amazon.ags.constants.BindingKeys;
import com.amazon.ags.constants.ClientVersionConstants;
import com.amazon.ags.constants.OptInActivityConstants;
import com.amazon.ags.constants.OptInStatusKeys;
import com.amazon.ags.overlay.PopUpManager;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;

/* loaded from: classes.dex */
public class ServiceProxy implements AmazonGamesService {
    private static final String FEATURE_NAME = "AGC";
    private static final String SERVICE_CLASS_NAME = "com.amazon.ags.app.service.AmazonGamesService";
    private static final String SERVICE_PACKAGE_NAME = "com.amazon.ags.app";
    private static final String TAG = "AGC_" + ServiceProxy.class.getSimpleName();
    private final EnumSet<AmazonGamesFeature> features;
    private String sessionId;
    private final Handler statusHandler;
    private IBinder synchronousBinder = null;
    private Messenger serviceMessenger = null;
    private AmazonGamesStatus status = AmazonGamesStatus.INITIALIZING;
    private YesNoMaybe optInState = YesNoMaybe.MAYBE;
    private YesNoMaybe authorizedState = YesNoMaybe.MAYBE;
    private final AuthenticationServiceProxy authenticationProxy = new AuthenticationServiceProxy(this);

    public ServiceProxy(Handler statusHandler, EnumSet<AmazonGamesFeature> features) {
        this.statusHandler = statusHandler;
        if (features == null) {
            this.features = EnumSet.noneOf(AmazonGamesFeature.class);
        } else {
            this.features = features;
        }
    }

    @Override // com.amazon.ags.client.AmazonGamesService
    public final void bind(Context context) {
        if (isServiceAvailable(context)) {
            bindToAsynchronousService(context);
            bindToSynchronousService(context);
            registerBroadcastReceiver(context);
        } else {
            Log.e(TAG, "Amazon Games Service is not available");
            changeStatus(AmazonGamesStatus.CANNOT_BIND);
        }
    }

    private boolean isServiceAvailable(Context context) {
        Intent bindIntent = createBindIntent();
        ResolveInfo resolveInfo = context.getPackageManager().resolveService(bindIntent, 0);
        return resolveInfo != null;
    }

    @Override // com.amazon.ags.client.AmazonGamesService
    public final boolean isReady() {
        return this.status == AmazonGamesStatus.SERVICE_CONNECTED;
    }

    @Override // com.amazon.ags.client.AmazonGamesService
    public final AmazonGamesStatus getStatus() {
        return this.status;
    }

    private void bindToAsynchronousService(Context context) {
        ServiceConnection asyncConnection = new ServiceConnection() { // from class: com.amazon.ags.client.ServiceProxy.1
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName className, IBinder service) {
                ServiceProxy.this.serviceMessenger = new Messenger(service);
                ServiceProxy.this.onBindChange();
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName className) {
                ServiceProxy.this.serviceMessenger = null;
                ServiceProxy.this.onBindChange();
            }
        };
        Intent intent = createBindIntent();
        intent.setAction(BindingKeys.BIND_ASYNCHRONOUS);
        try {
            boolean bound = context.bindService(intent, asyncConnection, 1);
            Log.d(TAG, "binding result:" + bound);
        } catch (SecurityException se) {
            Log.e(TAG, "unable to bind to asynchronous service: ", se);
            changeStatus(AmazonGamesStatus.CANNOT_BIND);
        }
    }

    private void bindToSynchronousService(Context context) {
        ServiceConnection syncConnection = new ServiceConnection() { // from class: com.amazon.ags.client.ServiceProxy.2
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName className, IBinder service) {
                ServiceProxy.this.synchronousBinder = service;
                ServiceProxy.this.onBindChange();
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName className) {
                ServiceProxy.this.synchronousBinder = null;
                ServiceProxy.this.onBindChange();
            }
        };
        Intent intent = createBindIntent();
        intent.setAction(BindingKeys.BIND_SYNCHRONOUS);
        try {
            context.bindService(intent, syncConnection, 1);
        } catch (SecurityException se) {
            Log.e(TAG, "unable to bind to synchronous service: ", se);
            changeStatus(AmazonGamesStatus.CANNOT_BIND);
        }
    }

    private Intent createBindIntent() {
        Intent intent = new Intent();
        intent.setClassName(SERVICE_PACKAGE_NAME, SERVICE_CLASS_NAME);
        intent.putExtra(ClientVersionConstants.CLIENT_VERSION_KEY, ClientVersionConstants.CLIENT_VERSION_VALUE);
        return intent;
    }

    private void registerBroadcastReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter(OptInActivityConstants.OPT_IN_STATUS_BROADCAST_ACTION);
        context.registerReceiver(new AmazonGamesBroadcastReceiver(), intentFilter);
    }

    private boolean isBound() {
        if (this.synchronousBinder == null || this.serviceMessenger == null) {
            Log.d(TAG, "synchronousBinder:" + this.synchronousBinder + " serviceMessengerClient:" + this.serviceMessenger);
            return false;
        }
        Log.d(TAG, "Client is bound to service");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBindChange() {
        if (isBound() && this.status == AmazonGamesStatus.INITIALIZING) {
            if (initSession()) {
                authorize();
                return;
            } else {
                changeStatus(AmazonGamesStatus.CANNOT_BIND);
                return;
            }
        }
        if (!isBound() && this.status == AmazonGamesStatus.SERVICE_CONNECTED) {
            changeStatus(AmazonGamesStatus.SERVICE_DISCONNECTED);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeStatus(AmazonGamesStatus newStatus) {
        if (newStatus != this.status) {
            Log.d(TAG, "Changing Status from:" + this.status + " to: " + newStatus);
            this.status = newStatus;
            notifyCaller();
        }
    }

    private void notifyCaller() {
        Message statusMessage = this.statusHandler.obtainMessage();
        statusMessage.obj = this.status;
        this.statusHandler.sendMessage(statusMessage);
    }

    private boolean initSession() {
        try {
            Parcel reply = transact(24, Parcel.obtain());
            this.sessionId = reply.readString();
            Log.d(TAG, "Session Id:" + this.sessionId);
            if (TextUtils.isEmpty(this.sessionId)) {
                Log.e(TAG, "Could not obtain session");
                return false;
            }
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "Could not obtain session");
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStateChange() {
        boolean authenticated = isAuthenticated();
        if (this.authorizedState == YesNoMaybe.YES && this.optInState == YesNoMaybe.YES && isBound() && authenticated) {
            changeStatus(AmazonGamesStatus.SERVICE_CONNECTED);
        } else if (this.authorizedState == YesNoMaybe.YES && this.optInState == YesNoMaybe.NO) {
            changeStatus(AmazonGamesStatus.SERVICE_NOT_OPTED_IN);
        }
    }

    private boolean isAuthenticated() {
        return isBound() && this.authenticationProxy.isAuthenticated();
    }

    private void authorize() {
        Message msg = Message.obtain();
        Bundle msgBundle = new Bundle();
        ArrayList<String> featureList = buildFeatureList();
        msg.what = 25;
        msgBundle.putStringArrayList(BindingKeys.FEATURE_LIST, featureList);
        msg.setData(msgBundle);
        Handler authorizeHandler = new Handler() { // from class: com.amazon.ags.client.ServiceProxy.3
            @Override // android.os.Handler
            public void handleMessage(Message msg2) {
                Log.d(ServiceProxy.TAG, "Handling authorize callback");
                Bundle bundle = msg2.getData();
                String authResult = bundle.getString(BindingKeys.AUTHORIZE_RESULT_BUNDLE_KEY);
                Log.d(ServiceProxy.TAG, "authResult: " + authResult);
                if (AuthorizeKeys.AUTHORIZED.equals(authResult)) {
                    ServiceProxy.this.authorizedState = YesNoMaybe.YES;
                    ServiceProxy.this.onStateChange();
                    RemoteViews remoteViews = (RemoteViews) bundle.getParcelable(BindingKeys.WELCOME_BACK_TOAST);
                    if (remoteViews != null) {
                        Log.d(ServiceProxy.TAG, "Generating a welcome back toast");
                        PopUpManager.getInstance().executeWelcomeBackPopUp(remoteViews, 3);
                        return;
                    }
                    return;
                }
                if (AuthorizeKeys.INVALID_SESSION.equals(authResult)) {
                    ServiceProxy.this.changeStatus(AmazonGamesStatus.INVALID_SESSION);
                } else if (AuthorizeKeys.CANNOT_AUTHORIZE.equals(authResult)) {
                    ServiceProxy.this.changeStatus(AmazonGamesStatus.CANNOT_AUTHORIZE);
                } else if (AuthorizeKeys.NOT_AUTHORIZED.equals(authResult)) {
                    ServiceProxy.this.changeStatus(AmazonGamesStatus.NOT_AUTHORIZED);
                }
            }
        };
        msg.replyTo = new Messenger(authorizeHandler);
        try {
            sendMessage(msg);
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to send Message to Service: ", e);
            changeStatus(AmazonGamesStatus.CANNOT_BIND);
        }
    }

    @Override // com.amazon.ags.client.AmazonGamesService
    public final void sendMessage(Message message) throws RemoteException {
        if (message.getData() == null) {
            message.setData(new Bundle());
        }
        message.getData().putString(ClientVersionConstants.CLIENT_VERSION_KEY, ClientVersionConstants.CLIENT_VERSION_VALUE);
        message.getData().putString(BindingKeys.SESSION_ID, this.sessionId);
        if (this.serviceMessenger == null) {
            Log.e(TAG, "Service is not bound");
        } else {
            this.serviceMessenger.send(message);
        }
    }

    @Override // com.amazon.ags.client.AmazonGamesService
    public final Parcel transact(int code, Parcel data) throws RemoteException {
        Parcel reply = Parcel.obtain();
        if (this.synchronousBinder == null) {
            Log.e(TAG, "transact() was called while disconnected");
        } else {
            this.synchronousBinder.transact(code, data, reply, 0);
        }
        return reply;
    }

    private ArrayList<String> buildFeatureList() {
        ArrayList<String> res = new ArrayList<>(this.features.size());
        Iterator i$ = this.features.iterator();
        while (i$.hasNext()) {
            AmazonGamesFeature feature = (AmazonGamesFeature) i$.next();
            res.add(feature.toString());
        }
        return res;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class AmazonGamesBroadcastReceiver extends BroadcastReceiver {
        private AmazonGamesBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String optInStatus = intent.getExtras().getString(OptInActivityConstants.OPT_IN_STATUS_EXTRA_KEY);
            Log.d(ServiceProxy.TAG, "Game received broadcast from service with opt-in status [" + optInStatus + "]");
            if (OptInStatusKeys.NOT_OPTED_IN.equals(optInStatus)) {
                ServiceProxy.this.optInState = YesNoMaybe.NO;
                ServiceProxy.this.onStateChange();
            } else if (OptInStatusKeys.OPTED_IN.equals(optInStatus)) {
                ServiceProxy.this.optInState = YesNoMaybe.YES;
                ServiceProxy.this.onStateChange();
            }
        }
    }
}
