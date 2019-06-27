package com.techwarriors.mav_admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoIncompatibleDriverException;
import com.mongodb.MongoTimeoutException;

import java.sql.DriverManager;

public class DriverSearch extends AppCompatActivity {

    String DATABASE_URI="mongodb:// 192.168.43.71:27017";
    String DATABASE_NAME="mav";
    String COLLECTION_NAME1="driver";
    DBCollection drivercollection;
    BasicDBObject basicdb;
    EditText etui;
    String utaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etui=(EditText)findViewById(R.id.PTdriverid_status);

           }

    public void onsearchclick(View view){

        try {
            MongoClientURI mongoClientURI = new MongoClientURI(DATABASE_URI);
            MongoClient mongoClient = new MongoClient(mongoClientURI);
            DB db = mongoClient.getDB(DATABASE_NAME);
            utaid=etui.getText().toString();
            if (utaid.length()<10){
                Toast.makeText(getApplicationContext(),"Enter a valid 10-digit UTA ID",Toast.LENGTH_LONG).show();
            }
            else {
                drivercollection = db.getCollection(COLLECTION_NAME1);
                DBCursor cursor = drivercollection.find(new BasicDBObject("d_utaid", utaid));
                if (cursor.count() == 0) {
                    Toast.makeText(getApplicationContext(), "Account Does not Exist", Toast.LENGTH_LONG).show();
                } else {

                    while (cursor.hasNext()) {

                        basicdb = (BasicDBObject) cursor.next();

                        String status1 = basicdb.get("driver_status").toString();

                        //Toast.makeText(getApplicationContext(), "Enter Details", Toast.LENGTH_LONG).show();
                        Intent i1 = new Intent(DriverSearch.this, ChangeDriverStatus.class);
                        i1.putExtra("status1", status1);
                        i1.putExtra("utaid", utaid);
                        startActivity(i1);
                    }
                }
            }
        } catch (MongoTimeoutException mte) {
            mte.printStackTrace();
        } catch (MongoIncompatibleDriverException mide) {
            mide.printStackTrace();
        } catch (Exception exe) {
            exe.printStackTrace();
        }

    }

}


