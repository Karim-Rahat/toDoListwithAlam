package com.example.mytodoapplication;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
   //initialize variables

    private List<MainData> dataList;
    private final Activity context;
    private  RoomDB database;

    AlertDialog.Builder builder;


    @SuppressLint("NotifyDataSetChanged")
    public MainAdapter(List<MainData> dataList, Activity context) {
        this.dataList = dataList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //intilaize view
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_main,parent,false);


        return new ViewHolder(view);
    }
    void setFilter(List<MainData> FilteredDataList) {
        dataList = FilteredDataList;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        //int main data
        MainData data=dataList.get(position);
        //init db
        database=RoomDB.getInstance(context);
        //set text in textview
        holder.textView.setText(data.getText());
        holder.mDate.setText(data.getDate());
        holder.mTime.setText(data.getTime());
        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //init main data
                MainData d=dataList.get(holder.getAdapterPosition());
                //get id
                int sID = d.getID();
                //get text
                String sText = d.getText();
                                       //access the time form the choose time button
                //create dialog
                Dialog dialog = new Dialog(context);
                // set content view

                dialog.setContentView(R.layout.dialog_update);

                //init width

                int width= WindowManager.LayoutParams.MATCH_PARENT;
                //int height

                int height=WindowManager.LayoutParams.WRAP_CONTENT;
                //set layout

                dialog.getWindow().setLayout(width,height);

                //show dialog
                Intent intent = new Intent(context.getApplicationContext(), ReminderActivity.class);
                intent.putExtra("key",sID);
                intent.putExtra("text",sText);
                intent.putExtra("className","update");
                intent.putExtra("date",data.getDate());
                intent.putExtra("time",data.getTime());

                context.startActivity(intent);
                dialog.show();

                //init and assign variable

                EditText editText=dialog.findViewById(R.id.edit_text);
                Button btUpdate=dialog.findViewById(R.id.bt_update);

                //set text on edit text

                editText.setText(sText);

                btUpdate.setOnClickListener(new View.OnClickListener() {

                    @SuppressLint("NotifyDataSetChanged")

                    @Override
                    public void onClick(View v) {




                    }
                });




            }


        });

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder= new AlertDialog.Builder(v.getContext());
                //Setting message manually and performing action on button click
                builder.setMessage("Are you sure you want to delete this task?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                MainData d=dataList.get(holder.getAdapterPosition());
                                //delete text from database

                                database.mainDao().delete(d);
                                //notify when data is deleted
                                int position = holder.getAdapterPosition();
                                dataList.remove(position);

                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,dataList.size());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("DeleteConfirmation");
                alert.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //init varivles

        TextView textView,mDate,mTime;
        ImageView btEdit,btDelete;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            //Assign varable
            textView=itemView.findViewById(R.id.text_view);
            btEdit=itemView.findViewById(R.id.bt_edit);
            btDelete=itemView.findViewById(R.id.bt_delete);
                                     //holds the reference of the materials to show data in recyclerview
            mDate =  itemView.findViewById(R.id.txtDate);
            mTime =  itemView.findViewById(R.id.txtTime);



        }
    }


}
