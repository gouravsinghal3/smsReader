package com.vrs.smsapp.Receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;

import com.vrs.smsapp.MainClassApplication;
import com.vrs.smsapp.MessageBox;
import com.vrs.smsapp.R;

import org.json.JSONException;

public class SmsReceiver extends BroadcastReceiver {

    private String ANDROID_CHANNEL_ID = "sms_channel";

    //interface
    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //Check the sender to filter messages which we require to read

//            if (sender.equals("GADGETSAINT"))
//            {

                String messageBody = smsMessage.getMessageBody();

                Notification(context, messageBody, sender, smsMessage.getTimestampMillis());

                //Pass the message text to interface
               // mListener.messageReceived(messageBody);

            //}
        }

    }

    public void Notification(Context context, String message, String Title, long id) {
        Intent intent = new Intent(context, MessageBox.class);
        intent.putExtra("id", MainClassApplication.GetrowCount(context) +1);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 11 /* Request code */, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle(Title)
                    .setContentText(message)
                    .setChannelId(ANDROID_CHANNEL_ID)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        }
        else
        {
            notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle(Title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        }
        //notificationBuilder.f |= Notification.FLAG_AUTO_CANCEL;


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(002, notificationBuilder.build());

    }


    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}