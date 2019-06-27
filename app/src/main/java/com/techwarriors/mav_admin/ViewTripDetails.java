package com.techwarriors.mav_admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.List;

public class ViewTripDetails extends AppCompatActivity {

    ArrayList<String> trips=new ArrayList<String>();
    ArrayAdapter<String> aa;
    ListView lv;
    String trip_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i2=getIntent();
        trips=i2.getStringArrayListExtra("Trips");


        lv=(ListView)findViewById(R.id.ListView2);
        //aa.notifyDataSetChanged();
        aa=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,trips);

        lv.setAdapter(aa);
        //aa.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                trip_id=lv.getItemAtPosition(i).toString();
                Intent i1=new Intent(ViewTripDetails.this,DisplayingTripDetails.class);
                i1.putExtra("trip_id",trip_id);
                startActivity(i1);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i2=new Intent(this,Home.class);
        startActivity(i2);
        finish();
    }

}
