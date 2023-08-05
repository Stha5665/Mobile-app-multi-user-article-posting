package com.example.assignmenttest;

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

import com.example.assignmenttest.model.PostModel;
import com.example.assignmenttest.myHandler.DBHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
   private Context context;
   private ArrayList<PostModel> arrPosts;
   DBHelper dbHelper;
   private String activeUser;

    public MyAdapter(Context context,ArrayList<PostModel> arrPosts) {
        this.context = context;
        this.arrPosts = arrPosts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.userentry,parent,false);
        dbHelper = new DBHelper(context);
        activeUser = dbHelper.getActiveUser();
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PostModel model = (PostModel) arrPosts.get(holder.getAdapterPosition());
        holder.user_id.setText(String.valueOf(model.username));
        holder.name_id.setText(String.valueOf(model.title));
        holder.date_id.setText(String.valueOf(model.date));
        holder.description_id.setText(String.valueOf(model.description));

        if(model.username.equals(activeUser)) {
            holder.postRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.update_post);
                    EditText editTitleOfPost = dialog.findViewById(R.id.editPostTitle);
                    EditText description = dialog.findViewById(R.id.editPostDescription);
                    Button btnAction = dialog.findViewById(R.id.btnAction);

                    editTitleOfPost.setText(arrPosts.get(holder.getAdapterPosition()).title);
                    description.setText(arrPosts.get(holder.getAdapterPosition()).description);


                    btnAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String title = "", description1 = "", dateUpdated = getDateTime();

                            if (!editTitleOfPost.getText().toString().equals("")) {
                                title = editTitleOfPost.getText().toString();
                            } else {
                                Toast.makeText(context, "Please enter User name", Toast.LENGTH_SHORT).show();

                            }
                            if (!description.getText().toString().equals("")) {
                                description1 = description.getText().toString();
                            } else {
                                Toast.makeText(context, "Please enter User Email", Toast.LENGTH_SHORT).show();

                            }
                            arrPosts.set(holder.getAdapterPosition(), new PostModel(model.id, title, dateUpdated, description1, activeUser));
                            dbHelper.updateUserPost(model, title, description1, dateUpdated);
                            notifyItemChanged(holder.getAdapterPosition());
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }


            });

            holder.postRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    String activeUser = dbHelper.getActiveUser();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context)
                            .setTitle("Delete User")
                            .setMessage("Are you sure want to delete user")
                            .setIcon(R.drawable.ic_baseline_delete_24)
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    arrPosts.remove(holder.getAdapterPosition());
                                    dbHelper.deleteUserPost(model);
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
    }


    @Override
    public int getItemCount() {
        return arrPosts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView user_id, name_id, date_id, description_id;
        LinearLayout postRow;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name_id = itemView.findViewById(R.id.textname);
            date_id = itemView.findViewById(R.id.textdate);
            description_id = itemView.findViewById(R.id.textdescription);
            user_id = itemView.findViewById(R.id.textUsername);
            postRow = itemView.findViewById(R.id.post_row);

        }
    }

    private String getDateTime() {
        // defining format to get Date and time
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();//creating new Date object
        return dateFormat.format(date); // returning Date object on specified format
    }
}
