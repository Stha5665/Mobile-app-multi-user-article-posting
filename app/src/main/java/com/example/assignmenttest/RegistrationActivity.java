package com.example.assignmenttest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.assignmenttest.myHandler.DBHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistrationActivity extends AppCompatActivity {

    private EditText eRegName;// variable for linking with xml component
    private EditText eRegPassword; // variable for linking
    private EditText eEmail;
    private Button eSubmit; // variable for linking submit button
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        eRegName = findViewById(R.id.elementRegName); // linking with xml
        eRegPassword = findViewById(R.id.elementRegPassword); // linking Password field with xml
        eEmail = findViewById(R.id.elementEmail);
        eSubmit = findViewById(R.id.elemSubmit); // Linking submit button

        db = new DBHelper(this);

        // adding onClickListener on eSubmit button
        eSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting user input userName
                String registerUserName = eRegName.getText().toString();
                String registerPassword = eRegPassword.getText().toString();// getting user input password
                String email = eEmail.getText().toString();
                String date = getDateTime();
                if(isValid(registerUserName,registerPassword)){  // authorized user' user name and password is set
                    Boolean checkuser = db.checkusername(registerUserName);
                    if(checkuser == false){
                        Boolean insert = db.addUser(registerUserName,registerPassword,email,date,date); // storing valid user input data in credential class

                        if(insert){
                            Toast.makeText(RegistrationActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            // Toast message showing Registered successfully message

                        }
                    }
                    else{
                        Toast.makeText(RegistrationActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }
    // isValid method confirm whether user input is valid or not
    private boolean isValid(String userName, String password)
    {
        // return false for empty username and password length smaller than 8 characters
        if(userName.isEmpty() || password.length() < 8){
            Toast.makeText(this, "Please enter all details, password should be minmum 8 character long", Toast.LENGTH_SHORT).show();
            return false; // returning false
        }
        return true; // if user input field i.e. userName is not empty and password is greater than 8 characters then return true
    }

    // Method to get current system Date and time
    private String getDateTime() {
        // defining format to get Date and time
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();//creating new Date object
        return dateFormat.format(date); // returning Date object on specified format
    }
// end of REgistrationActivity
}
