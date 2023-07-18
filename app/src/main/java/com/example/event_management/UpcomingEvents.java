package com.example.event_management;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UpcomingEvents extends AppCompatActivity {
    ListView listView;
    KeyValueDB keyValueDB;

    ArrayList<Event> arrayList;
    Button createNewBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);

        listView=findViewById(R.id.eventList);
        createNewBtn=findViewById(R.id.btnCreate);

        arrayList=new ArrayList<>();
        keyValueDB=new KeyValueDB(this);

        createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UpcomingEvents.this,MainActivity.class);
                startActivity(intent);
            }
        });

        String[] keys={"action","id","semester"};
        String[] values={"restore","2019-1-60-139","2022-2"};

        httpRequest(keys,values);

        loadData();
    }

    public void loadData(){
        Cursor cursor=keyValueDB.getAllKeyValues();

        while (cursor.moveToNext()){
            String key=cursor.getString(0);
            String value=cursor.getString(1);
            System.out.println("KEY: " + key);
            System.out.println("VAL: " + value);
            if(!value.equals("")){
                String[] values =value.split("--");
                arrayList.add(new Event(key, values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]));
            }




        }
        CustomEventAdapter eventAdapter=new CustomEventAdapter(this,arrayList);
        listView.setAdapter(eventAdapter);
    }

    @SuppressLint("StaticFieldLeak")
    private void httpRequest(final String keys[], final String values[]){
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... voids) {
                List<NameValuePair> params =new ArrayList<NameValuePair>();
                for (int i=0; i<keys.length; i++){
                    params.add(new BasicNameValuePair(keys[i],values[i]));
                }
                URL url= null;
                String data="";
                try {
                    data=JSONParser.getInstance().makeHttpRequest("http://muthosoft.com/univ/cse489/index.php","POST",params);
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
            protected void onPostExecute(String data){
                if(data!=null){
                    System.out.println("Upcomoing event: "+data);
                    try {
                        JSONObject json = new JSONObject(data);
                        JSONArray events = json.getJSONArray("events");
                        for(int i = 0; i < events.length(); i++){
                            JSONObject e = events.getJSONObject(i);
                            String k = e.getString("key");
                            String v = e.getString("value");
                            keyValueDB.updateValueByKey(k,v);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.execute();
    }
}