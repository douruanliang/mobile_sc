package com.mobile.stu.data.repository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * @author: dourl
 * @date: 2020/4/16
 */
@Dao
public interface TaskDao {

    @Insert
    void insertTasks(Task... tasks);

    @Update
    void updateTasks(Task... tasks);

    @Query("UPDATE TASK SET status= :status  WHERE tid=:tid")
    void updateTasksByArgs(int status, int tid);

    @Delete
    void deleteTasks(Task... tasks);

    @Query("DELETE FROM TASK  WHERE env= :env AND profileId= :pid AND packageName= :packageName AND versionName= :versionName")
    void deleteTasksByArgs(int env, int pid, String packageName, String versionName);

    @Query("DELETE FROM TASK  WHERE env= :env AND profileId= :pid AND packageName= :packageName")
    void deleteTasksByArgs(int env, int pid, String packageName);

    @Query("DELETE FROM TASK  WHERE env= :env AND profileId= :pid")
    void deleteTasksByArgs(int env, int pid);

    @Query("SELECT * FROM TASK ORDER BY ID ASC")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM TASK WHERE status= :status AND  env= :env ORDER BY ID ASC")
    LiveData<List<Task>> getAllTasksByStatus(int status, int env);

    @Query("SELECT * FROM TASK WHERE env= :env AND profileId= :pid ORDER BY ID ASC")
    LiveData<List<Task>> getAllTasksByEnvAndPid(int env, int pid);

    @Query("SELECT * FROM TASK WHERE env= :env AND profileId= :pid AND packageName= :packageName ORDER BY ID ASC")
    LiveData<List<Task>> getAllTasksByEnvAndPidAnd(int env, int pid, String packageName);

    @Query("SELECT * FROM TASK  WHERE env= :env AND profileId= :pid AND packageName= :packageName AND versionName= :versionName ORDER BY ID ASC")
    LiveData<List<Task>> getTaskByPIDAndPackageNameAndVersionName(int env, int pid, String packageName, String versionName);


}
