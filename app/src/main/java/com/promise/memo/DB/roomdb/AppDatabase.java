package com.promise.memo.DB.roomdb;


import com.promise.memo.DB.roomdb.bean.RecordBean;
import com.promise.memo.DB.roomdb.dao.RecordDao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RecordBean.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RecordDao recordDao();


}