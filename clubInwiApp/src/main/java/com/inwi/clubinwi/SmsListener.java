package com.inwi.clubinwi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsMessage;

import com.inwi.clubinwi.Utils.MyLog;
import com.inwi.clubinwi.Utils.ObservableObject;

public class SmsListener extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        /*if (ContextCompat.checkSelfPermission())
        MyLog.e("INTENT", intent.toString());

        Bundle pudsBundle = intent.getExtras();
        MyLog.e("Bundle", pudsBundle.get("pdus").toString());
        Object[] pdus = (Object[]) pudsBundle.get("pdus");
        SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[0]);
        System.out.println("OMAR " + message.getMessageBody());
        ObservableObject.getInstance().updateValue(message.getMessageBody());
        abortBroadcast();*/
    }
}