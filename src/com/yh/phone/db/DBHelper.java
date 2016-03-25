package com.yh.phone.db;

import com.yh.phone.contacts.ContactColumn;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 创建数据 库  表 
 * @author yh
 *
 */
public class DBHelper extends SQLiteOpenHelper {

	//数据库名
	public static final String DATABASE_NAME = "mycontacts.db";
	//版本号
	public static final int DATABASE_VERSION = 1;
	//表名
	public static final String CONTACTS_TABLE = "contacts";
	//创建表
	private static final String DATABASE_CREATE =
			"CREATE TABLE "+CONTACTS_TABLE +
			" ("
			+ContactColumn._ID+" integer primary key autoincrement,"
			+ContactColumn.NAME+" text,"
			+ContactColumn.MOBILENUM+" text,"
			+ContactColumn.HOMENUM+" text,"
			+ContactColumn.ADDRESS+" text,"
			+ContactColumn.EMAIL+" text "
			+")";

//	public DBHelper(Context context, String name, CursorFactory factory, int version) {
//		super(context, DATABASE_NAME, null, DATABASE_VERSION);
//	}

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}
	
	//升级
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS "+CONTACTS_TABLE);
		onCreate(db);

	}

}
