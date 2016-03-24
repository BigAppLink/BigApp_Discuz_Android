package com.kit.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;

import com.kit.utils.ZogUtils;

import java.util.ArrayList;

public class ServiceManager {


    public SharedPreferences sharedPrefs;

    public ServiceManager() {
    }

    public void startService(final Context context, final Service... services) {

        Thread serviceThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Service s : services) {

                    if (!ServiceUtils.isServiceRunning(context, s.getClass()
                            .getName())) {

                        Intent i = new Intent(context, s.getClass());
                        context.startService(i);

                        ZogUtils.printLog(ServiceManager.class,
                                "start service " + s.getClass().getName());
                    }

                }
            }
        });
        serviceThread.start();
    }


    public void startService(final Context context, final Class... clazzes) {

        Thread serviceThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Class c : clazzes) {

                    if (!ServiceUtils.isServiceRunning(context, c.getName())) {

                        Intent i = new Intent(context, c);
                        context.startService(i);

                        ZogUtils.printLog(ServiceManager.class,
                                "start service " + c.getName());
                    }

                }
            }
        });
        serviceThread.start();
    }

    public void bindService(final Context context, final ArrayList<Service> services,
                            final ArrayList<ServiceConnection> mConnection) {

        for (int i = 0; i < services.size(); i++) {

            if (!ServiceUtils.isServiceRunning(context, services.get(i)
                    .getClass().getName())) {

                context.bindService(new Intent(context, services.get(i)
                                .getClass()), mConnection.get(i),
                        Context.BIND_AUTO_CREATE);
            }

        }

    }

    public void stopService(final Context context, final Service... services) {
        for (Service s : services) {
            Intent service = new Intent(context, s.getClass());
            context.stopService(service);
        }

    }


    public void stopService(final Context context, final Class... clazzes) {
        for (Class c : clazzes) {
            Intent service = new Intent(context, c);
            context.stopService(service);
        }
    }

    public void restartService(final Context context, final Service... services) {
        System.out.println("ServiceManager restartService");
        stopService(context, services);

        for (Service s : services) {
            Intent service = new Intent(context, s.getClass());
            context.startService(service);
        }

    }

    public SharedPreferences getSharedPrefs() {
        return sharedPrefs;
    }

    public void setSharedPrefs(SharedPreferences sharedPrefs) {
        this.sharedPrefs = sharedPrefs;
    }

}
