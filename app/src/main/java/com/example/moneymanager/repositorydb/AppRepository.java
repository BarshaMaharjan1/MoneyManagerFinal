package com.example.moneymanager.repositorydb;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.moneymanager.dao.ManagerDao;
import com.example.moneymanager.dao.RecordsDao;
import com.example.moneymanager.db.AppDatabase;
import com.example.moneymanager.pojos.Manager;
import com.example.moneymanager.pojos.Records;

import java.util.List;

public class AppRepository {
    private ManagerDao managerDao;
    private RecordsDao recordsDao;
    public AppRepository(Application application)
    {
        AppDatabase db=AppDatabase.getDatabase(application);
        managerDao=db.managerDao();
       recordsDao = db.recordsDao();
    }

    public LiveData<List<Manager>> retrieveManagers(String type)
    {
        return managerDao.retrieveManagers(type);
    }
    public void  insert(Manager manager)
    {
        managerDao.insertManager(manager);
    }

    public void saveManager(Manager manager)
    {

    }

    public long saveRecords(Records records) {
        return recordsDao.insertRecord(records);
    }

    public LiveData<Records> fetchInitialBudget() {
        return recordsDao.getInitialBudget();
    }

    public LiveData<List<Records>> fetchAllRecords() {
        return recordsDao.fetchAllRecords();
    }

    public Manager fetchManagerDetails(int managerId) {
        return managerDao.fetchManagerDetails(managerId);
    }

    public void updateCapitalBudget(Records records) {
        recordsDao.updateCapitalBudget(records.getId(),records.getAmount());
    }

    public Records fetchInitialCapital() {
        return recordsDao.fetchInitialCapital();
    }

    public List<Records> filterRecordsByDate(String date) {
        return recordsDao.filterRecordsByDate(date);
    }

    public List<Integer> fetchDistinctManagerId() {
        return recordsDao.fetchDistinctIdFromRecords();
    }

    public List<Records> fetchRecordsByManagerId(int managerId) {
        return recordsDao.fetchRecordsUsingManagerId(managerId);
    }

    public Manager fetchManagerFromManagerId(int managerId) {
        return managerDao.fetchManagerDetails(managerId);
    }

    public void updateRecords(Records records) {
        recordsDao.updateRecords(records);
    }

    public byte[] fetchImageByte(int managerId) {
        return managerDao.fetchImageByte(managerId);
    }

    public void deleteRecords(Records records) {
        recordsDao.deleteRecords(records);
    }

    public void deleteManager() {
        managerDao.deleteAllManager();
    }

    public void deleteAllRecords() {
        recordsDao.deleteAllRecords();
    }
}
