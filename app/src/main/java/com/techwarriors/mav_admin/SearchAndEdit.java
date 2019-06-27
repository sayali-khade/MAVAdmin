package com.techwarriors.mav_admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.util.List;

public class SearchAndEdit extends AppCompatActivity {
    String DATABASE_URI="mongodb:// 192.168.43.71:27017";
    String DATABASE_NAME="mav";
    String COLLECTION_NAME1="driver";
    DBCollection drivercollection;
    String name,phone,id;
    BasicDBObject dbobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_and_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final EditText etutaid=(EditText)findViewById(R.id.PTdriverid_editing);
        Button bsearch=(Button)findViewById(R.id.search);

        bsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String utaid=etutaid.getText().toString();
                if (utaid.length()<10){
                    Toast.makeText(getApplicationContext(),"Enter a valid 10-digit UTA ID",Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        MongoClientURI mongoClientURI = new MongoClientURI(DATABASE_URI);
                        MongoClient mongoClient = new MongoClient(mongoClientURI);
                        DB db = mongoClient.getDB(DATABASE_NAME);
                        drivercollection = db.getCollection(COLLECTION_NAME1);
                        DBCursor cur = drivercollection.find(new BasicDBObject("d_utaid", utaid));
                        if (cur.count() == 0) {
                            Toast.makeText(getApplicationContext(), "No Driver found!", Toast.LENGTH_LONG).show();

                        } else {
                            while (cur.hasNext()) {
                                dbobj = (BasicDBObject) cur.next();
                                //name=((BasicDBObject) cur.next()).getString("d_name");

                                name = dbobj.get("d_name").toString();
                                phone = dbobj.get("d_phone").toString();
                                id = dbobj.get("d_utaid").toString();

                                Intent i1 = new Intent(SearchAndEdit.this, EditDriverDetails.class);
                                //    i1.putExtra("dbobj",dbobj);

                                i1.putExtra("name", name);
                                i1.putExtra("phone", phone);

                                i1.putExtra("id", id);
//                        i1.putExtra("phone",phone);
                                startActivity(i1);

                            }


                        }

                    } catch (Exception e) {
                        Log.d("logsample", e.toString());
                    }
                }


            }
        });



    }

}
