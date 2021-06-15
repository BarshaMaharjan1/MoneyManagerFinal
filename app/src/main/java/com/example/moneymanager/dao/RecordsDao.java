package com.example.moneymanager.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moneymanager.pojos.Manager;
import com.example.moneymanager.pojos.Records;

import java.util.List;

@Dao
public interface RecordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRecord(Records records);

    @Query("select * from tbl_records")
    LiveData<List<Records>> getAllRecords();

    @Query("select * from tbl_records where type=:type")
    LiveData<List<Records>> getRecordsByType(String type);

    @Query("select * from tbl_records where type='initial'")
    LiveData<Records> getInitialBudget();

    @Query("select * from tbl_records where type='income' or type='expenses'")
    LiveData<List<Records>> fetchAllRecords();

    @Query("update tbl_records set amount=:amount where id=:id")
    void updateCapitalBudget(int id, String amount);

    @Query("select * from tbl_records where type='initial'")
    Records fetchInitialCapital();

    @Query("select * from tbl_records where month=:date")
    LiveData<List<Records>> filterRecordsByDate(String date);

    @Query("select distinct(managerId) from tbl_records")
    List<Integer> fetchDistinctIdFromRecords();

    @Query("select * from tbl_records where managerId=:managerId")
    List<Records> fetchRecordsUsingManagerId(int managerId);

    @Query("select * from tbl_manager where id=:managerId")
    Manager fetchManagerData(int managerId);

    @Update
    void updateRecords(Records records);

    @Delete
    void deleteRecords(Records records);

    @Query("delete from tbl_records")
    void deleteAllRecords();
}
