package com.raghavendra.expendiless;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.cardemulation.HostNfcFService;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

    Integer id;
    String name;
    Integer updatedprice;
    EditText budget;
    long dateStarted;

    TextView display;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        mdb= new DatabaseHelper(context);

        updateDisplay();

        Intent in = getIntent();
        String temp = in.getStringExtra("id");
        id = Integer.parseInt(temp);

        //month check

        Cursor cur = mdb.db.rawQuery("SELECT DATE FROM USERS WHERE ID ='"+id+"'",null);
        cur.moveToNext();
        dateStarted = cur.getLong(0);
        if(dateStarted+2628000000L<= new Date().getTime()){
            Toast.makeText(homepage.this,"One Month is Over...Your Price is now Resetted",Toast.LENGTH_LONG).show();
            reset();
        }
        else{

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    TextView days = findViewById(R.id.numberofdays);
                    Long temp=dateStarted;
                    temp=2628000000L-(new Date().getTime()-temp);
                    temp=temp/86400000L;
                    days.setText(Long.toString(temp)+" More Days");
                }
            },1000);

            drawPie(id);
        }

        Button resetbt=findViewById(R.id.reset);
        resetbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder rdialog= new AlertDialog.Builder(homepage.this);
                //rdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                View dialogv= getLayoutInflater().inflate(R.layout.reset_budget,null);




                Button ok=(Button)dialogv.findViewById(R.id.OkButton);
                budget=(EditText)dialogv.findViewById(R.id.newBudget);
                //updatedprice=Integer.parseInt(budget.getText().toString());
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(!budget.getText().toString().isEmpty()){
                            updatedprice=Integer.parseInt(budget.getText().toString());

                            Cursor getValues=mdb.db.rawQuery("SELECT NAME FROM USERS WHERE ID ="+id,null);
                            getValues.moveToNext();
                            name=getValues.getString(0);
                            Integer updatedexpenses=0;
                            String desc="RESETTED";

                            ContentValues content = new ContentValues();
                            content.put(COL_0,id);
                            content.put(COL_1,name);
                            content.put(COL_3,updatedprice);
                            content.put(COL_4,updatedexpenses);
                            content.put(COL_5,desc);
                            content.put(COL_6,new Date().getTime());
                            mdb.addTransactionT1(id,content);
                            mdb.addTransactionT2(content);
                            Toast.makeText(homepage.this, "updated",Toast.LENGTH_LONG).show();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    homepage.this.recreate();
                                }
                            },1000);



                            reset();

                        }
                        else{
                            Toast.makeText(homepage.this,"Please Enter Details",Toast.LENGTH_LONG).show();
                        }
                    }
                });

                rdialog.setView(dialogv);
                AlertDialog dialog= rdialog.create();
                dialog.show();
            }
        });

    }

    public void updateDisplay(){

        Intent in = getIntent();
        String temp = in.getStringExtra("id");
        id = Integer.parseInt(temp);


         display= findViewById(R.id.display);
         mdb = new DatabaseHelper(this);
        //EditText expense = findViewById(R.id.expenditure);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //For Displaying the Prices and Expenses
                Cursor cur = mdb.db.rawQuery("SELECT USERS.PRICE FROM USERS WHERE USERS.ID="+id,null);
                cur.moveToNext();
                Integer x =cur.getInt(0);
                display.setText(""+x);
                cur = mdb.db.rawQuery("SELECT USERS.EXPENSES FROM USERS WHERE USERS.ID="+id,null);
                display= findViewById(R.id.display2);
                cur.moveToNext();
                x =cur.getInt(0);
                display.setText(""+x);

                //For Displaying the number of days left
//                Cursor getDays = mdb.db.rawQuery("SELECT DATE FROM USERS WHERE ID ='"+id+"'",null);
//                cur.moveToNext();
//                long startedDay = getDays.getLong(0);
//                TextView days = findViewById(R.id.numberofdays);
//                days.setText((int) startedDay);





            }

        }, 1000);

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