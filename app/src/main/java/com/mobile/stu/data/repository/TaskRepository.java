package com.mobile.stu.data.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * @author: dourl
 * @date: 2020/4/16
 */
public class TaskRepository {

    public static String TAG = "TaskRepository";
    private TaskDao mTaskDao;

    public TaskRepository(Context context) {
        TaskDataBase mDB = TaskDataBase.getTaskDataBase(context.getApplicationContext());
        mTaskDao = mDB.getTaskDao();
    }


    LiveData<List<Task>> getAllTasksLiveData(int env, int pid) {
        Log.d(TAG, TAG + "参数{env}" + env + "参数{pid}" + pid);
        return mTaskDao.getAllTasksByEnvAndPid(env, pid);
    }

    /**
     * 主要根据包名称
     *
     * @param env
     * @param pid
     * @param packageName
     * @return
     */
    LiveData<List<Task>> getAllTasksLiveData(int env, int pid, String packageName) {
        Log.d(TAG, TAG + "参数{env}" + env + "参数{pid}" + pid);
        return mTaskDao.getAllTasksByEnvAndPidAnd(env, pid, packageName);
    }

    LiveData<List<Task>> getTaskByPIDAndPackageNameAndVersionName(int env, int pid, String packageName, String versionName) {
        return mTaskDao.getTaskByPIDAndPackageNameAndVersionName(env, pid, packageName, versionName);
    }


    /**
     * 根据状态\环境获取
     *
     * @param status
     * @return
     */
    LiveData<List<Task>> getAllTasksByStatus(int status, int env) {
        return mTaskDao.getAllTasksByStatus(status, env);
    }

    /**
     * 插入
     *
     * @param tasks
     */
    void insertTasks(Task... tasks) {
        new InsertAsyncTask(mTaskDao).execute(tasks);
    }

    /**
     * 更新
     *
     * @param tasks
     */
    void updateTasks(Task... tasks) {
        new UpdateAsyncTask(mTaskDao).execute(tasks);
    }

    /**
     * 更新状态
     *
     * @param tasks
     */
    void updateStatus(Task... tasks) {
        new UpdateStatusAsyncTask(mTaskDao).execute(tasks);
    }

    void updateByPIDTasks(Task... tasks) {
        new UpdateByPIDAsyncTask(mTaskDao).execute(tasks);
    }

    static class InsertAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mTaskDao;

        InsertAsyncTask(TaskDao taskDao) {
            mTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mTaskDao.insertTasks(tasks);
            return null;
        }
    }


    /**
     * 更新操作
     */
    static class UpdateAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mTaskDao;

        public UpdateAsyncTask(TaskDao taskDao) {
            mTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            Log.d(TAG, new Gson().toJson(tasks));
            //新的网络数据 至少一个
            if (tasks.length > 0) {
                Task deleteTask = tasks[0];
                final int env = deleteTask.getEnv();
                final int pid = deleteTask.getProfileId();
                final String packageName = deleteTask.getPackageName();
                mTaskDao.deleteTasksByArgs(env, pid, packageName);
                Log.d(TAG, "执行删除" + "参数{env}" + env);
            }
            mTaskDao.insertTasks(tasks);

            return null;
        }
    }



    static class UpdateByPIDAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mTaskDao;

        public UpdateByPIDAsyncTask(TaskDao taskDao) {
            mTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            Log.d(TAG, new Gson().toJson(tasks));
            //新的网络数据 至少一个
            if (tasks.length > 0) {
                Task deleteTask = tasks[0];
                final int env = deleteTask.getEnv();
                final int pid = deleteTask.getProfileId();
                mTaskDao.deleteTasksByArgs(env, pid);
                Log.d(TAG, "执行删除" + "参数{env}" + env);
            }
            mTaskDao.insertTasks(tasks);

            return null;
        }
    }


    /**
     * 更新任务状态
     */
    static class UpdateStatusAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao mTaskDao;

        public UpdateStatusAsyncTask(TaskDao taskDao) {
            mTaskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            Log.d(TAG, new Gson().toJson(tasks));
            //新的网络数据 至少一个
            if (tasks.length > 0) {
                Task deleteTask = tasks[0];
                final int tid = deleteTask.getTid();
                final int status = deleteTask.getStatus();
                mTaskDao.updateTasksByArgs(status, tid);

            }

            return null;
        }
    }


}
