package com.techwarriors.mav_admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoIncompatibleDriverException;
import com.mongodb.MongoTimeoutException;

public class ChangeDriverStatus extends AppCompatActivity {

    String DATABASE_URI = "mongodb:// 192.168.43.71:27017";
    String DATABASE_NAME = "mav";
    String COLLECTION_NAME1 = "driver";
    DBCollection drivercollection;
    TextView etdutaid;
    String driverstatus;
    Switch dstatus;
    String utaid;
    String newstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_driver_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
//getting data from plain text fields
        Intent i = getIntent();
        utaid = i.getStringExtra("utaid");
        driverstatus = i.getStringExtra("status1");


        etdutaid = (TextView) findViewById(R.id.TVdriverid1);
        etdutaid.setText(utaid);
        dstatus = (Switch) findViewById(R.id.switch_status);

        if (driverstatus.equals("working")) {
            dstatus.setChecked(true);
        } else {
            dstatus.setChecked(false);
        }

        dstatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    newstatus = "working";
                } else {
                    newstatus = "notWorking";
                }
            }
        });

    }

    public void onupdateclick(View view) {

        try {
            MongoClientURI mongoClientURI = new MongoClientURI(DATABASE_URI);
            MongoClient mongoClient = new MongoClient(mongoClientURI);
            DB db = mongoClient.getDB(DATABASE_NAME);

            //connect to db
            drivercollection = db.getCollection(COLLECTION_NAME1);
            BasicDBObject setstatus = new BasicDBObject("$set", new BasicDBObject("driver_status", newstatus));
            drivercollection.update(new BasicDBObject("d_utaid", utaid), setstatus);
            Toast.makeText(getApplicationContext(),"Driver Information Updated Successfully!",Toast.LENGTH_LONG).show();
            Intent i2=new Intent(ChangeDriverStatus.this,Home.class);
            startActivity(i2);

        } catch (MongoTimeoutException mte) {
            mte.printStackTrace();
        } catch (MongoIncompatibleDriverException mide) {
            mide.printStackTrace();
        } catch (Exception exe) {
            exe.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}


