package com.example.assignmenttest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.assignmenttest.myHandler.DBHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPost extends AppCompatActivity {
    EditText name, description;
    Button insert, view;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        insert = findViewById(R.id.btInsert);
        view = findViewById(R.id.btView);

        db = new DBHelper(this);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddPost.this, UserList.class));
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtName = "", txtDescription = "";
                String txtDate = getDateTime();
                if (!name.getText().toString().equals("")) {
                     txtName = name.getText().toString();
                }else {
                    Toast.makeText(AddPost.this, "Please enter post title ", Toast.LENGTH_SHORT).show();
                }

                txtDescription = description.getText().toString();
                String activeUser = db.getActiveUser();
                Boolean checkInsert = db.insertUserData(activeUser,txtName,txtDate,txtDescription);

                if(checkInsert == true){
                    Toast.makeText(AddPost.this, "New insert vlaue", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(AddPost.this, "Not inserted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getDateTime() {
        // defining format to get Date and time
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();//creating new Date object
        return dateFormat.format(date); // returning Date object on specified format
    }
}