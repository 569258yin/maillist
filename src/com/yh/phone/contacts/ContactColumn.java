package com.yh.phone.contacts;

import android.provider.BaseColumns;

public class ContactColumn implements BaseColumns {
	public ContactColumn() {
	}
	//列名
	public static final String NAME = "name";
	public static final String MOBILENUM = "mobileNumber";
	public static final String HOMENUM = "homeNumber";
	public static final String ADDRESS = "address";
	public static final String EMAIL = "email";
	//列索引值
	public static final int _ID_COLUMN = 0;
	public static final int NAME_COLUMN = 1;
	public static final int MOBILENUM_COLUMN = 2;
	public static final int HOMENUM_COLUMN = 3;
	public static final int ADDRESS_COLUMN = 4;
	public static final int EMAIL_COLUMN = 5;
	
	//查询结果
	public static final String[] PROJECTION = {
		_ID,
		NAME,
		MOBILENUM,
		HOMENUM,
		ADDRESS,
		EMAIL
	};
}
