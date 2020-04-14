package com.raghavendra.expendiless;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.raghavendra.expendiless.R;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

import java.math.BigInteger;
import java.util.Date;

public class homepage extends AppCompatActivity {



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

    DatabaseHelper mdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        mdb= new DatabaseHelper(context);

        updateDisplay();

        Intent in = getIntent();
        String temp = in.getStringExtra("id");
        Integer id = Integer.parseInt(temp);

        //month check

        Cursor cur = mdb.db.rawQuery("SELECT DATE FROM USERS WHERE ID ='"+id+"'",null);
        cur.moveToNext();
        long dateStarted = cur.getLong(0);
        if(dateStarted+2592000000L<= new Date().getTime()){
            reset();
        }
        else{
            drawPie(id);
        }
    }

    public void updateDisplay(){

        Intent in = getIntent();
        String temp = in.getStringExtra("id");
        Integer id = Integer.parseInt(temp);


        TextView display= findViewById(R.id.display);
        DatabaseHelper mdb = new DatabaseHelper(this);
        //EditText expense = findViewById(R.id.expenditure);

        Cursor cur = mdb.db.rawQuery("SELECT USERS.PRICE FROM USERS WHERE USERS.ID="+id,null);
        cur.moveToNext();
        Integer x =cur.getInt(0);
        display.setText(""+x);
        cur = mdb.db.rawQuery("SELECT USERS.EXPENSES FROM USERS WHERE USERS.ID="+id,null);
        display= findViewById(R.id.display2);
        cur.moveToNext();
        x =cur.getInt(0);
        display.setText(""+x);

    }


    public void reset(){
        Date d = new Date();
        mdb= new DatabaseHelper(this);

        Intent in = getIntent();
        String temp = in.getStringExtra("id");
        Integer id = Integer.parseInt(temp);

        Cursor cur = mdb.db.rawQuery("SELECT PRICE,EXPENSES FROM USERS WHERE ID ="+id,null);
        cur.moveToNext();
        Integer i=cur.getInt(0);
        Integer j= cur.getInt(1);



        ContentValues content = new ContentValues();
        content.put(COL_7,new Date().getTime());
        content.put(COL_6,d.getMonth());
        content.put(COL_3,i+j);
        content.put(COL_4,0);

        mdb.db.update(TABLE1,content,"ID=?", new String[] {String.valueOf(id)} );

        Toast.makeText(this,"Your expense sheet has been resetted",Toast.LENGTH_LONG).show();

    }


    public void drawPie(Integer id) {

        Cursor cur = mdb.db.rawQuery("SELECT PRICE,EXPENSES FROM USERS WHERE ID='"+id+"';",null);

        Integer i = cur.getColumnIndex("PRICE");
        Integer j = cur.getColumnIndex("EXPENSES");
        cur.moveToFirst();
        Integer x = cur.getInt(1);
        Integer y= cur.getInt(0);



       // Toast.makeText(homepage.this,i.toString()+j.toString(),Toast.LENGTH_LONG);
       // Log.e("indent index ",x.toString()+y.toString());


        AnimatedPieView mAnimatedPieView = findViewById(R.id.animatedPieView);
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        config.startAngle(-90)// Starting angle offset
                .addData(new SimplePieInfo(x, Color.parseColor("#ff0000"), "Expenses"))//Data (bean that implements the IPieInfo interface)
                .addData(new SimplePieInfo(y, Color.parseColor("#00ff00"), "Limit"))
                .drawText(true).strokeMode(false)

                .duration(1000).textSize(40);// draw pie animation duration

// The following two sentences can be replace directly 'mAnimatedPieView.start (config); '
        mAnimatedPieView.applyConfig(config);
        mAnimatedPieView.start();
    }


    public void showTransactions(View view) {
        Intent in = getIntent();
        String temp = in.getStringExtra("id");
        Intent go = new Intent(homepage.this,ExpenseDescription.class);
        go.putExtra("id",temp);
        startActivity(go);

    }


    public void addExpensehere(View view) {
        Intent in = getIntent();
        String temp = in.getStringExtra("id");
        Intent gohere = new Intent(homepage.this,Transactions.class);
        gohere.putExtra("id",temp);
        startActivity(gohere);
    }


    @Override
    public void onResume(){
        super.onResume();
        Intent in = getIntent();
        String temp = in.getStringExtra("id");
        Integer id = Integer.parseInt(temp);
        drawPie(id);

    }

}