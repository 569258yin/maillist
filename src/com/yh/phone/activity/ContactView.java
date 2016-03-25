package com.yh.phone.activity;

import com.yh.phone.contacts.ContactColumn;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.DeletedContacts;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * 显示联系人信息，并进行相应的操作
 * @author yh
 *
 */
public class ContactView extends Activity {

	//姓名
	private  TextView mTextviewName;
	//手机
	private  TextView mTextviewMobile;
	//座机
	private  TextView mTextviewHome;
	//地址
	private  TextView mTextviewAddress;
	//电子邮箱
	private  TextView mTextviewEmail;
	
	private Cursor mCursor;
	private Uri mUri;
	//设置菜单的序号
	private static final int REVERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST+1;
	private static final int EDITOR_ID = Menu.FIRST+2;
	private static final int CALL_ID = Menu.FIRST+3;
	private static final int SENDSMS_ID = Menu.FIRST+4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_person);
		mUri = getIntent().getData(); 
		//初始化元素
		init();
		
		//获得并保存原始联系人的信息
		CursorLoader loader = new CursorLoader(this, mUri,
				ContactColumn.PROJECTION, null, null, null);
		mCursor = loader.loadInBackground();
		mCursor.moveToFirst();
		if (mCursor != null) {
			//读取并显示联系人
			mCursor.moveToFirst();
			
			mTextviewName.setText(mCursor.getString(ContactColumn.NAME_COLUMN));
			mTextviewMobile.setText(mCursor.getString(ContactColumn.MOBILENUM_COLUMN));
			mTextviewHome.setText(mCursor.getString(ContactColumn.HOMENUM_COLUMN));
			mTextviewAddress.setText(mCursor.getString(ContactColumn.ADDRESS_COLUMN));
			mTextviewEmail.setText(mCursor.getString(ContactColumn.EMAIL_COLUMN));
		}else{
			setTitle("错误信息");
		}
	}
	
	//添加菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		//返回
		menu.add(0, REVERT_ID, 0, R.string.revert).setShortcut('0', 'r').setIcon(R.drawable.listuser);
		//删除
		menu.add(0, DELETE_ID, 0, R.string.delete_user).setShortcut('0', 'd').setIcon(R.drawable.remove);
		//编辑联系人
		menu.add(0, EDITOR_ID, 0, R.string.editor_user).setShortcut('0', 'd').setIcon(R.drawable.edituser);
		//呼叫联系人
		menu.add(0, CALL_ID, 0, R.string.call_user).setShortcut('0', 'd')
			.setIcon(R.drawable.calluser).setTitle(this.getResources().getString(R.string.call_user)
					+mTextviewName.getText());
		//发送短信
		menu.add(0, SENDSMS_ID, 0, R.string.sendsms_user).setShortcut('0', 'd')
		.setIcon(R.drawable.sendsms).setTitle(this.getResources().getString(R.string.sendsms_user)
				+mTextviewName.getText());
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		//删除
		case DELETE_ID:
			DeletedContacts();
			finish();
			break;
		//返回
		case REVERT_ID:
			setResult(RESULT_CANCELED);
			finish();
			break;
		//编辑
		case EDITOR_ID:
			startActivity(new Intent(Intent.ACTION_EDIT,mUri));
			finish();
			break;
		//呼叫
		case CALL_ID:
			Intent call = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+mTextviewMobile.getText()));
			startActivity(call);
			break;
		//发送短信
		case SENDSMS_ID:
			Intent sms = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+mTextviewMobile.getText()));
			startActivity(sms);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	//删除联系人
	private void DeletedContacts() {
		if (mCursor != null) {
			mCursor.close();
			mCursor = null;
			getContentResolver().delete(mUri, null, null);
			setResult(RESULT_CANCELED);
		}
		
	}

	private void init() {
		mTextviewName = (TextView) findViewById(R.id.show_name);
		mTextviewMobile = (TextView) findViewById(R.id.show_phone);
		mTextviewHome = (TextView) findViewById(R.id.show_call);
		mTextviewAddress = (TextView) findViewById(R.id.show_address);
		mTextviewEmail = (TextView) findViewById(R.id.show_email);
	}
	
	
}
