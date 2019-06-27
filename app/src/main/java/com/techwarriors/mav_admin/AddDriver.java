package com.techwarriors.mav_admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AddDriver extends AppCompatActivity {

    //DECALARATIONS

    String DATABASE_URI="mongodb:// 192.168.43.71:27017";
    String DATABASE_NAME="mav";
    String COLLECTION_NAME1="driver";
    DBCollection drivercollection;

    EditText etdpwd,etdrivername,etdphone,etdsecque,etdsecans;
    TextView etdutaid;
    Switch sdstatus;
    String dutaid,dpwd,dname,dphone,dsecque,dsecans,dstatus="false";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
//getting data from plain text fields
        Intent i=getIntent();
        String utaid=i.getStringExtra("utaid");


        etdutaid=(TextView) findViewById(R.id.TVdid);
        etdpwd=(EditText)findViewById(R.id.PTdriverpassword);
        etdrivername=(EditText)findViewById(R.id.PTdrivername12);
        etdphone=(EditText)findViewById(R.id.PTdriverphone);
        etdsecque=(EditText)findViewById(R.id.PTdriversecque);
        etdsecans=(EditText)findViewById(R.id.PTdriversecans);
        sdstatus=(Switch)findViewById(R.id.Sdriverswitch);
        sdstatus.setChecked(false);

        etdutaid.setText(utaid);

        sdstatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    dstatus="working";
                }
                else {
                    dstatus="notWorking";
                }
            }
        });


    }

    private static final String md5(final String pwd) {
        try {

            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(pwd.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void onaddclick(View view) {


        try {
            MongoClientURI mongoClientURI = new MongoClientURI(DATABASE_URI);
            MongoClient mongoClient = new MongoClient(mongoClientURI);
            DB db = mongoClient.getDB(DATABASE_NAME);



            //converting to string
            dutaid=etdutaid.getText().toString();
            dpwd=etdpwd.getText().toString();
            dname=etdrivername.getText().toString();
            dphone=etdphone.getText().toString();
            dsecque=etdsecque.getText().toString();
            dsecans=etdsecans.getText().toString();





          //  dstatus=sdstatus.getText().toString();

            //other variables to be inserted in driver collection
            String drating="0";
            String disavailable="false";

            //connect to db
            drivercollection = db.getCollection(COLLECTION_NAME1);
            String encrpwd=md5(dpwd);

           if ((dutaid.equals("")) || (dpwd.equals("")) || (dname.equals("")) || (dphone.equals("")) ||(dsecque.equals("")) ||(dsecans.equals("")))
           {
               Toast.makeText(getApplicationContext(), "Form incomplete", Toast.LENGTH_SHORT).show();
           }
           else {

               BasicDBObject obj=new BasicDBObject("d_utaid",dutaid).append("d_pass",encrpwd).append("d_name",dname)
                                     .append("d_phone",dphone).append("d_sec_que",dsecque).append("d_sec_ans",dsecans)
                                      .append("driver_status",dstatus).append("d_rating",drating).append("driver_isavailable",disavailable);
               drivercollection.insert(obj);
               Toast.makeText(getApplicationContext(),"Driver Added Successfully!",Toast.LENGTH_LONG).show();
               Intent i2=new Intent(AddDriver.this,Home.class);
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
