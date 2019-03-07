package com.example.dangerisapproaching;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import entity.Contact;

public class ContactAdapter extends ArrayAdapter<Contact>{
	
	private int resourceId;
	
	public ContactAdapter(Context context, int textViewResourceId, List<Contact> objects)
	{
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		Contact contact = getItem(position);
		View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		ImageView avatar = (ImageView) view.findViewById(R.id.avatar1);
		TextView name = (TextView)view.findViewById(R.id.text_name);
		TextView number = (TextView)view.findViewById(R.id.text_number);
		name.setText(contact.getName());
		number.setText(contact.getNumber());
		return view;
	}
}
