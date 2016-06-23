package com.example.pluginapk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.base.TestBase;
import com.example.myplugin.IDog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private IDog baseService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("HPG","插件设置前:"+TestBase.getDefaultParam());
        TestBase.setParam("插件工程设置的参数");
        Log.e("HPG","插件设置后:"+TestBase.getDefaultParam());
        Log.e("HPG",getIntent().getStringExtra("haha"));
        findViewById(R.id.btn_toast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, TestBase.getDefaultParam(),Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post("hahahah");
//                gotoMaster();
                get();
            }
        });

        start();


    }
    public void get() {
        try {
            if (baseService != null) {
                StringBuilder sBuilder = new StringBuilder();
                sBuilder.append("name:" + baseService.getName());
                sBuilder.append("\nage:" + baseService.getAge());
                Toast.makeText(MainActivity.this, sBuilder.toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "请先绑定服务", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection connBase = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            baseService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // IDog.Stub.asInterface，获取接口
            baseService = IDog.Stub.asInterface(service);
        }
    };


    public void start() {
        Intent intent = new Intent();
        intent.setAction("com.example.aidlserver.BASE_SERVICE");
        intent.setPackage("com.example.myplugin");
//        bindService(createExplicitFromImplicitIntent(getApplicationContext(), intent), connBase, BIND_AUTO_CREATE);
        bindService(intent, connBase, BIND_AUTO_CREATE);
        Toast.makeText(MainActivity.this, "开始绑定服务", Toast.LENGTH_SHORT).show();
    }
    // 跳转控件
    private void gotoMaster() {
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName("com.example.myplugin","com.example.myplugin.MainActivity");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(cmp);
        intent.putExtra("haha","插件模块传递的值");
        startActivity(intent);
    }

    // Action是否允许
    public static boolean isActionAvailable(Context context, String action) {
        Intent intent = new Intent(action);
        return context.getPackageManager().resolveActivity(intent, 0) != null;
    }


    /**
     * 解决android L之后的bindservice失败的问题
     * @param context
     * @param implicitIntent
     * @return
     */
    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }
}
