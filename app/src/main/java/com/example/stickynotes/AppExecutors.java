package com.example.stickynotes;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {
    private static AppExecutors INSTANCE;
    private final Executor mDiskIO;
    private final Executor mMainThread;


    private AppExecutors(Executor diskIO, Executor mainThread) {
        this.mDiskIO = diskIO;
        this.mMainThread = mainThread;
    }

    public static AppExecutors getInstance() {
        if (INSTANCE == null) {
            synchronized (AppExecutors.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppExecutors(
                            Executors.newSingleThreadExecutor(),
                            new MainThreadExecutor());
                }
            }
        }
        return INSTANCE;
    }

    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
