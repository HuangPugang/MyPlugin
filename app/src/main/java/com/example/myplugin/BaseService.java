package com.example.myplugin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.base.TestBase;

import java.util.Random;

/**
 * Created by paul on 16/6/21.
 */
public class BaseService extends Service {
    private String[] names=new String[]{"小白","旺财","小黑"};
    private int[] ages=new int[]{1,2,3};
    private DogBinder dogBinder;
    public class DogBinder extends IDog.Stub {

        @Override
        public String getName() throws RemoteException {
            Random random=new Random();
            int nextInt = random.nextInt(2);
            return TestBase.getDefaultParam();
        }

        @Override
        public int getAge() throws RemoteException {
            Random random=new Random();
            int nextInt = random.nextInt(2);
            return ages[nextInt];
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dogBinder = new DogBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return dogBinder;
    }



}
