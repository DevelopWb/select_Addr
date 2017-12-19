/*
 * Copyright (C) 2017 nanck
 *
 * 1999 Free Software Foundation, Inc. 59 Temple Place, Suite 330, Boston,
 * MA 02111-1307 USA Everyone is > > permitted to copy and distribute verbatim copies of this license document,
 * but changing it is not allowed.
 * [This is the first released version of the Lesser GPL.
 * It also counts as the successor of the GNU Library Public License, > > version 2,
 * hence the version number 2.1.]
 */


package com.choseaddrdemo.selectAddr;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.choseaddrdemo.R;

import java.util.List;

/**
 * <h3>目标</h3>
 * 1. 优化选择方式。返回结果更加优雅
 * 2. 支持使用其他 Module(如 app) 中的数据库文件
 * //
 * Context c = createPackageContext("chroya.demo", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
 * //载入这个类
 * Class clazz = c.getClassLoader().loadClass("chroya.demo.Main");
 * //新建一个实例
 * Object owner = clazz.newInstance();
 * //获取print方法，传入参数并执行
 * Object obj = clazz.getMethod("print", String.class).invoke(owner, "Hello");
 * //
 * 3. 爬虫最新行政区划数据。
 */
public class ChooserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "ChooserActivity";

    public static final String ART_PARAM1 = "area";
    public static final String ART_LAST_CONTEXT = "last_context";
    public static final String ART_ADDRESS = "address";
    public static final String ART_CITYINFO = "cityInfo";
    public static final String ACTION = "broadcast_action";

    public static String sAddress = "";
    public static String sCityId = "";

    private Area mArea;
    private String lastContextPackageName;
    private Context mLastContext;

    private AreaSelectorAdapter adapter;
    private ImageView mTopLeftImageIv;
    /**
     * 标题
     */
    private TextView mTopTitleTv;

    public static void start(Context context, @Nullable Area area) {
        Intent intent = new Intent(context, ChooserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ART_PARAM1, area);
        bundle.putString(ART_LAST_CONTEXT, context.getClass().getPackage().getName());
        Log.d(TAG, "Last context package name:" + context.getClass().getPackage().getName());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_addr);
        initView();


        ListView lv = (ListView) findViewById(R.id.lv_selector);

        Bundle bundle = getIntent().getExtras();
        mArea = bundle.getParcelable(ART_PARAM1);
        lastContextPackageName = bundle.getString(ART_LAST_CONTEXT);

//        mLastContext= createPackageContext("chroya.demo", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
        try {
            mLastContext = createPackageContext(lastContextPackageName, (Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Open last context name not found.");
        }

        List<Area> list = null;
//        //显示指定城市的信息（1代表北京）
//        if (mArea == null) {
//            list = new AreaDAO(this).fetchSubAreaByFatherId(1);
//        }else{
//            sAddress = sAddress + mArea.getName() + " ";
//            list = new AreaDAO(this).fetchSubAreaByFatherId(mArea.getId());
//        }
        //显示所有城市信息
        if (mArea == null) {
            list = new AreaDAO(this).fetchSubAreaByFatherId(1);
        } else {
            sAddress = sAddress + mArea.getName() + " ";
            sCityId = sCityId + mArea.getInfoId() + ",";
            list = new AreaDAO(this).fetchSubAreaByFatherId(mArea.getInfoId());
        }
        adapter = new AreaSelectorAdapter(this, list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);
        Utils.getInstance().put(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBack();
    }

    private void onBack() {
        if (mArea != null) {
            int end = sAddress.length() - (mArea.getName().length() + 1);
            // FIXME: StringIndexOutOfBoundsException 12-05 04:49:05.764 2256-2256/? E/MessageQueue-JNI: java.lang.StringIndexOutOfBoundsException: length=0; regionStart=0; regionLength=-3
            sAddress = sAddress.substring(0, end);
            if (mArea.getLevel() == 1) {
                sAddress = "";
            }
        }
    }

    private boolean isSelected = false;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (isSelected) {
            return;
        }
        Area area = (Area) adapter.getItem(i);
        if (area.getLevel() < 4) {
            start(this, (Area) adapter.getItem(i));
        } else {
            sAddress = sAddress + area.getName();
            sCityId = sCityId + area.getInfoId();
            Intent intent = new Intent();
            intent.setAction(ACTION);
            intent.putExtra(ART_ADDRESS, sAddress);
            intent.putExtra(ART_CITYINFO, sCityId);
            sendBroadcast(intent);
            sAddress = "";
            sCityId = "";
            Utils.getInstance().clearAll();
        }
        isSelected = true;
    }


    @Override
    protected void onStop() {
        super.onStop();
        isSelected = false;
    }

    private void initView() {
        mTopLeftImageIv = (ImageView) findViewById(R.id.top_left_image_iv);
        mTopTitleTv = (TextView) findViewById(R.id.top_title_tv);
        mTopTitleTv.setText("选择地址");
        mTopLeftImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}