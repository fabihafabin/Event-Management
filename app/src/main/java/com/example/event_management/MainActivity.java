package com.example.event_management;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView errorMessage;
    EditText name,place,datetime,capacity,budget,email,phone,description;
    Button cancel,share,save;
    RadioButton indoor, online, outdoor;

    String name1,place1,datetime1,email1,description1,phone1, type, capacity1,budget1;;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String key=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorMessage=findViewById(R.id.errMsg);


        sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
        editor= sharedPreferences.edit();









//        Edittext initialization
        name=findViewById(R.id.name);
        place=findViewById(R.id.place);
        datetime=findViewById(R.id.date);
        capacity=findViewById(R.id.capacity);
        budget=findViewById(R.id.budget);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        description=findViewById(R.id.description);
        indoor = findViewById(R.id.rbtn1);
        outdoor = findViewById(R.id.rbtn2);
        online = findViewById(R.id.rbtn3);

        cancel=findViewById(R.id.btnCancel);
        share=findViewById(R.id.btnShare);
        save=findViewById(R.id.btnSave);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            key = extras.getString("key");
            KeyValueDB keyValueDB=new KeyValueDB(this);
            String value=keyValueDB.getValueByKey(key);
            String[] values=value.split("--");
            name.setText(values[0]);
            place.setText(values[1]);
            if(values[2].equals("Indoor")){
                indoor.setChecked(true);
            }
            else if(values[2].equals("Outdoor")){
                outdoor.setChecked(true);
            }
            else{
                online.setChecked(true);
            }
            datetime.setText(values[3]);
            capacity.setText(values[4]);
            budget.setText(values[5]);
            email.setText(values[6]);
            phone.setText(values[7]);
            description.setText(values[8]);
        }



        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Share Button Clicked",Toast.LENGTH_SHORT).show();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name1=name.getText().toString();
                place1=place.getText().toString();
                datetime1=datetime.getText().toString();
                capacity1=capacity.getText().toString();
                budget1=budget.getText().toString();
                email1=email.getText().toString();
                description1=description.getText().toString();
                phone1=phone.getText().toString();

                String errorMessage="";

                if(name1.length()<5){
                    errorMessage+="Name is not valid\n";
                }
                if(place1.length()<4){
                    errorMessage+="Place is not valid\n";
                }
                if(capacity1.length()<1){
                    errorMessage+="Capacity is not valid\n";
                }
                if(budget1.length()<2){
                    errorMessage+="Budget is not valid\n";
                }
                if(!isValidEmail(email1)){
                    errorMessage+="Email is not valid\n";
                }
                if(description1.length()<5){
                    errorMessage+="Description is not valid\n";
                }
                if(phone1.length()!=11){
                    errorMessage+="Phone number is not valid\n";
                }

                boolean indoorIsChecked = indoor.isChecked();
                if(indoorIsChecked== true){
                    System.out.println("Indoor checked ");
                    type = "Indoor";
                }

                boolean outDoorIsChecked = outdoor.isChecked();
                if(outDoorIsChecked== true){
                    System.out.println("Outdoor checked ");
                    type = "Outdoor";
                }

                boolean onlineIsChecked = online.isChecked();
                if(onlineIsChecked== true){
                    System.out.println("Online checked ");
                    type = "Online";
                }

                if( !indoorIsChecked || !outDoorIsChecked || !onlineIsChecked){
                    errorMessage+="Radio button not clicked\n";
                }


                if(errorMessage.length() < 1){
                    showDialog(errorMessage,"Error","Ok","Back");
                }
                else{
                    showDialog("Do you want to save this event information ?","Info","Yes","No");
                }
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }



    private void showDialog(String message, String title, String btn1,String btn2){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);

        builder.setCancelable(false)
                .setPositiveButton(btn1,new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id){
                        if(btn1.equals("Yes")){
                            KeyValueDB keyValueDB=new KeyValueDB(MainActivity.this);
                            if(key==null){
                                key=name1+"--"+System.currentTimeMillis();
                            }
                            String value=name1+"--"+place1+"--"+type+"--"+datetime1+"--"+capacity1+"--"+budget1+"--"+email1+"--"+phone1+"--"+description1;

                            String[] keys={"action","id","semester","key","event"};
                            String[] values={"backup","2019-1-60-139","2022-2",key,value};

                            httpRequest(keys,values);
                            keyValueDB.updateValueByKey(key,value);
                            Toast.makeText(getApplicationContext(),"Data Saved Successfully",Toast.LENGTH_SHORT).show();

                            editor.putString("key",key);
                            editor.commit();
                        }
                    }
                })
                .setNegativeButton(btn2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert=builder.create();
        alert.show();
    }

    @SuppressLint("StaticFieldLeak")
    private void httpRequest(final String keys[], final String values[]){
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... voids) {
                List<NameValuePair> params=new ArrayList<NameValuePair>();
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
                    System.out.println(data);
                    try {
                        JSONObject jsonObject=new JSONObject(data);
                        JSONArray events=jsonObject.getJSONArray("events");
                        for(int i=0; i<events.length(); i++){
                            JSONObject e=events.getJSONObject(i);
                            String k=e.getString("key");
                            String v=e.getString("value");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
//                    Log.d("aaaaaa",data);
                }
            }
        }.execute();
    }
}