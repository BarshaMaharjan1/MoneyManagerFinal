package com.example.moneymanager.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.moneymanager.pojos.Manager;
import com.example.moneymanager.pojos.Records;
import com.example.moneymanager.repositorydb.AppRepository;

import java.util.List;

public class AppViewModel extends AndroidViewModel {
    private final AppRepository mAppRepository;

    private LiveData<List<Manager>> mManager;

    public AppViewModel(@NonNull Application application) {
        super(application);
     mAppRepository=new AppRepository(application);
//     mManager=mAppRepository.retrieveManagers();
    }
    public LiveData<List<Manager>> retrieveManager(String type)
    {
        return mAppRepository.retrieveManagers(type);
    }

    public void insertManager(Manager manager) {
        mAppRepository.insert(manager);
    }

    public long saveRecords(Records records) {
        return mAppRepository.saveRecords(records);
    }

    public LiveData<Records> fetchInitialBudget() {
        return mAppRepository.fetchInitialBudget();
    }

    public LiveData<List<Records>> fetchAllRecords() {
        return mAppRepository.fetchAllRecords();
    }

    public Manager fetchManagerDetails(int managerId) {
        return mAppRepository.fetchManagerDetails(managerId);
    }

    public void updateCapitalBudget(Records records) {
        mAppRepository.updateCapitalBudget(records);
    }

    public Records fetchInitialCapital() {
        return mAppRepository.fetchInitialCapital();
    }

    public LiveData<List<Records>> filterRecordsByDate(String date) {
        return mAppRepository.filterRecordsByDate(date);
    }

    public List<Integer> fetchDistinctManagerId() {
        return mAppRepository.fetchDistinctManagerId();
    }

    public List<Records> fetchRecordsByManagerId(int managerId) {
        return mAppRepository.fetchRecordsByManagerId(managerId);
    }

    public Manager fetchManagerDataFromManagerId(int managerId) {
        return mAppRepository.fetchManagerFromManagerId(managerId);
    }

    public void updateRecords(Records records) {
        mAppRepository.updateRecords(records);
    }

    public byte[] fetchImageByte(int managerId) {
        return mAppRepository.fetchImageByte(managerId);
    }

    public void deleteRecords(Records records) {
        mAppRepository.deleteRecords(records);
    }

    public void deleteAllDatas() {
        mAppRepository.deleteManager();
        mAppRepository.deleteAllRecords();
    }
}
