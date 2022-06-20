package com.example.a3165_asm2_g14.ui.record;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a3165_asm2_g14.DatabaseHelper;
import com.example.a3165_asm2_g14.MainActivity;
import com.example.a3165_asm2_g14.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class RecordListViewAdapter extends ArrayAdapter<String> {

    private final RecordFragment recordFragment;
    private final String userSelectedDate;
    private final ArrayList<String>record_id, record_date, record_type, record_amount, record_category, record_method, record_note;

    public RecordListViewAdapter(RecordFragment recordFragment, String userSelectedDate, ArrayList<String> record_id, ArrayList<String> record_date, ArrayList<String> record_type, ArrayList<String> record_amount, ArrayList<String> record_category, ArrayList<String> record_method, ArrayList<String> record_note) {
        super(recordFragment.getContext(), R.layout.record_listview, record_id);

        this.recordFragment=recordFragment;
        this.userSelectedDate=userSelectedDate;
        this.record_id=record_id;
        this.record_date=record_date;
        this.record_type=record_type;
        this.record_amount=record_amount;
        this.record_category=record_category;
        this.record_method=record_method;
        this.record_note=record_note;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = recordFragment.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.record_listview, null, false);

        String temp=record_date.get(position);
        String[] arrOfData = temp.split("/");
        String date=arrOfData[0]+"/"+String.valueOf(Integer.parseInt(arrOfData[1])+1)+"/"+arrOfData[2];
        //Log.d("thomas",position+":"+userSelectedDate+"    "+date);

        if(userSelectedDate.equals(date)) {
            TextView tv_list_show_type = rowView.findViewById(R.id.tv_list_show_type);
            tv_list_show_type.setText(record_type.get(position));
            TextView tv_list_show_amount = rowView.findViewById(R.id.tv_list_show_amount);
            tv_list_show_amount.setText("$"+record_amount.get(position));
            TextView tv_list_show_category = rowView.findViewById(R.id.tv_list_show_category);
            tv_list_show_category.setText(record_category.get(position));

            Button btn_list_show_detail = rowView.findViewById(R.id.btn_list_show_detail);
            btn_list_show_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("thomas", "pass onclick");

                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(getContext());

                    String data = "ID: " + record_id.get(position) +"\n"+
                            "Date: " + date +"\n"+
                            "Type: " + record_type.get(position) +"\n"+
                            "Category: " + record_category.get(position) +"\n"+
                            "Method: " + record_method.get(position) +"\n"+
                            "Amount: $" + record_amount.get(position) +"\n"+
                            "Note: " + record_note.get(position);

                    builder.setMessage(data).setTitle("Record Detail").setCancelable(false)
                            .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Toast.makeText(getContext(), "you choose cancal action for alertbox", Toast.LENGTH_SHORT).show();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            Button btn_list_show_del = rowView.findViewById(R.id.btn_list_show_del);
            btn_list_show_del.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Log.d("thomas","pass onclick");

                    DatabaseHelper recordDB = new DatabaseHelper(getContext());
                    recordDB.deleteRecord(record_id.get(position));
                    Cursor cursor = recordDB.readAllData();

                    record_id.clear();
                    record_date.clear();
                    record_type.clear();
                    record_amount.clear();
                    record_category.clear();
                    record_method.clear();
                    record_note.clear();

                    if (cursor.getCount() == 0) {
                        //Intent intent = new Intent(getContext(), MainActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //getContext().startActivity(intent);
                    } else {
                        while (cursor.moveToNext()) {
                            record_id.add(cursor.getString(0));
                            record_date.add(cursor.getString(1));
                            record_type.add(cursor.getString(2));
                            record_amount.add(cursor.getString(3));
                            record_category.add(cursor.getString(4));
                            record_method.add(cursor.getString(5));
                            record_note.add(cursor.getString(6));
                        }
                    }
                    RecordListViewAdapter.this.notifyDataSetChanged();
                    Log.d("thomas", "getView:\n"+
                            String.valueOf(record_id)+"\n" +
                            String.valueOf(record_date)+"\n" +
                            String.valueOf(record_type)+"\n" +
                            String.valueOf(record_amount)+"\n" +
                            String.valueOf(record_category)+"\n" +
                            String.valueOf(record_method)+"\n" +
                            String.valueOf(record_amount)+"\n" +
                            String.valueOf(record_note)+"\n");

                    Toast.makeText(getContext().getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            rowView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1));
            rowView.setVisibility(View.GONE);

        }
        return rowView;
    }
}
