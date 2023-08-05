package com.example.assignmenttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.example.assignmenttest.model.UserModel;
import com.example.assignmenttest.myHandler.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class LoggedUser extends AppCompatActivity {
    FloatingActionButton btnOpenDialog;
    DBHelper dbHelper;
    ArrayList<UserModel> arrUsers ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_user);
        btnOpenDialog = findViewById(R.id.btnOpenDialog);
        RecyclerView recyclerView = findViewById(R.id.loggedUserList);
        dbHelper = new DBHelper(this);
        arrUsers = new ArrayList<>();
        // Making recycler view of list

        // to show list in linear view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        arrUsers.add(new UserModel("A", "B", "2022"));
//        arrUsers.add(new UserModel("B", "B", "2022"));
//        arrUsers.add(new UserModel("C", "B", "2022"));
//        arrUsers.add(new UserModel("D", "B", "2022"));
//        arrUsers.add(new UserModel("A", "B", "2022"));
//        arrUsers.add(new UserModel("A", "B", "2022"));
//        arrUsers.add(new UserModel("A", "B", "2022"));
        arrUsers = dbHelper.fetchUser();

            UserRecycleAdapter adapter = new UserRecycleAdapter(this, arrUsers);
            recyclerView.setAdapter(adapter);



        btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //  adapter.notifyItemInserted(arrUsers.size()-1);
               //
               recyclerView.scrollToPosition(arrUsers.size()-1);

            }
        });


    }
}