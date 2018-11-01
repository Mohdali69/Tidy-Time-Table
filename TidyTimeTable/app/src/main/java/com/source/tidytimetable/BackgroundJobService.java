package com.source.tidytimetable;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class BackgroundJobService extends JobService {

    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        doBackgroundWork(params);

        return true;
    }

    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!jobCancelled) {
                    if(MainActivity.lastTimeTouch + 60000 < System.currentTimeMillis()) {
                        MainActivity.sessionTimeout();
                        return;
                    }
                    //long time = (MainActivity.lastTimeTouch + 60000 - System.currentTimeMillis()) / 1000;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                jobFinished(params, false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        jobCancelled = true;
        return true;
    }
}