package com.example.event_management;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Signup extends AppCompatActivity {

    EditText name, email, phone, userid, password, repassword;
    CheckBox checkBoxUser, checkBoxPass;
    Button btnExit, btnGo;
    TextView loginMsg, login, singup_title, clean;
    TableRow row_name, row_email, row_phone, row_userid, row_password, row_repassword;

    Boolean checkUser = false, checkPass = false, flg=false, flgLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        singup_title = findViewById(R.id.singup_title);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        userid = findViewById(R.id.userid);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);

        checkBoxUser = findViewById(R.id.checkBoxUser);
        checkBoxPass = findViewById(R.id.checkBoxPass);

        btnExit = findViewById(R.id.btnExit);
        btnGo = findViewById(R.id.btnGo);

        loginMsg = findViewById(R.id.loginMsg);
        login = findViewById(R.id.login);

        row_name = findViewById(R.id.row_name);
        row_email = findViewById(R.id.row_email);
        row_phone = findViewById(R.id.row_phone);
        row_userid = findViewById(R.id.row_userid);
        row_password = findViewById(R.id.row_password);
        row_repassword = findViewById(R.id.row_repassword);
        clean = findViewById(R.id.clean);

        clean.setVisibility(View.GONE);

        SharedPreferences sharedPref = this.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String userid_str_sp = sharedPref.getString("userid", "");
        String password_str_sp = sharedPref.getString("password", "");

        if(!userid_str_sp.isEmpty()){
            row_name.setVisibility(View.GONE);
            row_email.setVisibility(View.GONE);
            row_phone.setVisibility(View.GONE);
            row_repassword.setVisibility(View.GONE);
            clean.setVisibility(View.VISIBLE);
            loginMsg.setText("Register Now.");
            singup_title.setText("Log In");
            login.setText(getResources().getString(R.string.signup_text_underline));

            String checkUser_str_sp = sharedPref.getString("checkUser", "");
            String checkPass_str_sp = sharedPref.getString("checkPass", "");

            if(!checkUser_str_sp.isEmpty()){
                userid.setText(userid_str_sp);
            }
            if(!checkPass_str_sp.isEmpty()){
                password.setText(password_str_sp);
            }

            flg = true;

        }

        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String login_str = login.getText().toString().trim();
                //System.out.println(login_str);
                if(login_str.contains("Login")){
                    flg = true;

                    row_name.setVisibility(View.GONE);
                    row_email.setVisibility(View.GONE);
                    row_phone.setVisibility(View.GONE);
                    row_repassword.setVisibility(View.GONE);
                    clean.setVisibility(View.VISIBLE);
                    loginMsg.setText("Register Now.");
                    singup_title.setText("Log In");
                    login.setText(getResources().getString(R.string.signup_text_underline));

                }
                else{
                    row_name.setVisibility(View.VISIBLE);
                    row_email.setVisibility(View.VISIBLE);
                    row_phone.setVisibility(View.VISIBLE);
                    row_repassword.setVisibility(View.VISIBLE);
                    clean.setVisibility(View.GONE);
                    loginMsg.setText("Already have account? ");
                    login.setText(getResources().getString(R.string.login_text_underline));
                    singup_title.setText("Sign Up");

                    userid.setText("");
                    password.setText("");
                }
            }
        });

        btnGo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                if(flg){
                    String userid_str = userid.getText().toString();
                    String password_str = password.getText().toString();

                    if(userid_str.equals(userid_str_sp) && password_str.equals(password_str_sp)){
                        Intent i = new Intent(Signup.this, MainActivity.class);
                        startActivity(i);
                        finish();

                    }
                    return;
                }

                String name_str = name.getText().toString();
                String email_str = email.getText().toString();
                String phone_str = phone.getText().toString();
                String userid_str = userid.getText().toString();
                String password_str = password.getText().toString();
                String repassword_str = repassword.getText().toString();
                String errMsgAlert = "";

                if(checkBoxUser.isChecked()){
                    checkUser = true;
                }

                if(checkBoxPass.isChecked()){
                    checkPass = true;
                }

                int cnt = 0;

                if(name_str.length() < 3){
                    errMsgAlert += "Name, ";
                    cnt++;
                }
                if(email_str.isEmpty()){
                    errMsgAlert += "Email, ";
                    cnt++;
                }
                if(phone_str.isEmpty()){
                    errMsgAlert += "Phone, ";
                    cnt++;
                }

                if(!password_str.equals(repassword_str)){
                    errMsgAlert += "Password, ";
                    cnt++;
                }

                if(cnt > 0){
                    errMsgAlert = errMsgAlert.substring(0, errMsgAlert.length() - 2);
                    if(cnt>1){
                        errMsgAlert += " fields are Invalid";
                    }
                    else{
                        errMsgAlert += " field is Invalid";
                    }

                    showDialog(errMsgAlert, "Error", "Okay", "Back");
                    return;
                }
                else{
                    showDialog("Do you want to save this info?", "Info", "Yes", "No");
                }

                SharedPreferences.Editor prefsEditor = sharedPref.edit();
                prefsEditor.putString("name", name_str);
                prefsEditor.putString("email", email_str);
                prefsEditor.putString("phone", phone_str);
                prefsEditor.putString("userid", userid_str);
                prefsEditor.putString("password", password_str);
                if(checkUser){
                    prefsEditor.putString("checkUser", "True");
                }
                if(checkPass){
                    prefsEditor.putString("checkPass", "True");
                }
                prefsEditor.apply();
            }
        });

        clean.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                SharedPreferences.Editor prefsEditor = sharedPref.edit();
                prefsEditor.clear();
                prefsEditor.apply();
                flg=false;
                login.performClick();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });

    }

    private void showDialog(String message, String title, String btn1, String btn2){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);

        builder.setCancelable(false)
                .setPositiveButton(btn1, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        flg = true;
                        login.performClick();
                        dialog.cancel();
                    }
                })
                .setNegativeButton(btn2, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}