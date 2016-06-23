package com.example.myplugin;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.base.TestBase;
import com.morgoo.droidplugin.pm.PluginManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        Log.e("HPG","设置前:"+TestBase.getDefaultParam());
        TestBase.setParam("主工程设置的参数");
        Log.e("HPG","设置后:"+TestBase.getDefaultParam());
        if (getIntent()!=null&&getIntent().hasExtra("haha"))
        Log.e("HPG",getIntent().getStringExtra("haha"));
        path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/pluginapk-debug.apk";
        PluginManager.getInstance().addServiceConnection(new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    int i = PluginManager.getInstance().installPackage(path,0);
                    Log.e("HPG",i+"");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        });


        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PluginManager.getInstance().isConnected()) {
                    Toast.makeText(MainActivity.this, "connected",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    ComponentName cmp = new ComponentName("com.example.pluginapk","com.example.pluginapk.MainActivity");
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.setComponent(cmp);
                    intent.putExtra("haha","宿主工程传递的值");
                    startActivity(intent);
                } else {

                }



            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void test(String s){
        Log.e("HPG",s);
    }
}
