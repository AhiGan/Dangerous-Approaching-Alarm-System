package com.example.dangerisapproaching;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.dangerisapproaching.service.MyCallable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import entity.Contact;
import entity.MessageType;
import entity.MyLatLng;
import entity.User;

public class ContactsActivity extends Activity{
	private List<Contact> contactList = new ArrayList<Contact>();
	ContactAdapter adapter;
	private String deleteContactResult;
	private String deleteDangerResult;
	private String dangerState;
	private User user = LoginActivity.getUser();
    public static Timer mTimer = new Timer();// 定时器
    boolean stopTimer = false;
    //int testFlag = 0;
    Contact contact;
	private Handler handler = new Handler() {  
        @SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {  
        	switch(msg.what){
        	case 1:

        		LatLng danger = null;
				MyCallable call = new MyCallable(MessageType.DANGERMONITOR, contact);
				FutureTask<Object> task = new FutureTask<Object>(call);
				Thread thread = new Thread(task);
				thread.start();

				try {

					ArrayList<MyLatLng> dangerListTemp = (ArrayList<MyLatLng>)task.get();
					danger = new LatLng(dangerListTemp.get(0).getLatitude(),dangerListTemp.get(0).getLongitude());
					if(danger.latitude == 0.0 &&danger.longitude == 0.0){

						dangerState = "CHILDOUTOFDANGER";
						
					}
					else{
						dangerState = "CHILDINDANGER";
						
					}

					Toast.makeText(ContactsActivity.this,dangerListTemp.get(0).getLatitude()+"get！", Toast.LENGTH_SHORT).show();
					
					//dangerState =(MessageType) task.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (MessageType.CHILDINDANGER.equals(dangerState)) {
					//Toast.makeText(ContactsActivity.this, contact.getNumber() + " In Danger！", Toast.LENGTH_SHORT).show();
					stopTimer = true;
					deleteDanger();
					
					//震动
					final Vibrator vibrator = (Vibrator) ContactsActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
					vibrator.vibrate(new long[]{300,500},0);
					
					//响铃
					ContactsActivity.this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
					 
					final MediaPlayer mediaPlayer = new MediaPlayer();
					 mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer player) {
							player.seekTo(0);
						}
					});
					AssetFileDescriptor file = ContactsActivity.this.getResources().openRawResourceFd(R.raw.beep);
					try {
						mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
						file.close();
						//mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME,BEEP_VOLUME);
						mediaPlayer.prepare();
					} catch (IOException ioe) {
						
					}
					mediaPlayer.start();
					
					/*try {
						mTimer.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					addDangerInfoOverLay(danger);
					
					new AlertDialog.Builder(ContactsActivity.this).setTitle("危险提示")// 设置对话框标题
					.setMessage("您的孩子正位于湖泊附近！")// 设置显示的内容
					.setPositiveButton("我知道了停止保护模式 ", new DialogInterface.OnClickListener() {// 添加确定按钮
						@Override
						public void onClick(DialogInterface dialog, int which) {// 确定按钮的响应事件
							// TODO Auto-generated method stub
							vibrator.cancel();
							mediaPlayer.stop();
							mTimer.cancel();
							dialog.cancel();
						}
					}).setNegativeButton("我知道了继续保护模式 ", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 点击“返回”后的操作,这里不设置没有任何操作
							//mTimer.notify();
							stopTimer = false;
							vibrator.cancel();
							mediaPlayer.stop();
							dialog.cancel();
						}
					}).show();// 在按键响应事件中显示此对话框
					
				}else{
					//Toast.makeText(ContactsActivity.this, contact.getNumber() + "safe！", Toast.LENGTH_SHORT).show();
				}
				thread.interrupt();
				break;
        	case 2:
        		break;
        	}
            super.handleMessage(msg);  
        }  
    };  
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts);
		adapter = new ContactAdapter(ContactsActivity.this, R.layout.contact_item, contactList);
		ListView listView = (ListView)findViewById(R.id.contacts_view);
		listView.setAdapter(adapter);
		readContacts();
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				final Contact contact1 = contactList.get(position);
				contact1.setUserNumber(LoginActivity.getUser().getNumber());
				
				new AlertDialog.Builder(ContactsActivity.this).setTitle("删除提示")// 设置对话框标题
						.setMessage("确认删除该好友？")// 设置显示的内容
						.setPositiveButton("确认", new DialogInterface.OnClickListener() {// 添加确定按钮
							@Override
							public void onClick(DialogInterface dialog, int which) {// 确定按钮的响应事件
								// TODO Auto-generated method stub
								MyCallable call = new MyCallable(MessageType.DELETECONTACT, contact1);
								FutureTask<Object> task = new FutureTask<Object>(call);
								Thread thread = new Thread(task);
								thread.start();
								try
								{
									deleteContactResult = (String)task.get();
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								if(MessageType.DELETECONTACT_SUCCESS.equals(deleteContactResult))
								{
									contactList.remove(position);
									adapter.notifyDataSetChanged();
								}
								thread.interrupt();
								dialog.cancel();
							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// 点击“返回”后的操作,这里不设置没有任何操作
								dialog.cancel();
							}
						}).show();// 在按键响应事件中显示此对话框
						
				return true;
			}});
		
		//短按对该好友开启保护模式
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				if (LoginActivity.getUser().getRole().equals("父母")) {
					contact = contactList.get(position);
					contact.setUserNumber(LoginActivity.getUser().getNumber());
					new AlertDialog.Builder(ContactsActivity.this).setTitle("开启保护监控提示")// 设置对话框标题
							.setMessage("确认开始监控保护该好友？")// 设置显示的内容
							.setPositiveButton("确认", new DialogInterface.OnClickListener() {// 添加确定按钮
								@Override
								public void onClick(DialogInterface dialog, int which) {// 确定按钮的响应事件
									// TODO Auto-generated method stub

									// 删除数据库中已有的危险
									deleteDanger();
									stopTimer = false;
									mTimer = new Timer();
									timerTask(); // 定时执行
									
									dialog.cancel();
								}
							}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// 点击“返回”后的操作,这里不设置没有任何操作
									dialog.cancel();
								}
							}).show();// 在按键响应事件中显示此对话框
				}
				
			}});
	}

	public void timerTask() {
		// 创建定时线程执行更新任务
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				Message msgTemp =new Message(); 
				if(!stopTimer){  		
					msgTemp.what = 1;
				}
				else{
					msgTemp.what = 2;
				}
				
				handler.sendMessage(msgTemp); 
				//Toast.makeText(ContactsActivity.this, "test: "+testFlag, Toast.LENGTH_SHORT).show();
				//testFlag++;
			}
		}, 3000, 3000);// 定时任务
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.contacts, menu);
		return true;
	}

	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.id_newcontact:
			Intent intent = new Intent(ContactsActivity.this, NewContactActivity.class);
			startActivityForResult(intent, 1);
			break;
		default:
		}

		return super.onOptionsItemSelected(item);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode) {
		case 1:
			if(resultCode == RESULT_OK) {
				String name = data.getStringExtra("name");
				String number = data.getStringExtra("number");
				Contact newContact = new Contact(name, number);
				contactList.add(newContact);
				adapter.notifyDataSetChanged();
			}
			break;
		default:
			break;
		}
	}
	
	public void readContacts()
	{
		MyCallable call = new MyCallable(MessageType.READCONTACT, LoginActivity.getUser());
		FutureTask<Object> task = new FutureTask<Object>(call);
		Thread thread = new Thread(task);
		thread.start();	
		try
		{
			List<Contact> contactListTemp = (ArrayList<Contact>)task.get();
			for(int i=0; i<contactListTemp.size(); i++)
			{
				contactList.add(contactListTemp.get(i));
			}
			adapter.notifyDataSetChanged();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		thread.interrupt();
	}
	
    @Override  
    protected void onDestroy() {  
        //mTimer.cancel();// 程序退出时cancel timer  
        super.onDestroy();  
    } 
    
    private void deleteDanger(){
		// 删除之前危险
		MyCallable call = new MyCallable(MessageType.DELETEDANGER, contact);
		FutureTask<Object> task = new FutureTask<Object>(call);
		Thread thread = new Thread(task);
		thread.start();
		try {
			deleteDangerResult = (String) task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (MessageType.DELETEDANGER_SUCCESS.equals(deleteDangerResult)) {
			Toast.makeText(ContactsActivity.this, contact.getNumber() + " 删除危险成功！", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(ContactsActivity.this, contact.getNumber() + "删除危险失败！", Toast.LENGTH_SHORT).show();
		}
		thread.interrupt();
    }
    
	private void addDangerInfoOverLay(LatLng destInfo)
	{
		MainActivity.mBaiduMap.clear();
		OverlayOptions options = new MarkerOptions().position(destInfo)//
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.danger))//
				.zIndex(5);
		MainActivity.mBaiduMap.addOverlay(options);
	}
	
}
