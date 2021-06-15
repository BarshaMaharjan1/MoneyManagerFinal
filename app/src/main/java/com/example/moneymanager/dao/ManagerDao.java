package com.example.moneymanager.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moneymanager.pojos.Manager;

import java.util.List;

@Dao
public interface ManagerDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertManager(Manager manager);

//    @Query("select * from tbl_manager where title=:title and type=:type ")
//    Manager getManager(String title, String type);

    @Update
    void updateManager(Manager manager);

    @Delete
    void deleteManager(Manager manager);

    @Query("select * from tbl_manager where type=:type")
    LiveData<List<Manager>> retrieveManagers(String type);


    @Query("select * from tbl_manager where id=:managerId")
    Manager fetchManagerDetails(int managerId);

    @Query("select image from tbl_manager where id=:managerId")
    byte[] fetchImageByte(int managerId);

   @Query("delete from tbl_manager")
    void deleteAllManager();
}
