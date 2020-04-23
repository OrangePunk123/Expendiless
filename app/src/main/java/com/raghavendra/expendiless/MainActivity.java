package com.raghavendra.expendiless;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class    MainActivity extends AppCompatActivity {
    DatabaseHelper mDB;
    EditText Name;
    EditText Password;
    EditText NPassword;
    //  EditText Description;
    EditText Price;
    //  EditText Expenses;


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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        mDB = new DatabaseHelper(context);
    }

    public void addRESULT(View view) {

        DatabaseHelper mdb = new DatabaseHelper(this);

        Name = findViewById(R.id.etNAME);
        Password = findViewById(R.id.etPASS);
        NPassword = findViewById(R.id.etNPASS);

        //converting Name, password and new password to string for checking
        String pass = Password.getText().toString();
        String npass = NPassword.getText().toString();
        String uname = Name.getText().toString();


        if (pass.equals(npass)) {



            Log.v("password", "EQUALS");
            Price = findViewById(R.id.etPRICE);

            if(Name.getText().toString().equals("") || Password.getText().toString().equals("") || Price.getText().toString().equals(""))
            {
                Toast.makeText(this,"Enter All the Fields Please",Toast.LENGTH_LONG).show();
                return;
            }


            //converting Price to int
            String local = Price.getText().toString();
            Integer iprice = Integer.parseInt(local);


            //converting id to int
            EditText identity =findViewById(R.id.identity);
            String temp = identity.getText().toString();
            Integer i=Integer.parseInt(temp);


            //Date
            Date d = new Date();



            //TABLE 1
            ContentValues content_TABLE1 = new ContentValues();
            content_TABLE1.put(COL_0,i);
            content_TABLE1.put(COL_1,uname);
            content_TABLE1.put(COL_2,pass);
            content_TABLE1.put(COL_3,iprice);
            content_TABLE1.put(COL_6,d.getMonth());
            content_TABLE1.put(COL_7,new Date().getTime());
            Log.e("price value",iprice.toString());



            //TABLE 2
            ContentValues content_TABLE2 = new ContentValues();
            content_TABLE2.put(COL_0,i);
            content_TABLE2.put(COL_1,uname);
            content_TABLE2.put(COL_3,iprice);
            content_TABLE2.put(COL_6,d.getDate());
            Log.e("price value",iprice.toString());


            //boolean check = mDB.addToDatabase(i,Name.getText().toString(), Password.getText().toString(), iprice);
           // boolean check1 = mDB.addToDatabase(i,Name.getText().toString(), Password.getText().toString(), iprice);

            Cursor checkifexists = mdb.db.rawQuery("SELECT * FROM USERS WHERE USERS.ID="+i,null);
           // checkifexists.moveToNext();
            if(checkifexists.getCount()>0){
                checkifexists.close();
                Toast.makeText(MainActivity.this, "ID ALREADY EXISTS PLEASE CHANGE", Toast.LENGTH_LONG).show();
                return;
            }

            long check = mDB.db.insert(TABLE1,null,content_TABLE1);
            long check1 = mDB.db.insert(TABLE2,null,content_TABLE2);



            if(check==-1 && check1==-1) {


                Toast.makeText(MainActivity.this, "NOT INSERTED TO DATABASE", Toast.LENGTH_LONG).show();


            }
            else {

                Toast.makeText(MainActivity.this, "INSERTED TO DATABASE", Toast.LENGTH_LONG).show();

                //Going to Sign in Activity
                Intent go = new Intent(MainActivity.this,sign_in.class);
                go.putExtra("id",i);
                startActivity(go);

            }
        }
        else {
            Toast.makeText(this,"Please Recheck Your Password",Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void showAllMAIN(View view) {
        Cursor cur =mDB.showALL();
        StringBuffer result = new StringBuffer();

            //TABLE 1 ID:0 NAME:1 PASSWORD:2 PRICE:3  EXPENSES:4 EXPENSESDESCRIPTION:5  DATEOFENTRY(STRING):6 DATE(DATE):7
            //TABLE 2 ID:0 NAME:1 PRICE:2 EXPENSES:3 EXPENSESDESCRIPTION:4 DATEOFENTRY:5

        while(cur.moveToNext()){
            result.append("name: "+cur.getString(1)+"\n");
            result.append("price: "+cur.getInt(3)+"\n");
            result.append("expense: "+cur.getInt(4)+"\n");
           // result.append("date:"+cur.getInt(6)+"\n");
           // result.append("date :"+cur.getInt(6)+"\n");
        }

        //Toast.makeText(MainActivity.this,result,Toast.LENGTH_LONG).show();


        AlertDialog.Builder alert= new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage(result)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                });
        alert.show();



    }

}

