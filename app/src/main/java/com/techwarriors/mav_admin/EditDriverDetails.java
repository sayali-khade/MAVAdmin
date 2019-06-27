package com.techwarriors.mav_admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoIncompatibleDriverException;
import com.mongodb.MongoTimeoutException;

public class EditDriverDetails extends AppCompatActivity {

    String DATABASE_URI="mongodb:// 192.168.43.71:27017";
    String DATABASE_NAME="mav";
    String COLLECTION_NAME1="driver";
    DBCollection drivercollection;

    EditText drivername,driverphone;
    String dname,dphone,dutaid;
    TextView utaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_driver_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent i2=getIntent();
       // BasicDBObject dbobj=(BasicDBObject) i2.getComponent("dbobj");
        dname=i2.getStringExtra("name");
        dphone=i2.getStringExtra("phone");
        dutaid=i2.getStringExtra("id");

        //String dphone1=i.getStringExtra("dphone1");
        drivername=(EditText)findViewById(R.id.PTdrivername12);
        driverphone=(EditText)findViewById(R.id.PTcontact);
        utaid=(TextView)findViewById(R.id.TVid2);

        utaid.setText(dutaid);

        drivername.setText(dname);
        driverphone.setText(dphone);

    }

    public void onupdateclick(View view) {

        try {
            MongoClientURI mongoClientURI = new MongoClientURI(DATABASE_URI);
            MongoClient mongoClient = new MongoClient(mongoClientURI);
            DB db = mongoClient.getDB(DATABASE_NAME);

            String newname=drivername.getText().toString();
            String newphone=driverphone.getText().toString();

            //connect to db
            drivercollection = db.getCollection(COLLECTION_NAME1);


            if ((dname.equals("")) || (dphone.equals("")))
            {
                Toast.makeText(getApplicationContext(), "Form incomplete", Toast.LENGTH_SHORT).show();
            }
            else {

                BasicDBObject setname=new BasicDBObject("$set",new BasicDBObject("d_name",newname));
                BasicDBObject setphone=new BasicDBObject("$set",new BasicDBObject("d_phone",newphone));
                drivercollection.update(new BasicDBObject("d_utaid",dutaid),setname);
                drivercollection.update(new BasicDBObject("d_utaid",dutaid),setphone);
                Toast.makeText(getApplicationContext(),"Driver Information Updated Successfully!",Toast.LENGTH_LONG).show();
                Intent i2=new Intent(EditDriverDetails.this,Home.class);
                startActivity(i2);
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
