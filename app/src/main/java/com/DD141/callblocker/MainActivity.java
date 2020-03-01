package com.DD141.callblocker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.DD141.callblocker.ContactDatabase.Contact;
import com.DD141.callblocker.ContactDatabase.ContactDatabase;
import com.DD141.callblocker.ContactDatabase.ContactViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity implements RVContactCallLogApdater.RecyclerViewItemClickListener{

    private static final int REQUEST_SELECT_CONTACT = 1;
    private BottomNavigationView bottomNavigationView;
    private AdView mAdView;
    private FloatingActionButton fab;
    private ExtendedFloatingActionButton fabAddContact, fabAddKeyBoard, fabAddHistory;

    private boolean fabCheck = false;

    private ContactDatabase contactDatabase;
    private View view;

    public static ContactViewModel viewModel;
    private CallLogListDialog callLogListDialog;

    public static Context contextOfApplication;

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        advertisement();
        anhXa();

        contactDatabase = ContactDatabase.getInstance(getApplicationContext());


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabCheck) {
                    closeFAB();
                } else {
                    openFAB();
                }
            }
        });

        fabAddKeyBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContactFromKeyboarDialog();
                closeFAB();
            }
        });

        fabAddHistory.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                closeFAB();
                List<Contact> contactList = getCallLogs();
                Collections.reverse(contactList);

                RVContactCallLogApdater apdater = new RVContactCallLogApdater(contactList, MainActivity.this);
                callLogListDialog = new CallLogListDialog(MainActivity.this, apdater);

                callLogListDialog.show();
                callLogListDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

            }
        });

        viewModel = new ViewModelProvider(this).get(ContactViewModel.class);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmnet_container, new BlackListFragment()).commit();

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.CALL_PHONE,
        };

        //Yêu cầu quyền đọc danh bạ
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(this, PERMISSIONS)) {
                new AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert)
                        .setIcon(R.drawable.warning_icon)
                        .setTitle(getString(R.string.warning_title))
                        .setMessage(getString(R.string.warning_content))
                        .setPositiveButton(getString(R.string.agree), (dialogInterface, i) -> {
                            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
                        })
                        .setNegativeButton(R.string.disagree, (d, i) -> dialogQuit())
                        .setCancelable(false)
                        .show();
            }
        }

        fabAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectContact();
            }
        });
    }

    private void anhXa() {
        fab = findViewById(R.id.fab);
        fabAddContact = findViewById(R.id.fab_add_contact);
        fabAddKeyBoard = findViewById(R.id.fab_add_keyboard);
        fabAddHistory = findViewById(R.id.fab_add_call_history);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        view = findViewById(R.id.main_activity);
    }

    public void selectContact() {
        closeFAB();
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_CONTACT);
        }
    }


    private void advertisement() {
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private List<Contact> getCallLogs() {
        List<Contact> contactList = new ArrayList<>();
        if (checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return contactList;
        }
        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );

        while ( managedCursor.moveToNext() ) {
            String phNumber = managedCursor.getString( number );
            contactList.add(new Contact("", phNumber));
        }
        managedCursor.close();

        return contactList;
    }

    private void closeFAB(){
        fabAddContact.setVisibility(View.INVISIBLE);
        fabAddHistory.setVisibility(View.INVISIBLE);
        fabAddKeyBoard.setVisibility(View.INVISIBLE);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.add_icon));
        fabCheck = !fabCheck;
    }

    private void openFAB(){
        fabAddContact.setVisibility(View.VISIBLE);
        fabAddHistory.setVisibility(View.VISIBLE);
        fabAddKeyBoard.setVisibility(View.VISIBLE);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.close_icon));
        fabCheck = !fabCheck;
    }

    public void dialogQuit(){
        new AlertDialog.Builder(MainActivity.this,R.style.Theme_MaterialComponents_Light_Dialog_Alert)
                .setTitle(getString(R.string.close_app_title))
                .setIcon(R.drawable.warning_icon)
                .setMessage(getString(R.string.close_app_content))
                .setPositiveButton(getString(R.string.exit), (dialogInterface, i) -> {
                    finish();
                    System.exit(0);
                })
                .setCancelable(false)
                .show();
    }

    private void addContactFromKeyboarDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_from_keyboard, (ViewGroup)findViewById(R.layout.activity_main));
        EditText etName = (EditText) dialogView.findViewById(R.id.et_name);
        EditText etNumber = (EditText) dialogView.findViewById(R.id.et_phone_number);
        Button btAdd = (Button) dialogView.findViewById(R.id.bt_add_from_keyboard);

        dialog.setTitle(getResources().getString(R.string.fab_add_keyboard));

        dialog.setView(dialogView);
        AlertDialog a = dialog.create();


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etNumber.getText().toString().equals("")){
                    String name = etName.getText().toString().equals("")? "NoName" : etName.getText().toString();
                    new CheckContactAsyncTask().execute(etNumber.getText().toString(), name);
                    a.dismiss();
                }else Toast.makeText(getApplicationContext(),"Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            }
        });
        a.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null,
                    null, null);
            if (cursor.moveToFirst()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));


                if (hasPhone.equalsIgnoreCase("1")) {
                    Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                            null, null);

                    phone.moveToFirst();
                    String number = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA))
                            .replaceAll("\\D+","");
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                            .replaceAll("\n"," ");

                    new CheckContactAsyncTask().execute(number, name);
                }else {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.not_phone_number),Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    class CheckContactAsyncTask extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            List<Contact> contactList = contactDatabase.contactDAO().getAllContactNonLiveData();
            boolean check = false;
            for (Contact item: contactList)
                try {
                    if (item.getNumber().equals(strings[0])) {
                        check = true;
                        break;
                    }
                }catch (Exception e){
                    continue;
                }
            if (!check) {
                contactDatabase.contactDAO().insertContact(new Contact(strings[1], strings[0]));
            }
            return check;
        }

        @Override
        protected void onPostExecute(Boolean check) {
            super.onPostExecute(check);
            if(check){
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.phone_number_existed),Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0){
                    for (int i: grantResults){
                        if(i == PackageManager.PERMISSION_DENIED){
                            dialogQuit();
                            break;
                        }
                    }
                } else {
                    dialogQuit();
                }
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()){
                        case R.id.menu_black_list:
                            selectedFragment = new BlackListFragment();
                            break;
                        case R.id.menu_log:
                            selectedFragment = new LogFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmnet_container, selectedFragment).commit();
                    return true;
                }
            };

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void clickOnItem(Contact data) {
        new CheckContactAsyncTask().execute(data.getNumber(), data.getName());
        if (callLogListDialog != null){
            callLogListDialog.dismiss();
        }
    }
}
