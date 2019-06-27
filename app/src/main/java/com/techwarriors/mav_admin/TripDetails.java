package com.techwarriors.mav_admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoIncompatibleDriverException;
import com.mongodb.MongoTimeoutException;

import java.util.ArrayList;

public class TripDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String DATABASE_URI="mongodb:// 192.168.43.71:27017";
    String DATABASE_NAME="mav";
    String COLLECTION_NAME1="trip";
    DBCollection tripcollection;
    EditText et_driver_id;
    Spinner sourcespinner,destinationspinner,statusspinner;
    String driverid,source,destination,status;

    String trip_id,start_time,end_time,no_of_riders,duration,rutaid;
    ArrayList<String> Trips=new ArrayList<String>();

    int cnt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //spinner code
        sourcespinner = (Spinner) findViewById(R.id.S_source);
        ArrayAdapter<CharSequence> sourceadapter = ArrayAdapter.createFromResource(this,R.array.sourcearray,android.R.layout.simple_spinner_item);
        sourceadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourcespinner.setAdapter(sourceadapter);
        sourcespinner.setSelection(0);

         destinationspinner = (Spinner) findViewById(R.id.S_destination);
        ArrayAdapter<CharSequence> destinationadapter = ArrayAdapter.createFromResource(this,R.array.destinationarray,android.R.layout.simple_spinner_item);
        destinationadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationspinner.setAdapter(destinationadapter);
        destinationspinner.setSelection(0);

        statusspinner =(Spinner)findViewById(R.id.S_status);
        ArrayAdapter<CharSequence> statusadapter=ArrayAdapter.createFromResource(this,R.array.statusarray,android.R.layout.simple_spinner_item);
        statusadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusspinner.setAdapter(statusadapter);
        statusspinner.setSelection(0);

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        et_driver_id=(EditText)findViewById(R.id.PTdriver_id_trip);


    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_viewtripdetails) {
            // Handle the camera action
        } else if (id == R.id.nav_add_drivers) {

        } else if (id == R.id.nav_editdriverinfo) {

        } else if (id == R.id.nav_changedriverstatus) {

        } else if (id == R.id.nav_logout) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onshowclick(View view)
    {
         cnt=0;
        try {
            MongoClientURI mongoClientURI = new MongoClientURI(DATABASE_URI);
            MongoClient mongoClient = new MongoClient(mongoClientURI);
            DB db = mongoClient.getDB(DATABASE_NAME);

            //getting values
            driverid=et_driver_id.getText().toString();
            source=sourcespinner.getSelectedItem().toString();
            destination=destinationspinner.getSelectedItem().toString();
            status=statusspinner.getSelectedItem().toString();

            //connect to db
            tripcollection = db.getCollection(COLLECTION_NAME1);

            //only driver id present
            if ((!driverid.isEmpty()) && (source.equals("Select Source")) && (destination.equals("Select Destination")) && (status.equals("Select Trip Status"))){
                DBCursor cursor1 = tripcollection.find(new BasicDBObject("d_utaid", driverid));
                if (cursor1.count() == 0) {
                    Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                } else {
                    while(cursor1.hasNext()) {
                        //data retrival
                        cnt++;
                        BasicDBObject dbobj1 = (BasicDBObject) cursor1.next();
                        //name=((BasicDBObject) cur.next()).getString("d_name");

                        trip_id = dbobj1.get("trip_id").toString();
                        for (int i = 0; i < cnt; i++) {
                            Trips.add(trip_id);
                            cnt--;
                        }

                        //Toast.makeText(getApplicationContext(), "Record Found", Toast.LENGTH_SHORT).show();
                        Intent i1 = new Intent(TripDetails.this, ViewTripDetails.class);
                        i1.putStringArrayListExtra("Trips", Trips);
                        startActivity(i1);
                    }
                }
                Trips.clear();
            }

            //only source is selected
           else if((!source.equals("Select Source")) && (driverid.isEmpty()) && (destination.equals("Select Destination")) &&(status.equals("Select Trip Status"))){
                DBCursor cursor2 = tripcollection.find(new BasicDBObject("source", source));
                if (cursor2.count() == 0) {
                    Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                } else {
                      while (cursor2.hasNext()) {
                            //data retrival
                            cnt++;
                            BasicDBObject dbobj2 = (BasicDBObject) cursor2.next();
                            //name=((BasicDBObject) cur.next()).getString("d_name");

                            trip_id = dbobj2.get("trip_id").toString();
                            for (int i = 0; i < cnt; i++) {
                                Trips.add(trip_id);
                                cnt--;
                            }

                           // Toast.makeText(getApplicationContext(), "Record Found", Toast.LENGTH_SHORT).show();
                            Intent i1 = new Intent(TripDetails.this, ViewTripDetails.class);
                            i1.putStringArrayListExtra("Trips", Trips);
                            startActivity(i1);

                    }
                    Trips.clear();
                }
            }

            //only destination is selected
            else if((!destination.equals("Select Destination")) && (source.equals("Select Source")) &&(status.equals("Select Trip Status")) && (driverid.isEmpty()))
            {
                DBCursor cursor3 = tripcollection.find(new BasicDBObject("destination",destination));
                if (cursor3.count() == 0) {
                    Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                } else {
                    while(cursor3.hasNext()) {
                        //data retrival
                        cnt++;
                        BasicDBObject dbobj3 = (BasicDBObject) cursor3.next();
                        //name=((BasicDBObject) cur.next()).getString("d_name");

                        trip_id = dbobj3.get("trip_id").toString();
                        for (int i = 0; i < cnt; i++) {
                            Trips.add(trip_id);
                            cnt--;
                        }

                        //Toast.makeText(getApplicationContext(), "Record Found", Toast.LENGTH_SHORT).show();
                        Intent i1 = new Intent(TripDetails.this, ViewTripDetails.class);
                        i1.putStringArrayListExtra("Trips", Trips);
                        startActivity(i1);
                    }
                }
                Trips.clear();
            }

            //only status is selected
            else if((!status.equals("Select Trip Status")) && (destination.equals("Select Destination")) &&(source.equals("Select Source")) && (driverid.isEmpty()))
            {
                DBCursor cursor4 = tripcollection.find(new BasicDBObject("status",status));
                if (cursor4.count() == 0) {
                    Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                } else {
                    while (cursor4.hasNext()) {
                        //data retrival
                        cnt++;
                        BasicDBObject dbobj4 = (BasicDBObject) cursor4.next();
                        //name=((BasicDBObject) cur.next()).getString("d_name");

                        trip_id = dbobj4.get("trip_id").toString();
                        for (int i = 0; i < cnt; i++) {
                            Trips.add(trip_id);
                            cnt--;
                        }

                        //Toast.makeText(getApplicationContext(), "Record Found", Toast.LENGTH_SHORT).show();
                        Intent i1 = new Intent(TripDetails.this, ViewTripDetails.class);
                        i1.putStringArrayListExtra("Trips", Trips);
                        startActivity(i1);
                    }
                }
                Trips.clear();
            }


            //driver and source
            else if((!driverid.isEmpty()) && (!source.equals("Select Source")) &&(destination.equals("Select Destination") && (status.equals("Select Trip Status")))){
                DBCursor cursor5 = tripcollection.find(new BasicDBObject("d_utaid", driverid).append("source",source));
                if (cursor5.count() == 0) {
                    Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                } else {
                    while (cursor5.hasNext()) {
                        //data retrival
                        cnt++;
                        BasicDBObject dbobj5 = (BasicDBObject) cursor5.next();
                        //name=((BasicDBObject) cur.next()).getString("d_name");

                        trip_id = dbobj5.get("trip_id").toString();
                        for (int i = 0; i < cnt; i++) {
                            Trips.add(trip_id);
                            cnt--;
                        }

                        //Toast.makeText(getApplicationContext(), "Record Found", Toast.LENGTH_SHORT).show();
                        Intent i1 = new Intent(TripDetails.this, ViewTripDetails.class);
                        i1.putStringArrayListExtra("Trips", Trips);
                        startActivity(i1);
                    }
                }
                Trips.clear();
            }

            //driver and destination
            else if( (!driverid.isEmpty()) && (!destination.equals("Select Destination")) &&(source.equals("Select Source")) && (status.equals("Select Trip Status"))){
                DBCursor cursor6 = tripcollection.find(new BasicDBObject("d_utaid", driverid).append("destination",destination));
                if (cursor6.count() == 0) {
                    Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                } else {
                    while (cursor6.hasNext()) {
                        //data retrival
                        cnt++;
                        BasicDBObject dbobj6 = (BasicDBObject) cursor6.next();
                        //name=((BasicDBObject) cur.next()).getString("d_name");

                        trip_id = dbobj6.get("trip_id").toString();
                        for (int i = 0; i < cnt; i++) {
                            Trips.add(trip_id);
                            cnt--;
                        }

                        //Toast.makeText(getApplicationContext(), "Record Found", Toast.LENGTH_SHORT).show();
                        Intent i1 = new Intent(TripDetails.this, ViewTripDetails.class);
                        i1.putStringArrayListExtra("Trips", Trips);
                        startActivity(i1);
                    }
                }
                Trips.clear();
            }

            //driver and status
            else if ((!driverid.isEmpty()) && (!status.equals("Select Trip Status")) &&(destination.equals("Select Destination")) && (source.equals("Select Source"))){
                DBCursor cursor7 = tripcollection.find(new BasicDBObject("d_utaid", driverid).append("status",status));
                if (cursor7.count() == 0) {
                    Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                } else {
                    while (cursor7.hasNext()) {
                        //data retrival
                        cnt++;
                        BasicDBObject dbobj7 = (BasicDBObject) cursor7.next();
                        //name=((BasicDBObject) cur.next()).getString("d_name");

                        trip_id = dbobj7.get("trip_id").toString();
                        for (int i = 0; i < cnt; i++) {
                            Trips.add(trip_id);
                            cnt--;
                        }

                        //Toast.makeText(getApplicationContext(), "Record Found", Toast.LENGTH_SHORT).show();
                        Intent i1 = new Intent(TripDetails.this, ViewTripDetails.class);
                        i1.putStringArrayListExtra("Trips", Trips);
                        startActivity(i1);
                    }
                }
                Trips.clear();
            }

            //driver,source,destination

            else if ((!driverid.isEmpty()) && (!source.equals("Select Source")) && (!destination.equals("Select Destination")) && (status.equals("Select Trip Status")) ){
                DBCursor cursor8 = tripcollection.find(new BasicDBObject("d_utaid", driverid).append("source",source).append("destination",destination));
                if (cursor8.count() == 0) {
                    Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                } else {
                    while (cursor8.hasNext()) {
                        //data retrival
                        cnt++;
                        BasicDBObject dbobj8 = (BasicDBObject) cursor8.next();
                        //name=((BasicDBObject) cur.next()).getString("d_name");

                        trip_id = dbobj8.get("trip_id").toString();
                        for (int i = 0; i < cnt; i++) {
                            Trips.add(trip_id);
                            cnt--;
                        }

                       // Toast.makeText(getApplicationContext(), "Record Found", Toast.LENGTH_SHORT).show();
                        Intent i1 = new Intent(TripDetails.this, ViewTripDetails.class);
                        i1.putStringArrayListExtra("Trips", Trips);
                        startActivity(i1);
                    }
                }
                Trips.clear();
            }

            //driver source status

            else if ((!driverid.isEmpty()) && (!source.equals("Select Source")) && (!status.equals("Select Trip Status")) &&(destination.equals("Select Destination"))){
                DBCursor cursor9 = tripcollection.find(new BasicDBObject("d_utaid", driverid).append("source",source).append("status",status));
                if (cursor9.count() == 0) {
                    Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                } else {
                    while (cursor9.hasNext()) {
                        //data retrival
                        cnt++;
                        BasicDBObject dbobj9 = (BasicDBObject) cursor9.next();
                        //name=((BasicDBObject) cur.next()).getString("d_name");

                        trip_id = dbobj9.get("trip_id").toString();
                        for (int i = 0; i < cnt; i++) {
                            Trips.add(trip_id);
                            cnt--;
                        }

                        //Toast.makeText(getApplicationContext(), "Record Found", Toast.LENGTH_SHORT).show();
                        Intent i1 = new Intent(TripDetails.this, ViewTripDetails.class);
                        i1.putStringArrayListExtra("Trips", Trips);
                        startActivity(i1);
                    }
                }
                Trips.clear();
            }

            //driver destination status

            else if ((!driverid.isEmpty()) && (!destination.equals("Select Destination")) && (!status.equals("Select Trip Status")) &&(source.equals("Select Source") ) ){
                DBCursor cursor10 = tripcollection.find(new BasicDBObject("d_utaid", driverid).append("destination",destination).append("status",status));
                if (cursor10.count() == 0) {
                    Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                } else {
                    while (cursor10.hasNext()) {
                        //data retrival
                        cnt++;
                        BasicDBObject dbobj10 = (BasicDBObject) cursor10.next();
                        //name=((BasicDBObject) cur.next()).getString("d_name");

                        trip_id = dbobj10.get("trip_id").toString();
                        for (int i = 0; i < cnt; i++) {
                            Trips.add(trip_id);
                            cnt--;
                        }

                      //  Toast.makeText(getApplicationContext(), "Record Found", Toast.LENGTH_SHORT).show();
                        Intent i1 = new Intent(TripDetails.this, ViewTripDetails.class);
                        i1.putStringArrayListExtra("Trips", Trips);
                        startActivity(i1);
                    }
                }
                Trips.clear();
            }

            //source destination status
           else if ((!source.equals("Select Source")) && (!destination.equals("Select Destination")) && (!status.equals("Select Trip Status")) &&(driverid.isEmpty()) ){
                DBCursor cursor11 = tripcollection.find(new BasicDBObject("source",source).append("destination",destination).append("status",status));
                if (cursor11.count() == 0) {
                    Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                } else {
                    while (cursor11.hasNext()) {
                        //data retrival
                        cnt++;
                        BasicDBObject dbobj11 = (BasicDBObject) cursor11.next();
                        //name=((BasicDBObject) cur.next()).getString("d_name");

                        trip_id = dbobj11.get("trip_id").toString();
                        for (int i = 0; i < cnt; i++) {
                            Trips.add(trip_id);
                            cnt--;
                        }

                       // Toast.makeText(getApplicationContext(), "Record Found", Toast.LENGTH_SHORT).show();
                        Intent i1 = new Intent(TripDetails.this, ViewTripDetails.class);
                        i1.putStringArrayListExtra("Trips", Trips);
                        startActivity(i1);
                    }
                }
                Trips.clear();
            }

            //driver source destination status
            else if ((!driverid.isEmpty()) && (!source.equals("Select Source")) && (!destination.equals("Select Destination")) && (!status.equals("Select Trip Status")) ){
                DBCursor cursor12 = tripcollection.find(new BasicDBObject("d_utaid",driverid).append("source",source).append("destination",destination).append("status",status));
                if (cursor12.count() == 0) {
                    Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                } else {
                    while (cursor12.hasNext()) {
                        //data retrival
                        cnt++;
                        BasicDBObject dbobj12 = (BasicDBObject) cursor12.next();
                        //name=((BasicDBObject) cur.next()).getString("d_name");

                        trip_id = dbobj12.get("trip_id").toString();
                        for (int i = 0; i < cnt; i++) {
                            Trips.add(trip_id);
                            cnt--;
                        }

                        //Toast.makeText(getApplicationContext(), "Record Found", Toast.LENGTH_SHORT).show();
                        Intent i1 = new Intent(TripDetails.this, ViewTripDetails.class);
                        i1.putStringArrayListExtra("Trips", Trips);
                        startActivity(i1);
                    }
                }
                Trips.clear();
            }

            //source destination
            else if ((!source.equals("Select Source")) && (!destination.equals("Select Destination")) &&(driverid.isEmpty()) && (status.equals("Select Trip Status"))){
                DBCursor cursor13 = tripcollection.find(new BasicDBObject("source",source).append("destination",destination));
                if (cursor13.count() == 0) {
                    Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                } else {
                    while(cursor13.hasNext()) {
                        //data retrival
                        cnt++;
                        BasicDBObject dbobj13 = (BasicDBObject) cursor13.next();
                        //name=((BasicDBObject) cur.next()).getString("d_name");

                        trip_id = dbobj13.get("trip_id").toString();
                        for (int i = 0; i < cnt; i++) {
                            Trips.add(trip_id);
                            cnt--;
                        }

                       // Toast.makeText(getApplicationContext(), "Record Found", Toast.LENGTH_SHORT).show();
                        Intent i1 = new Intent(TripDetails.this, ViewTripDetails.class);
                        i1.putStringArrayListExtra("Trips", Trips);
                        startActivity(i1);
                    }
                }
                Trips.clear();
            }

            //source and status
            else if ( (!source.equals("Select Source")) && (!status.equals("Select Trip Status")) &&(destination.equals("Select Destination")) && (driverid.isEmpty()) ){
                DBCursor cursor14 = tripcollection.find(new BasicDBObject("source",source).append("status",status));
                if (cursor14.count() == 0) {
                    Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                } else {
                    while (cursor14.hasNext()) {
                        //data retrival
                        cnt++;
                        BasicDBObject dbobj14 = (BasicDBObject) cursor14.next();
                        //name=((BasicDBObject) cur.next()).getString("d_name");

                        trip_id = dbobj14.get("trip_id").toString();
                        for (int i = 0; i < cnt; i++) {
                            Trips.add(trip_id);
                            cnt--;
                        }

                       // Toast.makeText(getApplicationContext(), "Record Found", Toast.LENGTH_SHORT).show();
                        Intent i1 = new Intent(TripDetails.this, ViewTripDetails.class);
                        i1.putStringArrayListExtra("Trips", Trips);
                        startActivity(i1);

                    }
                }
                Trips.clear();
            }

            //destination and status
            else if ((!destination.equals("Select Destination")) && (!status.equals("Select Trip Status")) &&(source.equals("Select Destination") && (driverid.isEmpty())) ){
                DBCursor cursor15 = tripcollection.find(new BasicDBObject("destination",destination).append("status",status));
                if (cursor15.count() == 0) {
                    Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                } else {
                    while (cursor15.hasNext()) {
                        //data retrival
                        cnt++;
                        BasicDBObject dbobj15 = (BasicDBObject) cursor15.next();
                        //name=((BasicDBObject) cur.next()).getString("d_name");

                        trip_id = dbobj15.get("trip_id").toString();
                        for (int i = 0; i < cnt; i++) {
                            Trips.add(trip_id);
                            cnt--;
                        }

                        //Toast.makeText(getApplicationContext(), "Record Found", Toast.LENGTH_SHORT).show();
                        Intent i1 = new Intent(TripDetails.this, ViewTripDetails.class);
                        i1.putStringArrayListExtra("Trips", Trips);
                        startActivity(i1);
                        finish();
                    }
                }
                Trips.clear();
            }

            else
            {
                Toast.makeText(getApplicationContext(), "Please make a selection!", Toast.LENGTH_SHORT).show();
            }
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
