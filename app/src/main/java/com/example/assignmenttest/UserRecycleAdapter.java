package com.example.assignmenttest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignmenttest.model.UserModel;
import com.example.assignmenttest.myHandler.DBHelper;

import java.util.ArrayList;

public class UserRecycleAdapter extends RecyclerView.Adapter<UserRecycleAdapter.ViewHolder> {

    Context context;
    ArrayList<UserModel> arrUsers;
    DBHelper dbHelper;


    public UserRecycleAdapter(Context context, ArrayList<UserModel> arrUsers) {
        this.context = context;
        this.arrUsers = arrUsers;
    }

    @NonNull
    @Override
    // Oncreate to create
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       // context get from main class so use constructor to sett context
        // inflate means to include layout inside layout
        // we have to recycle view when scrolling so attachto bottom is false
        View v = LayoutInflater.from(context).inflate(R.layout.logged_user_row, parent, false);
        dbHelper = new DBHelper(context);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
        // returning viewholder views
        // to generate view use inflater
    }

    // data binding here
    // binding with xml
    //Finding id's
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //data sets according to the position accept as parameter
        // this has holder class reference which find id with xml
        // index of dataset
      //  for (int i = 0; i < arrUsers.size(); i ++) {
            UserModel model = (UserModel) arrUsers.get(holder.getAdapterPosition());
            holder.txtName.setText(model.userName);
            holder.txtEmail.setText(model.email);
            holder.txtDate_Registered.setText(model.date_registered);
       // }
        holder.userRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_upate_user_layout);
                EditText editName = dialog.findViewById(R.id.editUserName);
                EditText editemail = dialog.findViewById(R.id.editUserEmail);
                EditText editDate = dialog.findViewById(R.id.editUserDateRegistered);
                Button btnAction = dialog.findViewById(R.id.btnAction);


                editName.setText(arrUsers.get(holder.getAdapterPosition()).userName);
                editemail.setText(arrUsers.get(holder.getAdapterPosition()).email);
                editDate.setText(arrUsers.get(holder.getAdapterPosition()).date_registered);
                btnAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "", email="", dateRegistered="";

                        if (!editName.getText().toString().equals("")){
                            name = editName.getText().toString();
                        }else {
                            Toast.makeText(context,"Please enter User name", Toast.LENGTH_SHORT).show();

                        }
                        if (!editemail.getText().toString().equals("")){
                            email = editemail.getText().toString();
                        }else {
                            Toast.makeText(context,"Please enter User Email", Toast.LENGTH_SHORT).show();

                        }
                        if (!editDate.getText().toString().equals("")){
                            dateRegistered = editDate.getText().toString();
                        }else {
                            Toast.makeText(context,"Please enter date Registered", Toast.LENGTH_SHORT).show();

                        }

                        arrUsers.set(holder.getAdapterPosition(), new UserModel(name,email,dateRegistered));
                        dbHelper.updateUser(model, name, email, dateRegistered);
                        notifyItemChanged(holder.getAdapterPosition());
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        holder.userRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Delete User")
                        .setMessage("Are you sure want to delete user")
                        .setIcon(R.drawable.ic_baseline_delete_24)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                arrUsers.remove(holder.getAdapterPosition());
                                dbHelper.deleteUser(model);
                                notifyItemRemoved(holder.getAdapterPosition());
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.show();

                return true;
            }
        });
    }

    //getting upto last element
    @Override
    public int getItemCount() {
        return arrUsers.size();
    }

    // This contain view holder
    // to show data in xml
    // viewholder recycle to reuse layout
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtName, txtEmail, txtDate_Registered;
        LinearLayout userRow;
        // user row to manage clickable in list

        // itemView has ids for xml text view so finding by here
        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.usertxtName);
            txtEmail = itemView.findViewById(R.id.usertxtemail);
            txtDate_Registered = itemView.findViewById(R.id.userdate_registered);
            userRow = itemView.findViewById(R.id.Row11);
        }
    }

}
