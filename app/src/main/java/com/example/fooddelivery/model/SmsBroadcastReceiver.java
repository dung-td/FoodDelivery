package com.example.fooddelivery.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    public SmsReceiverListener listener;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SmsRetriever.SMS_RETRIEVED_ACTION)) {
            Bundle extras = intent.getExtras();
            Status smsStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (smsStatus.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                    listener.OnSuccess(consentIntent);
                    break;
                case CommonStatusCodes.TIMEOUT:
                    listener.OnFailure();
                    break;
            }
        }
    }

    public interface SmsReceiverListener {
        void OnSuccess(Intent intent);
        void OnFailure();
    }
}
