package com.DD141.callblocker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CallLogListDialog extends Dialog implements View.OnClickListener {
    public CallLogListDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public CallLogListDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public Activity activity;
    public Dialog dialog;
    public Button cancel;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    public CallLogListDialog(Activity activity, RecyclerView.Adapter adapter){
        super(activity);
        this.activity = activity;
        this.adapter = adapter;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_from_call_log);

        cancel = findViewById(R.id.bt_cancel_from_call_log);
        recyclerView = findViewById(R.id.rv_call_log);
        layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt_cancel_from_call_log:
                dismiss();
                break;
        }
    }
}
