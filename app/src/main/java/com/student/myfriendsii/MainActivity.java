package com.student.myfriendsii;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static com.student.myfriendsii.R.id.list_item;

public class MainActivity extends ActionBarActivity {
    private MyDBHandler myDBH = null;
    private Cursor myCursor = null;
    private MyDBAdapter myDBAdapter = null;
    public ArrayList<String> tblNameArr=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView myListView = (ListView)findViewById(R.id.listView);
        myDBH = new MyDBHandler(this, null, null, 1);
        myDBH.createDatabase();
        myCursor=myDBH.getCursor();
        startManagingCursor(myCursor);

        myDBAdapter = new MyDBAdapter(this, myCursor);
        myListView.setAdapter(myDBAdapter);


        tblNameArr = new ArrayList<String>();
        Cursor tableNameCursor = myDBH.getTableNameCursor();

        //http://stackoverflow.com/questions/11506527/count-the-number-of-tables-in-the-sqlite-database
        if (tableNameCursor.moveToFirst()) {
            while ( !tableNameCursor.isAfterLast() ) {
                tblNameArr.add(tableNameCursor.getString(tableNameCursor.getColumnIndex("name")));
                tableNameCursor.moveToNext();
            }
        }

        /*EditText tableNames = (EditText)findViewById(R.id.dbsName);

        for(String msg : tblNameArr)
        {
            tableNames.setText(tableNames.getText()+msg);
        }*/

        init();


        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = (Cursor) myListView.getItemAtPosition(position);
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"));
            Toast.makeText(getApplicationContext(), "Email:   " + email + "\n" + "Phone:  " + phoneNumber, Toast.LENGTH_LONG).show();
            }
        });
    }


    public class MyDBAdapter extends CursorAdapter{

        public MyDBAdapter(Context context, Cursor cursor){
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.myfriendslistview, parent, false);
        }


        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Find fields to populate in inflated template
            //EditText dbsNameBody = (EditText) view.findViewById(R.id.dbsName);
            //TextView idBody = (TextView) view.findViewById(R.id.idtxtview);
            TextView nameBody = (TextView) view.findViewById(R.id.nametxtview);
            //TextView emailBody = (TextView) view.findViewById(R.id.emailtxtview);
            //TextView phoneNumberBody = (TextView) view.findViewById(R.id.phonenumtxtview);

            // Extract properties from cursor
            //String dbsValue = cursor.getString(cursor.getColumnIndexOrThrow(""));
            int idValue = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            String nameValue =cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String emailValue =cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String phoneNumberValue = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"));

            System.getProperty("line.separator");
            //dbsNameBody.setText(dbsValue);
            //idBody.setText(String.valueOf(idValue));
            nameBody.setText(nameValue);
            //emailBody.setText(emailValue);
            //phoneNumberBody.setText(phoneNumberValue);
            System.getProperty("line.separator");

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;

        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id){
            case R.id.action_about :
            Toast.makeText(getBaseContext(), "This App provides the contact information of my friends\n", Toast.LENGTH_LONG).show();
            return true;

            case R.id.action_help:
                Toast.makeText(getBaseContext(), "This App displays their names in ascending order.\n" + "Once you click on one name,\n that person's email and phone number will be displayed.\n", Toast.LENGTH_LONG).show();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    public void init() {
        AutoCompleteTextView acTVTerm = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        String[] term = new String[tblNameArr.size()];
        tblNameArr.toArray(term);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,term);
        acTVTerm.setAdapter(adapter);
        acTVTerm.setTextColor(Color.RED);

    }

}
