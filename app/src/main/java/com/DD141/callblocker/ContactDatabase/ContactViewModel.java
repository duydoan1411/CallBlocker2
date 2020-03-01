package com.DD141.callblocker.ContactDatabase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {

    private ContactDataRepository contactDataRepository;
    private LiveData<List<Contact>> contactList;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        contactDataRepository = new ContactDataRepository(application);
        contactList = contactDataRepository.getAllContact();
    }

    public void insert(Contact contact){
        contactDataRepository.insert(contact);
    }

    public void delete(Contact contact){
        contactDataRepository.delete(contact);
    }

    public void deleteAllContact(){
        contactDataRepository.deleteAllContact();
    }

    public Contact getContact(int id){
        return contactDataRepository.getContact(id);
    }

    public LiveData<List<Contact>> getAllContact(){
        return contactList;
    }

}
