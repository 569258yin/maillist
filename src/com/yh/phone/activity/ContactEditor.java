package com.yh.phone.activity;

import com.yh.phone.contacts.ContactColumn;

import android.app.Activity;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ContactEditor extends Activity {
	//标识量
	private static final int STATE_EDIT = 0;
	private static final int STATE_INSERT = 1;
	//菜单
	private static final int REVERT_ID = Menu.FIRST;
	private static final int DISCARD_ID = Menu.FIRST+1;
	private static final int DELETE_ID = Menu.FIRST+2;
	
	private Cursor mCursor;
	private int mState;
	private Uri mUri;
	//界面元素
	private EditText nameText;
	private EditText mobilText;
	private EditText homeText;
	private EditText addressText;
	private EditText emailText;
	//按键
	private Button okButton;
	private Button cancelButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//每次只能是一种模式
		final Intent intent = getIntent();
		final String action = intent.getAction();
		//根据action的不同进行不同的操作
		//编辑联系人
		if (Intent.ACTION_EDIT.equals(action)) {
			mState = STATE_EDIT;
			mUri = intent.getData();
		}
		else if (Intent.ACTION_INSERT.equals(action)) {
			mState = STATE_INSERT;
			mUri = getContentResolver().insert(intent.getData(), null);
			if (mUri == null) {
				finish();
				return;
			}
			setResult(RESULT_OK,(new Intent()).setAction(mUri.toString()));
		}
		//其他情况
		else{
			finish();
			return;
		}
		
		setContentView(R.layout.add_person);
		//初始化
		init();
		
		//确定按钮监听事件
		okButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String text = nameText.getText().toString();
				if (text.length() == 0) {
					//如果没有输入，则将原来的记录删除
					setResult(RESULT_CANCELED);
					deletContact();
					finish();
				}else{
					//更新数据
					updateContact();
				}
			}
		});
		
		//删除按钮监听事件
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//不添加，也不保存
				setResult(RESULT_CANCELED);
				deletContact();
				finish();
			}
		});
		
		//获取并保存原始联系人的数据
		CursorLoader loader = new CursorLoader(this, mUri, 
				ContactColumn.PROJECTION, null, null, null);
		mCursor = loader.loadInBackground();
		mCursor.moveToFirst();
		if (mCursor != null) {
			//读取并显示联系人信息
			if (mState == STATE_EDIT) {
				setTitle(getText(R.string.editor_user));
			}
			else if (mState == STATE_INSERT) {
				setTitle(getText(R.string.add_user));
			}
			
			String name = mCursor.getString(ContactColumn.NAME_COLUMN);
			String mobile = mCursor.getString(ContactColumn.MOBILENUM_COLUMN);
			String home = mCursor.getString(ContactColumn.HOMENUM_COLUMN);
			String address = mCursor.getString(ContactColumn.ADDRESS_COLUMN);
			String emial = mCursor.getString(ContactColumn.EMAIL_COLUMN);
			
			//显示信息
			nameText.setText(name);
			mobilText.setText(mobile);
			homeText.setText(home);
			addressText.setText(address);
			emailText.setText(emial);
		}
		else{
			setTitle("错误信息");
		}
	}
	
	//菜单选项
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		if (mState == STATE_EDIT) {
			//返回按钮
			menu.add(0, REVERT_ID, 0, R.string.revert).setShortcut('0', 'r').setIcon(R.drawable.listuser);
			//删除按钮
			menu.add(0, DELETE_ID, 0, R.string.delete_user).setShortcut('0', 'f').setIcon(R.drawable.remove);
		}else{
			menu.add(0, DISCARD_ID, 0, R.string.revert).setShortcut('0', 'd').setIcon(R.drawable.listuser);
		}
		return true;
		
	}
	
	//菜单处理选项
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case DELETE_ID:
			deletContact();
			finish();
			break;
		case DISCARD_ID:
			cacelContact();
			finish();
			break;
		case REVERT_ID:
			finish();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	//丢失信息
	private void cacelContact() {
		if (mCursor != null) {
			deletContact();
		}
		setResult(RESULT_CANCELED);
		finish();
	}
	
	//更新变更信息
	protected void updateContact() {
		if (mCursor != null) {
			mCursor.close();
			mCursor = null;
			ContentValues values = new ContentValues();
			values.put(ContactColumn.NAME, nameText.getText().toString());
			values.put(ContactColumn.MOBILENUM, mobilText.getText().toString());
			values.put(ContactColumn.HOMENUM, homeText.getText().toString());
			values.put(ContactColumn.ADDRESS, addressText.getText().toString());
			values.put(ContactColumn.EMAIL, emailText.getText().toString());
			
			//更新数据
			getContentResolver().update(mUri, values, null, null);
			
		}
		setResult(RESULT_CANCELED);
		finish();
	}

	protected void deletContact() {
		if (mCursor != null) {
			mCursor.close();
			mCursor = null;
			getContentResolver().delete(mUri, null, null);
			nameText.setText("");
		}
		
	}

	private void init() {
		nameText = (EditText) findViewById(R.id.et_Name);
		mobilText = (EditText) findViewById(R.id.et_phone);
		homeText = (EditText) findViewById(R.id.et_call);
		addressText = (EditText) findViewById(R.id.et_address);
		emailText = (EditText) findViewById(R.id.et_email);
		okButton = (Button) findViewById(R.id.btn_save);
		cancelButton = (Button) findViewById(R.id.btn_cancle);
	}
}
