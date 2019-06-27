package com.techwarriors.mav_admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class DisplayingTripDetails extends AppCompatActivity {

    String DATABASE_URI="mongodb:// 192.168.43.71:27017";
    String DATABASE_NAME="mav";
    String COLLECTION_NAME1="trip";
    DBCollection tripcollection;
    String tripid;
    BasicDBObject basicdb;
    String start_time,end_time,no_of_riders,duration,rutaid,source,destination,status,driverid;
    TextView TVtripid,TVstarttime,TVendtime,TVnoofriders,TVduration,TVsource,TVdestination,TVstatus,TVriderid,TVdriverid;
    int dbtrip_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaying_trip_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent i1=getIntent();
        tripid=i1.getStringExtra("trip_id");
        dbtrip_id=Integer.parseInt(tripid);
        //get id's
        TVtripid=(TextView)findViewById(R.id.TVTripID);
        TVdriverid=(TextView)findViewById(R.id.TVDriverUTAID);
        TVstarttime=(TextView)findViewById(R.id.TVStartTime);
        TVendtime=(TextView)findViewById(R.id.TVEndTime);
        TVsource=(TextView)findViewById(R.id.TVSource);
        TVdestination=(TextView)findViewById(R.id.TVDestination);
        TVnoofriders=(TextView)findViewById(R.id.TVNumberofriders1);
        TVstatus=(TextView)findViewById(R.id.TVStatus);
        TVduration=(TextView)findViewById(R.id.TVDuration);
        TVriderid=(TextView)findViewById(R.id.TVRiderutaid);


        try {
            MongoClientURI mongoClientURI = new MongoClientURI(DATABASE_URI);
            MongoClient mongoClient = new MongoClient(mongoClientURI);
            DB db = mongoClient.getDB(DATABASE_NAME);

            tripcollection = db.getCollection(COLLECTION_NAME1);
            DBCursor cursor = tripcollection.find(new BasicDBObject("trip_id",dbtrip_id));
            if (cursor.count() ==0) {
                Toast.makeText(getApplicationContext(), "No trip Details found", Toast.LENGTH_LONG).show();
            } else {

                while(cursor.hasNext()) {

                    basicdb = (BasicDBObject) cursor.next();

                    start_time = basicdb.get("start_time").toString();
                    end_time = basicdb.get("end_time").toString();
                    source = basicdb.get("source").toString();
                    destination = basicdb.get("destination").toString();
                    no_of_riders = basicdb.get("no_of_riders").toString();
                    driverid = basicdb.get("d_utaid").toString();
                    status = basicdb.get("status").toString();
                    duration = basicdb.get("duration").toString();
                    rutaid = basicdb.get("r_utaid").toString();

                    mongoClient.close();

                }
            }
        } catch (MongoTimeoutException mte) {
            mte.printStackTrace();
        } catch (MongoIncompatibleDriverException mide) {
            mide.printStackTrace();
        } catch (Exception exe) {
            exe.printStackTrace();
        }

        TVtripid.setText(tripid);
        TVdriverid.setText(driverid);
        TVstarttime.setText(start_time);
        TVendtime.setText(end_time);
        TVsource.setText(source);
        TVdestination.setText(destination);
        TVnoofriders.setText(no_of_riders);
        TVstatus.setText(status);
        TVduration.setText(duration);
        TVriderid.setText(rutaid);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
