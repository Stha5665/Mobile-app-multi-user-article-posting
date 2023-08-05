package com.example.assignmenttest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignmenttest.myHandler.DBHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Homepage extends AppCompatActivity {
    private TextView loggedUserName, loggedUserEmail, loggedUserRegisteredDate, loggedUserLastUpdatedDate;
    private Button nav_post, edit, logout, home;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        db = new DBHelper(this);
        nav_post = findViewById(R.id.postView);
        home = findViewById(R.id.homeView);
        loggedUserName = findViewById(R.id.LoggedUserName);
        loggedUserEmail = findViewById(R.id.loggedUserEmail);
        loggedUserRegisteredDate = findViewById(R.id.registeredDate);
        loggedUserLastUpdatedDate = findViewById(R.id.lastUpdatedDate);
        edit = findViewById(R.id.editProfile);
        logout = findViewById(R.id.logout);


        String activeUser = db.getActiveUser();
        Cursor cursor1 = db.getActiveUserDetails(activeUser);
                if(cursor1.getCount()==0){
                    Toast.makeText(this, "No entry exists", Toast.LENGTH_SHORT).show();
                }
                else {
                    while (cursor1.moveToNext()){
                        loggedUserName.setText("Hello!! " + cursor1.getString(1));
                        loggedUserEmail.setText("Email: " + cursor1.getString(3));
                        loggedUserRegisteredDate.setText("Date Registered: " + cursor1.getString(4));
                        loggedUserLastUpdatedDate.setText("Last Updated: " + cursor1.getString(5));
                    }
                }





        nav_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this, AddPost.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteActiveUser();
                Intent intent = new Intent(Homepage.this, MainActivity.class);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(Homepage.this);
                dialog.setContentView(R.layout.edit_profile);
                EditText editName = dialog.findViewById(R.id.editUserName);
                EditText editemail = dialog.findViewById(R.id.editUserEmail);
                Button btnAction = dialog.findViewById(R.id.btnAction);
                dialog.show();
                editName.setText(activeUser);
                editemail.setText((loggedUserEmail.getText().toString()).substring(7));
                // substring use here to print user email only
                btnAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "", email = "", dateUpdated = getDateTime();

                        if (!editName.getText().toString().equals("")) {
                            name = editName.getText().toString();
                        } else {
                            Toast.makeText(Homepage.this, "Please enter User name", Toast.LENGTH_SHORT).show();

                        }
                        if (!editemail.getText().toString().equals("")) {
                            email = editemail.getText().toString();
                        } else {
                            Toast.makeText(Homepage.this, "Please enter User Email", Toast.LENGTH_SHORT).show();
                        }

                        db.updateUserProfile(activeUser, name, email,dateUpdated);
                      // to refresh activity after successfull
                        finish();
                        startActivity(getIntent());
                        dialog.dismiss();
                    }
                });

            }
        });

    }

    private String getDateTime() {
        // defining format to get Date and time
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();//creating new Date object
        return dateFormat.format(date); // returning Date object on specified format
    }

    // for reloading on backpress
//
//    @Override
//    public void onRestart() {
//        super.onRestart();
//        //When BACK BUTTON is pressed, the activity on the stack is restarted
//        //Do what you want on the refresh procedure here
//    }
}