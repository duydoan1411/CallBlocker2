package com.DD141.callblocker;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.DD141.callblocker.ContactDatabase.Contact;
import com.DD141.callblocker.ContactDatabase.ContactDatabase;
import com.android.internal.telephony.ITelephony;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CallBlockerListener extends BroadcastReceiver {

    private List<Contact> contactList = new ArrayList<Contact>();
    private ContactDatabase contactDatabase;
    private Context context;
    private Intent intent;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
        if (intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER) != null){
            if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                String numberIncoming = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                numberIncoming = numberIncoming.length() >= 10 ?
                        numberIncoming.substring(numberIncoming.length() - 9) : numberIncoming;
                new CheckBlockAsyncTask().execute(numberIncoming);
            }
        }
    }

    class CheckBlockAsyncTask extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... numberIncoming) {
            contactDatabase = ContactDatabase.getInstance(context);
            contactList = contactDatabase.contactDAO().getAllContactNonLiveData();
            for (Contact i : contactList) {
                String numberBL = i.getNumber().length() >= 10 ?
                        i.getNumber().substring(i.getNumber().length() - 9) : i.getNumber();
                if (numberBL.equals(numberIncoming[0])) {

                    //log here
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                ITelephony telephonyService;
                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

                if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
                    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    try {
                        @SuppressLint("SoonBlockedPrivateApi") Method m = tm.getClass().getDeclaredMethod("getITelephony");

                        m.setAccessible(true);
                        telephonyService = (ITelephony) m.invoke(tm);

                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                            telephonyService.endCall();

                            //Log in here

                            Toast.makeText(context, "Đã chặn cuộc gọi từ: " + number, Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(context, "Không thể chặn cuộc gọi với phiên bản Android lớn hơn 8.0", Toast.LENGTH_LONG)
                                    .show();

                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
