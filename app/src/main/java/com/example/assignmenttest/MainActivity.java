package com.example.assignmenttest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignmenttest.myHandler.DBHelper;

public class MainActivity extends AppCompatActivity {

    // for userName
    private EditText eUserName;
    //for getting password
    private EditText ePassword;
    private Button eLogin; // Login button
    private TextView eNoOfAtemptsInfo; // No of attempts remaining to login, if you entered wrong username or password

    private TextView eRegister;
    DBHelper dbHelper;
    // If new user you can get register by clicking this button
//        public static Credentials credentials = new Credentials();
    //Credentials to get or input user login or password

    int counter = 5;
    // counter variable to detect no of attempt remaining
    boolean isValid = false;
    // check validation of user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // linking variable by using id
        eUserName = findViewById(R.id.elementUserName);
        // linking edit text with ePassword
        ePassword = findViewById(R.id.elemLoginPassword);
        // Linking login button
        eLogin = findViewById(R.id.elementLogin);
        // Linking textview
        eNoOfAtemptsInfo = findViewById(R.id.elementtvAtemptsInfo);
        // linking textview that navigates to register activity
        eRegister = findViewById(R.id.elemRegister);
        dbHelper = new DBHelper(this);

        dbHelper.deleteActiveUser();

        eRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                // then move to new activity named as REgistrationActivity
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
                // Move from this class to REgistrationActivity class
            }
        });

        // If login button is clicked following action is proccessed
        eLogin.setOnClickListener(new View.OnClickListener() {
            // getting OnClick method
            @Override
            public void onClick(View view) {
                // Getting userName from EditText
                String logininputusername = eUserName.getText().toString();
                //Getting password from EditText of main activity
                String loginInputPassword = ePassword.getText().toString();

                // check whether user input userName or password is empty or not
                if (logininputusername.isEmpty() || loginInputPassword.isEmpty()) {
                    // if it is found empty then text message with "Please enter all details correctly is shown.
                    Toast.makeText(MainActivity.this, "Plese enter all details correctly", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean isValid = dbHelper.checkUserNamePassword(logininputusername, loginInputPassword);
                    if (isValid) {

//                        Boolean insert = dbHelper.addUser(logininputusername, loginInputPassword);
                        //show Login successfull toast message
                        Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                        //Add the code to go to new Activity
                        Intent intent = new Intent(MainActivity.this, Homepage.class);
//                        intent.putExtra("keyname", logininputusername);
                        startActivity(intent);
                        // startActivity means move to new Activity named Homepage
                    }
                    else if(logininputusername.equals("admin") && loginInputPassword.equals("admin")){
                        Intent intent = new Intent(MainActivity.this, LoggedUser.class);
                        startActivity(intent);
                    }
                    else {
                        // if user input data do not match with registered data then decrease counter variable
                        counter--;
                        // and show Incorrect credentials entered message
                        Toast.makeText(MainActivity.this, "Incorrect credentials entered!", Toast.LENGTH_SHORT).show();
                        // The text view shows the no of attempts remaining if incorrect details are entered
                        eNoOfAtemptsInfo.setText("No Of attempts remaining: " + counter);
                        // if counter is zero then disable login button
                        if (counter == 0) {
                            // Now user cannot get logged in
                            eLogin.setEnabled(false);
                        }
                    }
                }
            }
        });

    }
}