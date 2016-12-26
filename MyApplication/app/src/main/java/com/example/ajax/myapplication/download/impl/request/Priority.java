package com.example.ajax.myapplication.download.impl.request;

public enum Priority {
    LOW(1), DEFAULT(5), HIGH(10);
    final int mPriority;

    Priority(final int pPriority) {
        mPriority = pPriority;
    }
}
