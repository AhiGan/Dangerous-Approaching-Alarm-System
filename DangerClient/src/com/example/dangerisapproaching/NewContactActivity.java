package com.example.dangerisapproaching;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

import com.example.dangerisapproaching.service.MyCallable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import entity.Contact;
import entity.MessageType;
import entity.User;

public class NewContactActivity extends Activity{
	private EditText edit_name;
	private EditText edit_number;
	private Button btn_save;
	private String newContactResult;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newcontact);
		btn_save = (Button)findViewById(R.id.btn_save);
		edit_name = (EditText)findViewById(R.id.edit_name);
		edit_number = (EditText)findViewById(R.id.edit_number);
		
		btn_save.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				String name = edit_name.getText().toString();
				String number = edit_number.getText().toString();
				if(name == null || "".equals(name) || number == null || "".equals(number))
				{
					Toast.makeText(NewContactActivity.this, "请输入姓名和电话", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Contact contact = new Contact(name, number);
					contact.setUserNumber(LoginActivity.getUser().getNumber());
					MyCallable call = new MyCallable(MessageType.NEWCONTACT, contact);
					FutureTask<Object> task = new FutureTask<Object>(call);
					Thread thread = new Thread(task);
					thread.start();				
					try
					{
						newContactResult = (String)task.get();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					if(MessageType.NEWCONTACT_SUCCESS.equals(newContactResult))
					{
						Intent intent = new Intent();
						intent.putExtra("name", name);
						intent.putExtra("number", number);
						setResult(RESULT_OK, intent);
						finish();
					}
					else if(MessageType.NEWCONTACT_FAILURE.equals(newContactResult))
					{
						Toast.makeText(NewContactActivity.this, "该用户未注册", Toast.LENGTH_SHORT).show();
					}
					thread.interrupt();
				}
			}		
		});
	}
	
}
