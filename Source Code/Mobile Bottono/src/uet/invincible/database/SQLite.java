package uet.invincible.database;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class SQLite extends Activity {
	public SQLiteDatabase database = null;
	public String databaseName;
	public SQLite(String name) {
		this.databaseName = name;
		database = openOrCreateDatabase(databaseName,MODE_PRIVATE,null);
		doCreateTable();
	}
	public void doDeleteDb(){
		String msg="";
		if(deleteDatabase(databaseName)==true){
			msg="Delete successful";
		} else{
			msg="Delete failed";
		}
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	
	public void doCreateTable(){
		String sql="CREATE TABLE message (";
			 sql += "id INTEGER primary key,";
			 sql += "tag INT,";
			 sql += "content TEXT";
			 sql += "time TEXT";
		database.execSQL(sql);	 
	}
	public void doDeleteAll() {
		database.delete("message", null, null);
	}
	public void doDeleteRecordTable(){
		String malop="DHTH7C";
		database.delete("tbllop","malop=?",new String[]{malop});
	}
	public void loadAllMessage(){
		Cursor c = database.query("message",null, null, null, null, null, null);
		c.moveToFirst();
		String data="";
		while(c.isAfterLast()==false){
			data+=c.getString(0)+"-"+
				  c.getString(1)+"-"+
				  c.getString(2);
			data+="\n";
			c.moveToNext();
		}
		Toast.makeText(this, data, Toast.LENGTH_LONG).show();
		c.close();
	}
	public void doInsertRecord(int id, int tag, String message, String time){
		ContentValues values=new ContentValues();
		values.put("id", id);
		values.put("tag", tag);
		values.put("content", message);
		values.put("time", time);
		String msg="";
		if(database.insert("message", null, values)==-1){
			msg="Failed to insert record";
		} else {
			msg="insert record is successful";
		}
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	public void updateLopName(String malop,String new_tenlop){
		ContentValues values=new ContentValues();
		values.put("tenlop", new_tenlop);
		String msg="";
		int ret=database.update("tbllop", values,"malop=?", new String[]{malop});
		if(ret==0){
			msg="Failed to update";
		}
		else{
			msg="updating is successful";
		}
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
}
