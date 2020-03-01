package com.DD141.callblocker.LogDatabase;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class LogDataRepository {
    private LogDAO logDAO;
    private LiveData<List<Log>> listLiveData;

    public LogDataRepository(Application application) {
        LogDatabase logDatabase = LogDatabase.getInstance(application);
        logDAO = logDatabase.logDAO();
        listLiveData = logDAO.getAllLog();
    }
    public void insert(Log log){
        new LogDataRepository.InsertLogAsyncTask(logDAO).execute(log);
    }

    public void deleteAllLog(){
        new LogDataRepository.DeleteAllLogAsyncTask(logDAO).execute();
    }

    public void delete(Log log){
        new LogDataRepository.DeleteLogAsyncTask(logDAO).execute(log);
    }

    public void update(Log log){
        new LogDataRepository.UpdateLogAsyncTask(logDAO).execute(log);
    }

    public Log getLog(int id){
        return logDAO.getLog(id);
    }

    public LiveData<List<Log>> getAllLog(){

        return listLiveData;
    }

    private static class InsertLogAsyncTask extends AsyncTask<Log, Void, Void> {

        private LogDAO logDAO;

        public InsertLogAsyncTask(LogDAO logDAO) {
            this.logDAO = logDAO;
        }

        @Override
        protected Void doInBackground(Log... logs) {
            logDAO.insertLog(logs[0]);
            return null;
        }
    }

    private static class DeleteAllLogAsyncTask extends AsyncTask<Void, Void, Void>{

        private LogDAO logDAO;

        public DeleteAllLogAsyncTask(LogDAO logDAO) {
            this.logDAO = logDAO;
        }

        @Override
        protected Void doInBackground(Void... a) {
            logDAO.deleteAllLog();
            return null;
        }
    }

    private static class DeleteLogAsyncTask extends AsyncTask<Log, Void, Void>{

        private LogDAO logDAO;

        public DeleteLogAsyncTask(LogDAO logDAO) {
            this.logDAO = logDAO;
        }

        @Override
        protected Void doInBackground(Log... logs) {
            logDAO.deleteLog(logs[0]);
            return null;
        }
    }

    private static class UpdateLogAsyncTask extends AsyncTask<Log, Void, Void>{

        private LogDAO logDAO;

        public UpdateLogAsyncTask(LogDAO logDAO) {
            this.logDAO = logDAO;
        }

        @Override
        protected Void doInBackground(Log... logs) {
            logDAO.updateLog(logs[0]);
            return null;
        }
    }
}
