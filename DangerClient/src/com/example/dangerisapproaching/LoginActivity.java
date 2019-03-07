package com.example.dangerisapproaching;

import java.util.concurrent.FutureTask;

import com.example.dangerisapproaching.service.MyCallable;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import entity.MessageType;
import entity.User;

public class LoginActivity extends Activity {
	private static User loginUser = null;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private EditText numberEdit;
	private EditText passwordEdit;
	private Button login;
	private Button register;
	private CheckBox rememberPass;
	
	public static User getUser()
	{
		return loginUser;
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		numberEdit = (EditText)findViewById(R.id.number);
		passwordEdit = (EditText)findViewById(R.id.password);
		rememberPass = (CheckBox)findViewById(R.id.remember_pass);
		login = (Button)findViewById(R.id.login);
		register = (Button)findViewById(R.id.register);
		boolean isRemember = pref.getBoolean("remember_password", false);
		if (isRemember) {
			//将账号和密码都设置在文本框中
			String number = pref.getString("number", "");
			String password = pref.getString("password", "");
			numberEdit.setText(number);
			passwordEdit.setText(password);
			rememberPass.setChecked(true);
		}
		register.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String number = numberEdit.getText().toString();
				String password = passwordEdit.getText().toString();
				User user = new User(number, password);
				
				MyCallable call = new MyCallable(MessageType.LOGIN, user);
				FutureTask<Object> task = new FutureTask<Object>(call);
				Thread thread = new Thread(task);
				thread.start();
				
				try
				{
					loginUser = (User)task.get();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				if(loginUser != null) {
					editor = pref.edit();
					if(rememberPass.isChecked()) { //检查复选框是否被选中
						editor.putBoolean("remember_password", true);
						editor.putString("number", number);
						editor.putString("password", password);
					} else {
						editor.clear();
					}
					editor.commit();
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(LoginActivity.this, "手机号或密码错误", Toast.LENGTH_SHORT).show();
				}
				thread.interrupt();
			}
		});
	}	
}
