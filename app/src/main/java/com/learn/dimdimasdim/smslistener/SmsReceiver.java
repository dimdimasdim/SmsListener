package com.learn.dimdimasdim.smslistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

    final SmsManager smsManager = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null){
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++){
                    SmsMessage currrentMessage = getIncomingMessage(pdusObj[i], bundle);
                    String senderNum = currrentMessage.getDisplayOriginatingAddress();;
                    String message = currrentMessage.getDisplayMessageBody();

                    Intent showIntent = new Intent(context, SmsReceiverActivity.class);
                    showIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    showIntent.putExtra(SmsReceiverActivity.EXTRA_SMS_MESSAGE, message);
                    showIntent.putExtra(SmsReceiverActivity.EXTRA_SMS_NO, senderNum);
                    context.startActivity(showIntent);
                }
            }
        }catch (Exception e){
            Log.e("SmsReceiver", "Execp : ", e);
        }
    }

    private SmsMessage getIncomingMessage(Object o, Bundle bundle){
        SmsMessage currentMessage;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String format = bundle.getString("format");
            currentMessage = SmsMessage.createFromPdu((byte[]) o, format);
        }else {
            currentMessage = SmsMessage.createFromPdu((byte[]) o);
        }

        return currentMessage;
    }
}
