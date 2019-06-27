package com.techwarriors.mav_admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoIncompatibleDriverException;
import com.mongodb.MongoTimeoutException;

public class ConfirmDriver extends AppCompatActivity {

    String DATABASE_URI="mongodb:// 192.168.43.71:27017";
    String DATABASE_NAME="mav";
    String COLLECTION_NAME1="driver";
    DBCollection drivercollection;
    EditText etui;
    String utaid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_driver);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etui=(EditText)findViewById(R.id.PTid1);


    }

    public void onVerifydriver(View view)
    {

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
                if (cursor.count() > 0) {
                    Toast.makeText(getApplicationContext(), "Account Already Exists !", Toast.LENGTH_LONG).show();
                } else {

                    //Toast.makeText(getApplicationContext(), "Enter Details", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ConfirmDriver.this, AddDriver.class);
                    intent.putExtra("utaid", utaid);
                    startActivity(intent);
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

