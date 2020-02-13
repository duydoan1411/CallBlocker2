package com.DD141.callblocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CallBlockerListener extends BroadcastReceiver {

    private List<Contact> contactList = new ArrayList<Contact>();
    private ContactDatabase contactDatabase;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER) != null){
            if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                contactDatabase = ContactDatabase.getInstance(context);
                contactList = contactDatabase.contactDAO().getAllContactNonLiveData();
                String numberIncoming = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                numberIncoming = numberIncoming.length() >= 10 ?
                        numberIncoming.substring(numberIncoming.length() - 9) : numberIncoming;
                for (Contact i : contactList) {
                    String numberBL = i.getNumber().length() >= 10 ?
                            i.getNumber().substring(i.getNumber().length() - 9) : i.getNumber();
                    if (numberBL.equals(numberIncoming)) {

                        ITelephony telephonyService;
                        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                        String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

                        if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
                            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                            try {
                                Method m = tm.getClass().getDeclaredMethod("getITelephony");

                                m.setAccessible(true);
                                telephonyService = (ITelephony) m.invoke(tm);

  //                              if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                                    telephonyService.endCall();
//                                    Toast.makeText(context, "Đã chặn cuộc gọi từ: " + number, Toast.LENGTH_LONG).show();
//                                } else
//                                    Toast.makeText(context, "Không thể chặn cuộc gọi với phiên bản Android lớn hơn 8.0", Toast.LENGTH_LONG)
//                                            .show();

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
                        break;
                    }
                }
            }
        }
    }
}
