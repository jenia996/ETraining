package com.example.ajax.myapplication.download.impl.manager;

import android.os.Handler;

import com.example.ajax.myapplication.download.IRequest;
import com.example.ajax.myapplication.download.IRequestManager;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;

public class RequestManager implements IRequestManager, Runnable {

    Semaphore mSemaphore = new Semaphore(Runtime.getRuntime().availableProcessors());
    Handler mHandler = new Handler();
    private Queue mRequests;

    public RequestManager() {
       mRequests = new PriorityBlockingQueue(10);
    }


    @Override
    public void run() {

    }

    @Override
    public void addRequest(final IRequest pRequest) {

    }
}
