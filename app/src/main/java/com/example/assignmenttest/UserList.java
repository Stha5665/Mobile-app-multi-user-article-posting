package com.example.assignmenttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.example.assignmenttest.model.PostModel;
import com.example.assignmenttest.myHandler.DBHelper;

import java.util.ArrayList;

public class UserList extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<PostModel> arrPosts;
    DBHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        db = new DBHelper(this);
        arrPosts = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        arrPosts = db.fetchPost();
        MyAdapter adapter = new MyAdapter(this,arrPosts);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}