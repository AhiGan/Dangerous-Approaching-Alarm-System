package com.example.dangerisapproaching;

import java.util.concurrent.FutureTask;

import com.example.dangerisapproaching.service.MyCallable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import entity.MessageType;
import entity.User;

public class RegisterActivity extends Activity{
	
	private EditText numberEdit;
	private EditText passwordEdit;
	private EditText pwdconfirmEdit;
	private Button register;
	private RadioGroup radioGroup;
	private String registerResult;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		numberEdit = (EditText)findViewById(R.id.number1);
		passwordEdit = (EditText)findViewById(R.id.password1);
		pwdconfirmEdit = (EditText)findViewById(R.id.pwdconfirm);
		radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
		register = (Button)findViewById(R.id.register1);
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String number = numberEdit.getText().toString();
				String password = passwordEdit.getText().toString();
				String pwdconfirm = pwdconfirmEdit.getText().toString();
				if(!password.equals(pwdconfirm))
				{
					Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
				}
				else
				{
					RadioButton radioButton = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
					String role = radioButton.getText().toString();
					User user = new User(number, password, role);
					
					MyCallable call = new MyCallable(MessageType.REGISTER, user);
					FutureTask<Object> task = new FutureTask<Object>(call);
					Thread thread = new Thread(task);
					thread.start();
					
					try
					{
						registerResult = (String)task.get();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					if(MessageType.REGISTER_SUCCESS.equals(registerResult))
					{
						Toast.makeText(RegisterActivity.this, "注册成功!", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
						startActivity(intent);
						finish();
					}
					else if(MessageType.REGISTER_FAILURE.equals(registerResult))
					{
						Toast.makeText(RegisterActivity.this, "该号码已被注册!", Toast.LENGTH_SHORT).show();
					}
					thread.interrupt();
				}
			}
		});
	}	
}
