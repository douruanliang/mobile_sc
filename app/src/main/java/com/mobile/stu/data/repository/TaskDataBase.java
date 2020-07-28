package com.mobile.stu.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * @author: dourl
 * @date: 2020/4/16
 */
@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class TaskDataBase extends RoomDatabase {
    private static final String DB_NAME = "task.db";

    public abstract TaskDao getTaskDao();

    private static class Holder {
        private static volatile TaskDataBase INSTANCE;

        private static TaskDataBase getInstance(Context context) {
            if (INSTANCE == null) {
                synchronized (TaskDataBase.class) {
                    if (INSTANCE == null) {
                        INSTANCE = buildDatabase(context);
                    }
                }
            }
            return INSTANCE;
        }

        private static TaskDataBase buildDatabase(Context context) {
            return Room.databaseBuilder(context, TaskDataBase.class, DB_NAME)
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Log.e(DB_NAME, "task.db 数据库第一次创建成功！");
                        }

                        @Override
                        public void onOpen(@NonNull SupportSQLiteDatabase db) {
                            super.onOpen(db);
                        }
                    }).build();
        }
    }

    /**
     * 对外提供实例
     *
     * @param context
     * @return
     */
    public static TaskDataBase getTaskDataBase(Context context) {
        return Holder.getInstance(context);
    }
}
