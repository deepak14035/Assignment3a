package com.example.deepak14035.assignment2;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String FILE_NAME = "MyPrefsFile", SCORE5="5", SCORE10="10", SCORE15="15", SEND_SCORE="send scores", SEND_DATA="datt";
    public static final String COUNT5="c5", COUNT10="c10", COUNT15="c15";
    public static final int REQUEST_PERMISSIONS=5;
    String FILENAME = "hello_file";

    Float countToFiveTime, countToTenTime,countToFifteenTime;
    int countToFive, countToTen, countToFifteen;
    private Button button5, button10, button15, savePublicDataButton;
    long tStart;
    private TextView text5, text10, text15, helloText, publicFileData, sqliteDataText;
    EditText name;
    String data;
    DataHelper mDbHelper;
    SQLiteDatabase db;

    String data1="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        name = (EditText) findViewById(R.id.name);
        data="";
        try {
            FileInputStream fis = openFileInput(FILENAME);
            while(fis.available()!=0) {
                data += (char) fis.read();
            }
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        helloText=(TextView)findViewById(R.id.helloText);

        helloText.setText(data);

        SharedPreferences settings = getSharedPreferences(FILE_NAME, 0);
        countToFiveTime = settings.getFloat(SCORE5, Float.POSITIVE_INFINITY);
        countToTenTime = settings.getFloat(SCORE10, Float.POSITIVE_INFINITY);
        countToFifteenTime = settings.getFloat(SCORE15, Float.POSITIVE_INFINITY);
        button5=(Button)findViewById(R.id.buttonFiveId);
        button10=(Button)findViewById(R.id.buttonTenId);
        button15=(Button)findViewById(R.id.buttonFifteenId);
        savePublicDataButton = (Button)findViewById(R.id.savePublicDataButton);

        text5 = (TextView)findViewById(R.id.countToFiveText);
        text10 = (TextView)findViewById(R.id.countToTenText);
        text15 = (TextView)findViewById(R.id.countToFifteenText);
        publicFileData = (TextView)findViewById(R.id.publicFileData);
        sqliteDataText= (TextView)findViewById(R.id.sqliteDataText);
        text5.setText(countToFiveTime.toString());
        text10.setText(countToTenTime.toString());
        text15.setText(countToFifteenTime.toString());
        countToFive=0; countToTen=0; countToFifteen=0;
        button5.setText(""+countToFive);
        button10.setText(""+countToTen);
        button15.setText("" + countToFifteen);
        int col=Color.rgb(255,0,0);
        button5.setBackgroundColor(col);
        button10.setBackgroundColor(col);
        button15.setBackgroundColor(col);


        try {
            if (isExternalStorageReadable()) {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "publicAssignment2");

                File read = new File(file, "assignmentData.txt");
                FileInputStream f = new FileInputStream(read);
                while(f.available()!=0) {
                    data1 += (char) f.read();
                }

                //publicFileData.append(data1);
            } else {
                Log.e("error", "no external storage");
            }

            if (isExternalStorageReadable()) {
                File file = new File(this.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),"privateAssignment2");

                File read = new File(file, "assignmentData.txt");
                FileInputStream f = new FileInputStream(read);
                while(f.available()!=0) {
                    data1 += (char) f.read();
                }
                publicFileData.append("\n private data - "+data1);

            } else {
                Log.e("error", "no external storage");
            }


        }catch(Exception e){
            e.printStackTrace();
        }
        //externalData+=data1;
        mDbHelper = new DataHelper(this.getApplicationContext());
