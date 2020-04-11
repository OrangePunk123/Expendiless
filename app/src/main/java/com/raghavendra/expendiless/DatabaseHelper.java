package com.raghavendra.expendiless;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    final static String DATABASE ="expense.db";
    final static String TABLE1 ="USERS";
    final static String TABLE2="TRANSACTIONS";
    final static String COL_0 ="ID";
    final static String COL_1 ="NAME";
    final static String COL_2 ="PASSWORD";
    final static String COL_3 ="PRICE";
    final static String COL_4 ="EXPENSES";
    final static String COL_5 ="EXPENSE_DESCRIPTION";
    final static String COL_6 ="DATE_OF_ENTRY";
    final static String COL_7="DATE";
    SQLiteDatabase db;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE, null, 1);
        db = this.getWritableDatabase();
       // db.execSQL("DROP TABLE IF EXISTS "+TABLE1);
        //db.execSQL("DROP TABLE IF EXISTS "+TABLE2);
       //onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //TABLE 1


        db.execSQL("CREATE TABLE "+TABLE1+"("+COL_0+" INT PRIMARY KEY, "+COL_1+" VARCHAR(50) ,"+COL_2+" VARCHAR(30) NOT NULL,"+COL_3+" INT NOT NULL," +
               ""+COL_4+" INT DEFAULT 0," +COL_5+" VARCHAR(300),"+COL_6+" VARCHAR(50),"+COL_7+" BIGINT);");



        //TABLE 2

        db.execSQL("CREATE TABLE "+TABLE2+"("+COL_0+" INT, "+COL_1+" VARCHAR(50), "+COL_3+" INT, "+COL_4+" INT DEFAULT 0, "+COL_5+" VARCHAR(300), "+COL_6+" VARCHAR(50) );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE1);
        onCreate(db);

    }


    public Cursor showALL(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur =db.rawQuery("SELECT * FROM "+TABLE1,null);
        return  cur;
    }

    public Cursor showALLTransactions(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM TRANSACTIONS WHERE ID="+id,null);
        return cur;
    }

    // UPDATE USERS SET PRICE=PRICE-X, EXPENSES=EXPENSES+Y WHERE NAME='RAGHAVENDRA'


    public  void addTransactionT2(ContentValues content){
        SQLiteDatabase db = this.getWritableDatabase();
       // Log.e("my insert values",name+expense.toString());
        //db.rawQuery("UPDATE USERS SET PRICE="+expense+",EXPENSES="+expense+" WHERE NAME='"+name+"'",null);
        db.insert(TABLE2,null,content);
    }


    public void addTransactionT1(Integer id,ContentValues content){
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE1,content,"ID=?",new String[] {id.toString()});
    }



//DONT USE THIS METHOD FOR NOW

    public boolean addToDatabase(Integer id,String name, String password, Integer price){


        Date currenttime = Calendar.getInstance().getTime();
       // Date mytime =
        String date=currenttime.toString();
        date=date.substring(0,20);




        SQLiteDatabase db = this.getWritableDatabase();
        //table1
        ContentValues content = new ContentValues();
        content.put(COL_0,id);
        content.put(COL_1,name);
        content.put(COL_2,password);
        content.put(COL_3,price);
        content.put(COL_6,date);
        content.put(COL_7,new Date().getTime());
        long isinserted = db.insert(TABLE1,null,content);
        //table2
        ContentValues content1 = new ContentValues();
        content1.put(COL_0,id);
        content1.put(COL_1,name);
       // content1.put(COL_2,password);
        content1.put(COL_3,price);
        content1.put(COL_6,date);
        content1.put(COL_7,new Date().getTime());
        long isinserted1 = db.insert(TABLE2,null,content1);
        if(isinserted==isinserted1){
            return true;
        }
        else{
            Log.v("insert value","not inserted sorry");
            return false;
        }

    }



}
