package com.example.dangerisapproaching;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.FutureTask;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;
import com.example.dangerisapproaching.MyOrientationListener.OnOrientationListener;
import com.example.dangerisapproaching.service.MyCallable;

import entity.Contact;
import entity.MessageType;
import entity.MyLatLng;
import entity.User;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	private MapView mMapView;
	public static BaiduMap mBaiduMap;
	private Button mBtnLocation;
	private Button mBtnMockNav;
	private Button mBtnRealNav;
	private Button mBtnMyContacts;
	private Button mBtnDanger;
	
	private Context context;
	
	// 定位相关
	private LocationClient mLocationClient;
	private MyLocationListener mLocationListener;
	private boolean isFirstIn = true;
	private double mLatitude;
	private double mLongtitude;
	public static LatLng mLastLocationData;//设为公共
	private LatLng mDestLocationData;
	
	// 自定义定位图标
	private BitmapDescriptor mIconLocation;
	private MyOrientationListener myOrientationListener;
	private float mCurrentX;
	private LocationMode mLocationMode;
	
	// 覆盖物相关
	private BitmapDescriptor mMarker;
	private RelativeLayout mMarkerLy;
	
	// 初始化导航相关
	private static final String APP_FOLDER_NAME = "BNSDKDemo";
	private String mSDCardPath = null;
	private boolean hasInitSuccess = false;
	public static final String ROUTE_PLAN_NODE = "routePlanNode";
	public static List<Activity> activityList = new LinkedList<Activity>();
	
	//危险监听相关
	private ArrayList<MyLatLng> dangerList = new ArrayList<MyLatLng>();
	private String insertResult = null;
	private Handler handler = new Handler();    
	private Boolean flag = false;
	private ArrayList<LatLng> latlist = new ArrayList<LatLng>();
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		
		this.context = this;
		
		initView();
		// 初始化定位
		initLocation();
		
		// 联系人相关
		mBtnMyContacts = (Button)findViewById(R.id.id_btn_mycontacts);
		mBtnMyContacts.setOnClickListener(this);
		// 导航相关
		mBtnLocation = (Button)findViewById(R.id.id_btn_location);
		mBtnMockNav = (Button)findViewById(R.id.id_btn_mocknav);
		mBtnRealNav = (Button)findViewById(R.id.id_btn_realnav);
		// 导航按钮
		mBtnLocation.setOnClickListener(this);
		mBtnMockNav.setOnClickListener(this);
		mBtnRealNav.setOnClickListener(this);
		
		// 初始化导航相关
		if(initDirs()){
			initNavi();
		}
		
		mBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener()
		{
			public void onMapLongClick(LatLng arg0)
			{
				Toast.makeText(MainActivity.this, "设置目的地成功", Toast.LENGTH_SHORT).show();
				mDestLocationData = arg0;
				addDestInfoOverLay(arg0);
			}
		});
		
		// 覆盖物相关
		initMarker();
		
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener()
		{
			@Override
			public boolean onMarkerClick(Marker marker)
			{
				Bundle extraInfo = marker.getExtraInfo();
				Info info = (Info) extraInfo.getSerializable("info");
				ImageView iv = (ImageView) mMarkerLy
						.findViewById(R.id.id_info_img);
				TextView distance = (TextView) mMarkerLy
						.findViewById(R.id.id_info_nickname);
				TextView name = (TextView) mMarkerLy
						.findViewById(R.id.id_info_name);
				TextView zan = (TextView) mMarkerLy
						.findViewById(R.id.id_info_zan);
				iv.setImageResource(info.getImgId());
				distance.setText(info.getNickname());
				name.setText(info.getName());
				zan.setText(info.getZan() + "");

				InfoWindow infoWindow;
				TextView tv = new TextView(context);
				tv.setBackgroundResource(R.drawable.location_tips);
				tv.setPadding(30, 20, 30, 50);
				tv.setText(info.getName());
				tv.setTextColor(Color.parseColor("#ffffff"));

				final LatLng latLng = marker.getPosition();
				Point p = mBaiduMap.getProjection().toScreenLocation(latLng);
				p.y -= 47;
				LatLng ll = mBaiduMap.getProjection().fromScreenLocation(p);

				infoWindow = new InfoWindow(tv, ll, 1);
				mBaiduMap.showInfoWindow(infoWindow);
				mMarkerLy.setVisibility(View.VISIBLE);
				return true;
			}
		});
		mBaiduMap.setOnMapClickListener(new OnMapClickListener()
		{

			@Override
			public boolean onMapPoiClick(MapPoi arg0)
			{
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0)
			{
				mMarkerLy.setVisibility(View.GONE);
				mBaiduMap.hideInfoWindow();
			}
		});
		
		//初始化查询数据库危险数据点
		//是子女的话就查询
	    if(LoginActivity.getUser().getRole().equals("子女"))
	    {	
	    	readDanger();
	        handler.postDelayed(maintask,8000);//立即调用， 查询是否危险
	    }
	}
	
	 private Runnable maintask =new Runnable() {    
	       public void run() {    
	           // TODOAuto-generated method stub  
	    	   if(flag == false)
	    	   {
		              //需要执行的代码  
		            LatLng currentPosition = mLastLocationData;
		            Log.v("数据点",Double.toString(latlist.get(1).longitude));
		            boolean is = false; //标注当前范围内没有湖泊
		     		double range = 5000; //安全距离，单位米
		     		//Toast.makeText(MainActivity.this, "纬度："+ currentPosition.latitude + " 经度："+ currentPosition.longitude, Toast.LENGTH_SHORT).show();
		     		for(int j = 0; j<latlist.size();j++){			
		     			//System.out.println(j+" ~ "+DistanceUtil.getDistance(currentPosition,latlist.get(j)));
		     			//Toast.makeText(MainActivity.this, j+" ~ "+DistanceUtil.getDistance(currentPosition,latlist.get(j)), Toast.LENGTH_SHORT).show();
		     			if(DistanceUtil.getDistance(currentPosition,latlist.get(j))<range){		
		     				is = true;
		     				break;
		     			}
		     		}
		     		
		     		if(is == true){
		     			Toast.makeText(MainActivity.this, "危险！", Toast.LENGTH_SHORT).show();
		     			MyCallable call = new MyCallable(MessageType.INDANGER, LoginActivity.getUser());
						FutureTask<Object> task = new FutureTask<Object>(call);
						Thread thread = new Thread(task);
						thread.start();
						try
						{
							insertResult = (String)task.get();
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						/*if(!MessageType.INSERT_SUCCESS.equals(insertResult)){
							
						}*/
		     		}else{
		     			Toast.makeText(MainActivity.this, "暂无危险！", Toast.LENGTH_SHORT).show();
		     		}
		     		 handler.postDelayed(this,3000);//设置延迟时间，此处是5秒  
	    	   }  
	       }
	    };  
	private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    String authinfo = null;
    
    private void initNavi() {

        BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            public void initSuccess() {
                Toast.makeText(MainActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                hasInitSuccess = true;
                initSetting();
            }

            public void initStart() {
                Toast.makeText(MainActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            public void initFailed() {
                Toast.makeText(MainActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }

        }, null, ttsHandler, ttsPlayStateListener);

    }
    
    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    // showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    // showToastMsg("Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 内部TTS播报状态回调接口
     */
    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
            // showToastMsg("TTSPlayStateListener : TTS play end");
        }

        @Override
        public void playStart() {
            // showToastMsg("TTSPlayStateListener : TTS play start");
        }
    };
    
    private void initSetting() {
        // BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager
                .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
    }
    
    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }
	    
	
	private void addDestInfoOverLay(LatLng destInfo)
	{
		mBaiduMap.clear();
		OverlayOptions options = new MarkerOptions().position(destInfo)//
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.myloc))//
				.zIndex(5);
		mBaiduMap.addOverlay(options);
	}
	
	private CoordinateType mCoordinateType = null;
	
	private void routeplanToNavi(boolean mock) {
		
		CoordinateType coType = CoordinateType.BD09LL;
        mCoordinateType = coType;
        if (!hasInitSuccess) {
            Toast.makeText(MainActivity.this, "还未初始化!", Toast.LENGTH_SHORT).show();
        }
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        
        sNode = new BNRoutePlanNode(mLastLocationData.longitude, mLastLocationData.latitude, "我的地点", null, coType);
        eNode = new BNRoutePlanNode(mDestLocationData.longitude, mDestLocationData.latitude, "目的地点", null, coType);
        
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, mock, new DemoRoutePlanListener(sNode));
        }
    }
	
	public class DemoRoutePlanListener implements RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            /*
             * 设置途径点以及resetEndNode会回调该接口
             */

            for (Activity ac : activityList) {

                if (ac.getClass().getName().endsWith("GuideActivity")) {

                    return;
                }
            }
            Intent intent = new Intent(MainActivity.this, GuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
        	Log.e("TAG", "onRouterPlanFailed");
            Toast.makeText(MainActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }

	private void initMarker()
	{
		mMarker = BitmapDescriptorFactory.fromResource(R.drawable.marker);
		mMarkerLy = (RelativeLayout) findViewById(R.id.id_maker_ly);
	}
	
	private void initLocation() {
		// TODO Auto-generated method stub
		mLocationMode = LocationMode.NORMAL;
		mLocationClient = new LocationClient(this);
		mLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);

		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(1000);
		mLocationClient.setLocOption(option);
		// 初始化图标
		mIconLocation = BitmapDescriptorFactory
				.fromResource(R.drawable.navi_map_gps_locked);
		myOrientationListener = new MyOrientationListener(context);

		myOrientationListener
				.setOnOrientationListener(new OnOrientationListener()
				{
					@Override
					public void onOrientationChanged(float x)
					{
						mCurrentX = x;
					}
				});
	}

	private void initView()
	{
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);
	}
	 
    @Override  
    protected void onResume() {  
        super.onResume();  
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
        mMapView.onResume();  
        }  
    @Override
	protected void onStart()
	{
		super.onStart();
		// 开启定位
		mBaiduMap.setMyLocationEnabled(true);
		if (!mLocationClient.isStarted())
			mLocationClient.start();
		// 开启方向传感器
		myOrientationListener.start();
	}
    @Override  
    protected void onPause() {  
        super.onPause();  
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        mMapView.onPause();  
        }
    @Override
	protected void onStop()
	{
		super.onStop();
		// 停止定位
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
		// 停止方向传感器
		myOrientationListener.stop();
		//flag = true;
	}
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
        mMapView.onDestroy();  
        handler.removeCallbacks(maintask);
        ContactsActivity.mTimer.cancel();
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.id_map_common:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			break;

		case R.id.id_map_site:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			break;

		case R.id.id_map_traffic:
			if (mBaiduMap.isTrafficEnabled())
			{
				mBaiduMap.setTrafficEnabled(false);
				item.setTitle("实时交通(off)");
			} else
			{
				mBaiduMap.setTrafficEnabled(true);
				item.setTitle("实时交通(on)");
			}
			break;
		case R.id.id_map_location:
			centerToMyLocation();
			break;
		case R.id.id_map_mode_common:
			mLocationMode = LocationMode.NORMAL;
			break;
		case R.id.id_map_mode_following:
			mLocationMode = LocationMode.FOLLOWING;
			break;
		case R.id.id_map_mode_compass:
			mLocationMode = LocationMode.COMPASS;
			break;
		case R.id.id_add_overlay:
			addOverlays(Info.infos);
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 添加覆盖物
	 * 
	 * @param infos
	 */
	private void addOverlays(List<Info> infos)
	{
		mBaiduMap.clear();
		LatLng latLng = null;
		Marker marker = null;
		OverlayOptions options;
		for (Info info : infos)
		{
			// 经纬度
			latLng = new LatLng(info.getLatitude(), info.getLongitude());
			// 图标
			options = new MarkerOptions().position(latLng).icon(mMarker)
					.zIndex(5);
			marker = (Marker) mBaiduMap.addOverlay(options);
			Bundle arg0 = new Bundle();
			arg0.putSerializable("info", info);
			marker.setExtraInfo(arg0);
		}

		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.setMapStatus(msu);
	}
	
	private void centerToMyLocation() {
		LatLng latLng = new LatLng(mLatitude, mLongtitude);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(msu);	
	}
	
	//读取数据库危险数据点数据
	private void readDanger()
	{
		User user = LoginActivity.getUser();
		MyCallable call = new MyCallable(MessageType.READDANGER, user);
		FutureTask<Object> task = new FutureTask<Object>(call);
		Thread thread = new Thread(task);
		thread.start();	
		try
		{
			ArrayList<MyLatLng> dangerListTemp = (ArrayList<MyLatLng>)task.get();
			for(int i=0; i<dangerListTemp.size(); i++)
			{
				dangerList.add(dangerListTemp.get(i));
			}
			
		    for(int i = 0 ;i < dangerList.size();i++)
            {
            	LatLng tmp = new LatLng(dangerList.get(i).getLatitude(),dangerList.get(i).getLongitude());
            	latlist.add(tmp);
            }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		thread.interrupt();
	}

	private class MyLocationListener implements BDLocationListener
	{
		@Override
		public void onReceiveLocation(BDLocation location)
		{
			MyLocationData data = new MyLocationData.Builder()//
					.direction(mCurrentX)//
					.accuracy(location.getRadius())//
					.latitude(location.getLatitude())//
					.longitude(location.getLongitude())//
					.build();
			mBaiduMap.setMyLocationData(data);
			// 设置自定义图标
			MyLocationConfiguration config = new MyLocationConfiguration(
					mLocationMode, true, mIconLocation);
			mBaiduMap.setMyLocationConfigeration(config);

			// 更新经纬度
			mLatitude = location.getLatitude();
			mLongtitude = location.getLongitude();
			LatLng ll = new LatLng(mLatitude, mLongtitude);
			mLastLocationData = ll;
			if (isFirstIn)
			{
				LatLng latLng = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);
				isFirstIn = false;

				Toast.makeText(context, location.getAddrStr(),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.id_btn_location:
			if(mLastLocationData != null)
			{
				MapStatusUpdate u = MapStatusUpdateFactory//
						.newLatLng(mLastLocationData);
				mBaiduMap.animateMapStatus(u);
			}
			break;
		case R.id.id_btn_mocknav:
			if(mDestLocationData == null)
			{
				Toast.makeText(MainActivity.this, "长按地图设置目标地点", Toast.LENGTH_SHORT).show();
				return;
			}
			routeplanToNavi(false);
			break;
		case R.id.id_btn_realnav:
			if(mDestLocationData == null)
			{
				Toast.makeText(MainActivity.this, "长按地图设置目标地点", Toast.LENGTH_SHORT).show();
				return;
			}
			routeplanToNavi(true);
			break;
		case R.id.id_btn_mycontacts:
			Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
			startActivity(intent);
			break;
		//case R.id.id_btn_listen:
		//	MyCallable call = new MyCallable(MessageType.READDANGER, user);
		}
	}
}