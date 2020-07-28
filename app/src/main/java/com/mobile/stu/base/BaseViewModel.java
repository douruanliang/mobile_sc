package com.mobile.stu.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

/**
 * @author: dourl
 * @date: 2020/4/5
 */
public class BaseViewModel extends AndroidViewModel {


    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public interface OnLoadStatus {
        void finish();
    }


}
