package com.yh.phone.activity;

import com.yh.phone.contacts.ContactColumn;
import com.yh.phone.db.ContactsProvider;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends ListActivity {

	private static final int AddContact_ID = Menu.FIRST;
	private static final int DELEContact_ID = Menu.FIRST+2;
	private static final int EXITContact_ID = Menu.FIRST+3;
	private Cursor cursor;
	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
		//为Intent绑定数据
		Intent intent = getIntent();
		if (intent.getData() == null) {
			intent.setData(ContactsProvider.CONTENT_URI);
		}
		//设置菜单长按监听事件
		getListView().setOnCreateContextMenuListener(this);
		//设置背景图片
		getListView().setBackgroundResource(R.drawable.bg);

		show();

	}

	private void show() {
		//查询，获得所有联系人
		CursorLoader loader = new CursorLoader(getApplicationContext(),
				getIntent().getData(), ContactColumn.PROJECTION, null, null, null);
		cursor = loader.loadInBackground();
		//注册每个列表表示形式 ： 姓名 + 移动电话
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, cursor,
				new String[]{ContactColumn.NAME,ContactColumn.MOBILENUM}, 
				new int[]{android.R.id.text1,android.R.id.text2});

		setListAdapter(adapter);

	}

	/*
	 * 添加菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		//添加联系人
		menu.add(0,AddContact_ID,0,R.string.add_user)
		.setShortcut('3', 'a')
		.setIcon(R.drawable.add);
		menu.add(0,EXITContact_ID,0,R.string.exit)
		.setShortcut('4', 'd')
		.setIcon(R.drawable.exit);
		return true;
	}
	//处理菜单操作
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case AddContact_ID:
			startActivity(new Intent(Intent.ACTION_INSERT,getIntent().getData()));
			return true;
		case EXITContact_ID:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	//动态菜单的处理
	//单击的默认操作可以在这里处理
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);
		//查看联系人
		startActivity(new Intent(Intent.ACTION_VIEW,uri));
	}

	//长按触发事件
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info;
		try{
			info = (AdapterContextMenuInfo) menuInfo;
		}
		catch(ClassCastException e){
			return;
		}
		//得到长按的数据项
		Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
		if (cursor == null) {
			return;
		}
		//添加删除菜单
		menu.add(0,DELEContact_ID,0,R.string.delete_user);
	}

	//长按列表触发事件
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info;
		try{
			//获取选中项的信息
			info = (AdapterContextMenuInfo) item.getMenuInfo();
		}
		catch(ClassCastException e){
			return false;
		}

		if (item.getItemId() == DELEContact_ID) {
			//删除一个记录
			Uri noteUri = ContentUris.withAppendedId(getIntent().getData(), info.id);
			getContentResolver().delete(noteUri, null, null);
			show();
			return true;
		}
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		show();
	}

}