// Gets the data repository in write mode
        db = mDbHelper.getWritableDatabase();
        mDbHelper.onCreate(db);
    }

    public void onExternalData(View view){
        Intent intent = new Intent(this, Main22Activity.class);
        intent.putExtra(SEND_DATA, data1);

        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    void disableOthers(Button a, Button b){
        a.setEnabled(false);
        b.setEnabled(false);
    }
    void enableOthers(Button a, Button b){
        a.setEnabled(true);
        b.setEnabled(true);
    }
    public void countToFiveButton(View view){
        if(countToFive==0) {
            disableOthers(button10, button15);
            tStart = System.currentTimeMillis();
        }

        countToFive++;
        if(countToFive==5){
            enableOthers(button10, button15);
            countToFive=0;
            long tstop=System.currentTimeMillis();
            float timeDiff= (float) ((tstop-tStart)/1000.0);
            mDbHelper.insertValues(db, name.getText().toString(), ""+timeDiff, "five");

            if(timeDiff<countToFiveTime){
                countToFiveTime=timeDiff;
                text5.setText(""+countToFiveTime);
                if(mDbHelper.checkIfNameAlreadyExists(db, name.getText().toString(), "five")) {
                    mDbHelper.updateTime(db, name.getText().toString(), "" + timeDiff, "five");
                    Log.e("asd", "updating");
                }
                else{
                    mDbHelper.insertValues(db, name.getText().toString(), ""+timeDiff, "five");
                    Log.e("asd", "inserting");
                }
            }
        }
        int col=Color.rgb(255*(5-countToFive)/5,255*(countToFive)/5,0);
        button5.setBackgroundColor(col);
        button5.setText("" + countToFive);
    }

    public void countToTenButton(View view){
        if(countToTen==0) {
            disableOthers(button5, button15);
            tStart = System.currentTimeMillis();
        }

        countToTen++;
        if(countToTen==10){
            enableOthers(button5, button15);
            countToTen=0;
            long tstop=System.currentTimeMillis();
            float timeDiff= (float) ((tstop-tStart)/1000.0);
            mDbHelper.insertValues(db, name.getText().toString(), ""+timeDiff, "ten");

            if(timeDiff<countToTenTime){
                countToTenTime=timeDiff;
                text10.setText(""+countToTenTime);
                if(mDbHelper.checkIfNameAlreadyExists(db, name.getText().toString(), "ten")) {
                    mDbHelper.updateTime(db, name.getText().toString(), "" + timeDiff, "ten");
                    Log.e("asd", "updating");
                }
                else{
                    mDbHelper.insertValues(db, name.getText().toString(), ""+timeDiff, "ten");
                    Log.e("asd", "inserting");
                }


            }

        }
        int col=Color.rgb(255*(10-countToTen)/10,255*(countToTen)/10,0);
        button10.setBackgroundColor(col);
        button10.setText("" + countToTen);
    }

    public void onClearScores(View view){
        mDbHelper.clearData(db);
    }

    public void countToFifteenButton(View view){
        if(countToFifteen==0) {
            disableOthers(button10, button5);
            tStart = System.currentTimeMillis();
        }

        countToFifteen++;
        if(countToFifteen==15){
            enableOthers(button10, button5);
            countToFifteen=0;
            long tstop=System.currentTimeMillis();
            float timeDiff= (float) ((tstop-tStart)/1000.0);

            if(timeDiff<countToFifteenTime){
                countToFifteenTime=timeDiff;
                text15.setText(""+countToFifteenTime);
                if(mDbHelper.checkIfNameAlreadyExists(db, name.getText().toString(), "fifteen")) {
                    mDbHelper.updateTime(db, name.getText().toString(), "" + timeDiff, "fifteen");
                    Log.e("asd", "updating");
                }
                else{
                    mDbHelper.insertValues(db, name.getText().toString(), ""+timeDiff, "fifteen");
                    Log.e("asd", "inserting");
                }
            }
        }
        int col=Color.rgb(255*(15-countToFifteen)/15,255*(countToFifteen)/15,0);
        button15.setBackgroundColor(col);

        button15.setText("" + countToFifteen);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onSqliteFetchPressed(View view){
        String values = mDbHelper.getValues(db);
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra(SEND_SCORE, values);

        startActivity(intent);


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putFloat(SCORE5, countToFiveTime);
        savedInstanceState.putFloat(SCORE10, countToTenTime);
        savedInstanceState.putFloat(SCORE15, countToFifteenTime);
        savedInstanceState.putInt(COUNT5, countToFive);
        savedInstanceState.putInt(COUNT10, countToTen);
        savedInstanceState.putInt(COUNT15, countToFifteen);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        countToFiveTime = savedInstanceState.getFloat(SCORE5);
        countToTenTime= savedInstanceState.getFloat(SCORE10);
        countToFifteenTime= savedInstanceState.getFloat(SCORE15);
        countToFive=savedInstanceState.getInt(COUNT5);
        countToTen=savedInstanceState.getInt(COUNT10);
        countToFifteen=savedInstanceState.getInt(COUNT15);
        if(countToFive!=0){
            disableOthers(button10, button15);
        }
        if(countToTen!=0){
            disableOthers(button5, button10);
        }
        if(countToFifteen!=0){
            disableOthers(button10, button5);
        }
        button5.setText("" + countToFive);
        button10.setText("" + countToTen);
        button15.setText("" + countToFifteen);
        text5.setText(countToFiveTime.toString());
        text10.setText(countToTenTime.toString());
        text15.setText(countToFifteenTime.toString());

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.e("asd", "REACHED!!!");
        int id = item.getItemId();
        FragmentManager fm = getFragmentManager();

        if (id == R.id.nav_second_layout) {
            fm.beginTransaction().replace(R.id.content_frame, new SecondActivity()).commit();
            Fragment previousInstance = getFragmentManager().findFragmentById(R.id.content_frame);
            if (previousInstance != null)
                fm.beginTransaction().remove(previousInstance);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onSavePublicDataButton(View view){
        try {
            if(isExternalStorageWritable() && requestPermission()) {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "publicAssignment2");
                if (!file.mkdirs()) {
                    Log.e("error", "Directory not created");
                }
                else{
                    File write = new File(file, "assignmentData.txt");
                    FileOutputStream f = new FileOutputStream(write);
                    PrintWriter pw = new PrintWriter(f);
                    pw.println(name.getText()+"'s score\n is "+text5.getText()+"for 5 clicks");
                    pw.flush();
                    pw.close();
                    f.close();
                }
            }
            else{
                Log.e("error","no external storage");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        //SHARED PREFERENCES
        SharedPreferences settings = getSharedPreferences(FILE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(SCORE5, countToFiveTime);
        editor.putFloat(SCORE10, countToTenTime);
        editor.putFloat(SCORE15, countToFifteenTime);
        editor.commit();
        try {
            //INTERNAL STORAGE
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);

            data = name.getText().toString();
            fos.write(data.getBytes());
            fos.close();

            //EXTERNAL STORAGE
            if(isExternalStorageWritable() && requestPermission()) {
                //PUBLIC
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "publicAssignment2");
                if (!file.mkdirs()) {
                    Log.e("error", "Directory not created");
                    File write = new File(file, "assignmentData.txt");
                    if(file.exists()){
                        FileOutputStream f = new FileOutputStream(write);
                        PrintWriter pw = new PrintWriter(f);
                        pw.println(name.getText()+"'s score is\n "+text5.getText()+"for 5 clicks(public)");
                        pw.flush();
                        pw.close();
                        f.close();
                    }
                }
                else{
                    File write = new File(file, "assignmentData.txt");
                    FileOutputStream f = new FileOutputStream(write);
                    PrintWriter pw = new PrintWriter(f);
                    pw.println(name.getText()+"'s score is\n "+text5.getText()+"for 5 clicks(public)");
                    pw.flush();
                    pw.close();
                    f.close();
                }

                //PRIVATE
                File file2= new File(this.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),"privateAssignment2");
                if (!file2.mkdirs()) {
                    Log.e("error", "Directory not created");
                    File write = new File(file2, "assignmentData.txt");
                    if(file2.exists()){
                        FileOutputStream f = new FileOutputStream(write);
                        PrintWriter pw = new PrintWriter(f);
                        pw.println(name.getText()+"'s score is\n "+text5.getText()+"for 5 clicks (in private)");
                        pw.flush();
                        pw.close();
                        f.close();
                    }
                }
                else{
                    File write = new File(file2, "assignmentData.txt");
                    FileOutputStream f = new FileOutputStream(write);
                    PrintWriter pw = new PrintWriter(f);
                    pw.println(name.getText()+"'s score is\n "+text5.getText()+"for 5 clicks (in private)");
                    pw.flush();
                    pw.close();
                    f.close();
                }
            }
            else{
                Log.e("error","no external storage");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // Commit the edits!



    }

    boolean requestPermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
            return false;
        }
        return true;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void editName(View view){
        String hellotxt=name.getText().toString();
        helloText.setText("Hello "+hellotxt);
    }

}
