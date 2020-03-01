package com.DD141.callblocker;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.DD141.callblocker.ContactDatabase.Contact;
import com.DD141.callblocker.ContactDatabase.ContactDatabase;
import com.DD141.callblocker.ContactDatabase.ContactViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BlackListFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Contact> contactList = new ArrayList<Contact>();
    private View view;
    private ContactViewModel contactViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.black_list_fragment, container, false);

        anhXa();


        ContactDatabase database;
        database = ContactDatabase.getInstance(container.getContext());

        //contactList = database.contactDAO().getAllContact();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        RVContactApdater rvContactApdater = new RVContactApdater(new ArrayList<Contact>());
        recyclerView.setAdapter(rvContactApdater);

        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        contactViewModel.getAllContact().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                Collections.reverse(contacts);
                rvContactApdater.setData(contacts);
            }
        });

        return view;

    }

    private void anhXa(){
        recyclerView = (RecyclerView)view.findViewById(R.id.rv_black_list);
    }


}
