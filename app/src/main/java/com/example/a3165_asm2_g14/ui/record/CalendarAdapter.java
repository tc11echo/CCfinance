package com.example.a3165_asm2_g14.ui.record;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a3165_asm2_g14.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

    private final OnItemListener onItemListener;
    private final ArrayList<LocalDate> days;
    private final ArrayList<String> record_date;

    public CalendarAdapter(OnItemListener onItemListener,ArrayList<LocalDate> days,ArrayList<String> record_date) {
        this.onItemListener = onItemListener;
        this.days = days;
        this.record_date = record_date;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);

        return new CalendarViewHolder(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position);

        if (date == null){
            holder.day_cell.setText("");
            holder.contain_record.setVisibility(View.GONE);
        }
        else {
            String temp=localeDateToString(date);
            String[] arrOfData = temp.split("/");
            String strdate=arrOfData[0]+"/"+String.valueOf(Integer.parseInt(arrOfData[1])-1)+"/"+arrOfData[2];
            holder.day_cell.setText(String.valueOf(date.getDayOfMonth()));
            if (date.equals(RecordFragment.userSelectedDate)) {
                holder.cell_constrain_view.setBackgroundColor(Color.LTGRAY);
            }
            if(!record_date.contains(strdate)){
                holder.contain_record.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface OnItemListener {
        void onItemClick(int position, LocalDate date);
    }

    private String localeDateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        return date.format(formatter);
    }
}