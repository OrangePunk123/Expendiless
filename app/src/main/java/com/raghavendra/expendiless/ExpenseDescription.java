package com.raghavendra.expendiless;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ExpenseDescription extends AppCompatActivity {

    DatabaseHelper mDB;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_description);

        //creating link to database
        mDB = new DatabaseHelper(this);

        //linking with previous activity
        Intent in = getIntent();
        String temp = in.getStringExtra("id");
        Integer id = Integer.parseInt(temp);


        ArrayList myArray = new ArrayList();


        Cursor cur = mDB.showALLTransactions(id);


        while(cur.moveToNext()){
            StringBuffer sb= new StringBuffer();
            sb.append("Budget Price: "+cur.getInt(2));
            sb.append("\t Total Expense: "+cur.getInt(3));
            sb.append("\t "+cur.getString(4));
            //Toast.makeText(this,sb,Toast.LENGTH_LONG).show();
            myArray.add(sb);
        }



        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.mytextview, myArray);

        ListView lv =findViewById(R.id.mylistview);
        //lv.setBackground(Drawable.createFromPath("@drawable/newbackground"));
        lv.setAdapter(arrayAdapter);
    }
}
