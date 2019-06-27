package com.techwarriors.mav_admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView lv;
    ArrayList<String> activities=new ArrayList<String>();
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sp=getSharedPreferences("data",MODE_PRIVATE);

        //list view code
        activities.add("View Trip Details");
        activities.add("Add Drivers");
        activities.add("Edit Driver Information");
        activities.add("Change Driver Status");


        lv=(ListView)findViewById(R.id.ListView1);
        ArrayAdapter aa=new ArrayAdapter(this,android.R.layout.simple_list_item_1,activities);
        lv.setAdapter(aa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i)
                {
                    case 0: Intent i1= new Intent(Home.this,TripDetails.class);
                                       startActivity(i1);
                                       break;

                    case 1: Intent i2=new Intent(Home.this,ConfirmDriver.class);
                                      startActivity(i2);
                                      break;

                    case 2: Intent i3=new Intent(Home.this,SearchAndEdit.class);
                                      startActivity(i3);
                                      break;

                    case 3: Intent i4=new Intent(Home.this,DriverSearch.class);
                                      startActivity(i4);
                                      break;

                    default:  Toast.makeText(getApplicationContext(),"Make a selection", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
          //  super.onBackPressed();
            finish();
        }
    }

   /*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_viewtripdetails) {

            Intent i1=new Intent(Home.this,TripDetails.class);
            startActivity(i1);

        } else if (id == R.id.nav_add_drivers) {

            Intent i2=new Intent(Home.this,ConfirmDriver.class);
            startActivity(i2);

        } else if (id == R.id.nav_editdriverinfo) {

            Intent i3=new Intent(Home.this,SearchAndEdit.class);
            startActivity(i3);

        } else if (id == R.id.nav_changedriverstatus) {

            Intent i4=new Intent(Home.this,DriverSearch.class);
            startActivity(i4);

        } else if (id == R.id.nav_logout) {

            sp=getSharedPreferences("data",MODE_PRIVATE);

            SharedPreferences.Editor editor=sp.edit();
            editor.putInt("isLogged", 0);
            editor.apply();
            Intent i5 =new Intent(Home.this,Login.class);
            startActivity(i5);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
