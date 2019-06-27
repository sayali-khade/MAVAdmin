package com.techwarriors.mav_admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {

    //DECALARATIONS

    String DATABASE_URI="mongodb:// 192.168.43.71:27017";
    String DATABASE_NAME="mav";
    String COLLECTION_NAME1="admin";
    DBCollection admincollection;
    String utaid,pwd;
    EditText etui,etpw;
    SharedPreferences sharedpref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        etui=(EditText)findViewById(R.id.TFutaId);
        etpw=(EditText)findViewById(R.id.TFpassword);

        sharedpref=getSharedPreferences("data",MODE_PRIVATE);
        int number=sharedpref.getInt("isLogged",0);

        if(number==1){

            Intent i=new Intent(this,Home.class);
            startActivity(i);
            finish();
        }


    }

  /*  @Override
   public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
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

    public void onLoginclick(View v)
    {
        //code for connectivity

        try {
            MongoClientURI mongoClientURI = new MongoClientURI(DATABASE_URI);
            MongoClient mongoClient = new MongoClient(mongoClientURI);
            DB db = mongoClient.getDB(DATABASE_NAME);

            utaid=etui.getText().toString();
            pwd=etpw.getText().toString();
            admincollection = db.getCollection(COLLECTION_NAME1);
            String encrpwd=md5(pwd);
            DBCursor cursor = admincollection.find(new BasicDBObject("a_utaid",utaid)
                    .append("a_pass",encrpwd));
            if (cursor.count() == 0) {
                Toast.makeText(getApplicationContext(), "Login Failed!", Toast.LENGTH_SHORT).show();
            } else {


                   SharedPreferences.Editor editor = sharedpref.edit();
                   editor.putInt("isLogged", 1);
                   editor.apply();

                   Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                   Intent i4 = new Intent(Login.this, Home.class);
                   startActivity(i4);



            }
        } catch (MongoTimeoutException mte) {
            mte.printStackTrace();
        } catch (MongoIncompatibleDriverException mide) {
            mide.printStackTrace();
        } catch (Exception exe) {
            exe.printStackTrace();
        }
       ;



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
