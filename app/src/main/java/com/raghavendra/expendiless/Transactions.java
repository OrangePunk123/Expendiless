package com.raghavendra.expendiless;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Transactions extends AppCompatActivity {

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transactions);
        updateDisplay();
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


    }

    public void UPDATE(View view) {
        super.onResume();
        Intent in = getIntent();
        String temp = in.getStringExtra("id");
        Integer id = Integer.parseInt(temp);


        DatabaseHelper mDB= new DatabaseHelper(this);

        //linking to views
        EditText expense = findViewById(R.id.expenditure);
        EditText expensedescription = findViewById(R.id.edescription);
        //getting the expense and descriptoin
        String myactualexpense = expense.getText().toString();
        String desc = expensedescription.getText().toString();
        desc="Bought :"+myactualexpense+" For "+desc;
        Integer expence1 = Integer.parseInt(expense.getText().toString());






        if(expense.getText().toString().equals("")|| expensedescription.getText().toString().equals("")) {
            Toast.makeText(this,"Enter All the Fields Please",Toast.LENGTH_LONG).show();
            return;
        }




        Cursor cur = mDB.db.rawQuery("SELECT PRICE,EXPENSES FROM USERS WHERE ID ="+id,null);
        cur.moveToNext();
        Integer updatedprice =cur.getInt(0);
        Integer updatedexpenses =cur.getInt(1);


        if(updatedprice <= expence1){
            Integer temp123 = updatedprice;
            updatedprice=0;
            updatedexpenses=(temp123+updatedexpenses);

        }
        else {
            updatedprice = updatedprice - expence1;
            updatedexpenses += expence1;
        }


         cur = mDB.db.rawQuery("SELECT NAME FROM USERS WHERE ID ="+id,null);
        cur.moveToNext();
        String name = cur.getString(0);

        ContentValues content = new ContentValues();
        content.put(COL_0,id);
        content.put(COL_1,name);
        content.put(COL_3,updatedprice);
        content.put(COL_4,updatedexpenses);
        content.put(COL_5,desc);
        mDB.addTransactionT1(id,content);
        mDB.addTransactionT2(content);
        Toast.makeText(this, "updated",Toast.LENGTH_LONG).show();

        Intent go = new Intent(Transactions.this,homepage.class);
        go.putExtra("id",id.toString());
        startActivity(go);

    }
}
