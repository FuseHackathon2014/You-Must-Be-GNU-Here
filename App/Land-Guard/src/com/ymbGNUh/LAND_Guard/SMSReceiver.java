package com.ymbGNUh.LAND_Guard;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by jkahn on 11/15/14.
 */
public class SMSReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent)
    {

        Bundle myBundle = intent.getExtras();
        SmsMessage[] messages = null;
        String strMessage = "";

        if (myBundle != null)
        {
            //get message in pdus format(protocol discription unit)
            Object [] pdus = (Object[]) myBundle.get("pdus");
            //create an array of messages
            messages = new SmsMessage[pdus.length];
            String message = "";
            for (int i = 0; i < messages.length; i++)
            {
                //Create an SmsMessage from a raw PDU.
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                String potentialJSON = messages[i].getMessageBody();
                message += potentialJSON;
            }
            String json = message.substring(message.indexOf('{'), message.length());
            json = json.replaceAll("[\\n]", "");
            Log.d("LAND", json);
            if (isJSONValid(json)) {
                Log.d("LAND", "This Is Valid JSON!");
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                Intent nID = new Intent(context, EventActivity.class);
                nID.putExtra("json", json);
                PendingIntent pi = PendingIntent.getActivity(context, 0, nID, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification.Builder mBuilder = new Notification.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Somebody Needs Your Help!")
                        .setContentText("Click for Details!")
                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .setSound(soundUri); //This sets the sound to play

                notificationManager.notify(0, mBuilder.getNotification());
            }
            //show message in a Toast
            //Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show();
        }
    }


    public boolean isJSONValid(String json) {
        try {
            new JSONParser().parse(json);
            return true;
        } catch (ParseException e) {
            //e.printStackTrace();
            return false;
        }
    }

    public boolean isJSONValidOld(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

}
