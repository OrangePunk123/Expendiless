package com.raghavendra.expendiless;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class sign_in extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private AdView mAdView;


    DatabaseHelper mDB;
    EditText Name;
    EditText Password;
    Button Login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        mDB = new DatabaseHelper(context);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
/*
        Intent in = getIntent();
        String temp = in.getStringExtra("id");
        Integer id = Integer.parseInt(temp);



        Cursor cur = mDB.db.rawQuery("SELECT NAME FROM USERS WHERE ID ="+id,null);
        cur.moveToNext();
        String nameString = cur.getString(0);



        Name = findViewById(R.id.signinName);
        Name.setText(nameString);
*/
        // Toast.makeText(sign_in.this,"Logged in ",Toast.LENGTH_LONG);
    }


    public void openActivitySignUp(View view) {
        Intent intent123 = new Intent(sign_in.this, MainActivity.class);
        startActivity(intent123);
    }


    public void loginapp(View view) {


        EditText Name=findViewById(R.id.signinName);
        String namestring=Name.getText().toString();


        EditText Password=findViewById(R.id.signinPassword);
        String passwordstring=Password.getText().toString();

        if(Name.getText().toString().equals("")|| Password.getText().toString().equals("")){

            Toast.makeText(this,"Please Enter All the Fields Above",Toast.LENGTH_LONG).show();
        Log.e("empty string try","hi");
            return;
        }






        Cursor result= mDB.db.rawQuery("SELECT ID FROM USERS WHERE NAME='"+namestring+"'AND PASSWORD = '"+passwordstring+"'",null);
        Log.e("loginAPP","came out of rawQuery");


/*


        StringBuffer sb= new StringBuffer();
        while(result.moveToNext()){
            sb.append("name:"+ result.getString(0)  +"\n");
            sb.append("password:"+ result.getString(1)+"\n");
        }
//        AlertDialog.Builder alert= new AlertDialog.Builder(this)
//                .setTitle("entry")
//                .setMessage(sb)
*/
//        alert.show();
        Log.e("loginAPP","after result");
        if(result.moveToNext()){
            Log.e("loginAPP","before intent");

            /*Intent in = getIntent();
            String temp123 = in.getStringExtra("id");
            Integer id = Integer.parseInt(temp123);
            */

            //result.moveToNext();
            Integer id = result.getInt(0);

            Intent go = new Intent(sign_in.this,homepage.class);
            go.putExtra("id",id.toString());
            startActivity(go);
        }
        else {
            Toast.makeText(sign_in.this,"Sorry Please Check Your UserName and Passord Again",Toast.LENGTH_LONG).show();
        }

    }




}
