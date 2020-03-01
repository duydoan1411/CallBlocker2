package com.DD141.callblocker.ContactDatabase;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import java.util.List;

public class ContactDataRepository {
    private ContactDAO contactDAO;
    private LiveData<List<Contact>> listLiveData;

    public ContactDataRepository(Application application){
        ContactDatabase contactDatabase = ContactDatabase.getInstance(application);
        contactDAO = contactDatabase.contactDAO();
        listLiveData = contactDAO.getAllContact();
    }

    public void insert(Contact contact){
        new InsertContactAsyncTask(contactDAO).execute(contact);
    }

    public void deleteAllContact(){
        new DeleteAllContactAsyncTask(contactDAO).execute();
    }

    public void delete(Contact contact){
        new DeleteContactAsyncTask(contactDAO).execute(contact);
    }

    public void update(Contact contact){
        new UpdateContactAsyncTask(contactDAO).execute(contact);
    }

    public Contact getContact(int id){
        return contactDAO.getContact(id);
    }

    public LiveData<List<Contact>> getAllContact(){

        return listLiveData;
    }

    private static class InsertContactAsyncTask extends AsyncTask<Contact, Void, Void>{

        private ContactDAO contactDAO;

        public InsertContactAsyncTask(ContactDAO contactDAO) {
            this.contactDAO = contactDAO;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDAO.insertContact(contacts[0]);
            return null;
        }
    }

    private static class DeleteAllContactAsyncTask extends AsyncTask<Void, Void, Void>{

        private ContactDAO contactDAO;

        public DeleteAllContactAsyncTask(ContactDAO contactDAO) {
            this.contactDAO = contactDAO;
        }

        @Override
        protected Void doInBackground(Void... a) {
            contactDAO.deleteAllContact();
            return null;
        }
    }

    private static class DeleteContactAsyncTask extends AsyncTask<Contact, Void, Void>{

        private ContactDAO contactDAO;

        public DeleteContactAsyncTask(ContactDAO contactDAO) {
            this.contactDAO = contactDAO;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDAO.deleteContact(contacts[0]);
            return null;
        }
    }

    private static class UpdateContactAsyncTask extends AsyncTask<Contact, Void, Void>{

        private ContactDAO contactDAO;

        public UpdateContactAsyncTask(ContactDAO contactDAO) {
            this.contactDAO = contactDAO;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDAO.updateContact(contacts[0]);
            return null;
        }
    }

}
