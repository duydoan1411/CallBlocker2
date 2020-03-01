package com.DD141.callblocker.LogDatabase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class LogViewModel extends AndroidViewModel {

    private LogDataRepository logDataRepository;
    private LiveData<List<Log>> listLiveData;

    public LogViewModel(@NonNull Application application) {
        super(application);
        logDataRepository = new LogDataRepository(application);
        listLiveData = logDataRepository.getAllLog();
    }

    public void insert(Log log){
        logDataRepository.insert(log);
    }

    public void delete(Log log){
        logDataRepository.delete(log);
    }

    public void deleteAllLog(){
        logDataRepository.deleteAllLog();
    }

    public Log getLog(int id){
        return logDataRepository.getLog(id);
    }

    public LiveData<List<Log>> getAllLog(){
        return listLiveData;
    }

}
