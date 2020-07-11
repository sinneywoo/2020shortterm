package com.promise.memo.DB.roomdb.dao;

import com.promise.memo.DB.roomdb.bean.RecordBean;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface RecordDao {
    @Insert
    void insert(RecordBean recordBean);//插入数据

    @Query("select * from recordbean ")
        //获取所有数据
    List<RecordBean> getAllRecord();

    @Query("select * from recordbean where month = :month")
        //获取所有数据
    List<RecordBean> getAllRecord(String month);

    @Query("select sum(money) from recordbean where type =:typpe")
        //通过类型获取所有数据
    int getTotalMoney(String typpe);


    @Query("select sum(money) from recordbean where type =:type and month=:month")
        //通过类型获取所有数据
    int getTotalMoney(String type, String month);
}
