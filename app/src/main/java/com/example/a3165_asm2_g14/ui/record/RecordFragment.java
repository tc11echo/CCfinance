package com.example.a3165_asm2_g14.ui.record;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a3165_asm2_g14.DatabaseHelper;
import com.example.a3165_asm2_g14.R;
import com.example.a3165_asm2_g14.databinding.FragmentRecordBinding;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RecordFragment extends Fragment implements CalendarAdapter.OnItemListener, View.OnClickListener {

    //Database
    private DatabaseHelper recordDB;
    private ArrayList<String> record_id, record_date, record_type, record_amount, record_category, record_method, record_note;
    private ListView detailListView;

    //RecordFragment components
    private TextView tv_record_show_month, tv_record_show_selected_day;
    private Button backbtn, nextbtn;
    private RecyclerView calendarRecyclerView;
    public static LocalDate userSelectedDate;

    private RecordViewModel recordViewModel;
    private FragmentRecordBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RecordViewModel recordViewModel =
                new ViewModelProvider(this).get(RecordViewModel.class);

        binding = FragmentRecordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        userSelectedDate = LocalDate.now();
        //Database
        recordDB = new DatabaseHelper(getContext());
        record_id = new ArrayList<>();
        record_date = new ArrayList<>();
        record_type = new ArrayList<>();
        record_amount = new ArrayList<>();
        record_category = new ArrayList<>();
        record_method = new ArrayList<>();
        record_note = new ArrayList<>();
        getDataInDB();

        tv_record_show_selected_day = root.findViewById(R.id.tv_record_show_selected_day);
        tv_record_show_selected_day.setText(localeDateToString(userSelectedDate));
        calendarRecyclerView = root.findViewById(R.id.calendarRecyclerView);
        tv_record_show_month = root.findViewById(R.id.tv_record_show_month);

        backbtn = root.findViewById(R.id.previous_btn);
        backbtn.setOnClickListener(this);
        nextbtn = root.findViewById(R.id.next_btn);
        nextbtn.setOnClickListener(this);
        setMonthView();

        Log.d("thomas","recordfrag:\n"+
                String.valueOf(record_id)+"\n" +
                String.valueOf(record_date)+"\n" +
                String.valueOf(record_type)+"\n" +
                String.valueOf(record_amount)+"\n" +
                String.valueOf(record_category)+"\n" +
                String.valueOf(record_method)+"\n" +
                String.valueOf(record_amount)+"\n" +
                String.valueOf(record_note)+"\n");
        detailListView=root.findViewById(R.id.detailListView);
        RecordListViewAdapter adapter = new RecordListViewAdapter(this,localeDateToString(userSelectedDate), record_id, record_date, record_type, record_amount, record_category, record_method, record_note);
        detailListView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.previous_btn:
                userSelectedDate = userSelectedDate.minusMonths(1);
                setMonthView();
                RecordListViewAdapter adapter1 = new RecordListViewAdapter(this,localeDateToString(userSelectedDate), record_id, record_date, record_type, record_amount, record_category, record_method, record_note);
                detailListView.setAdapter(adapter1);
                break;
            case R.id.next_btn:
                userSelectedDate = userSelectedDate.plusMonths(1);
                setMonthView();
                RecordListViewAdapter adapter2 = new RecordListViewAdapter(this,localeDateToString(userSelectedDate), record_id, record_date, record_type, record_amount, record_category, record_method, record_note);
                detailListView.setAdapter(adapter2);
                break;
        }
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            userSelectedDate = date;
            tv_record_show_selected_day.setText(localeDateToString(userSelectedDate));
            setMonthView();

            RecordListViewAdapter adapter = new RecordListViewAdapter(this,localeDateToString(userSelectedDate), record_id, record_date, record_type, record_amount, record_category, record_method, record_note);
            detailListView.setAdapter(adapter);
        }
    }

    private void setMonthView() {
        tv_record_show_month.setText(monthYearFromDate(userSelectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(userSelectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(this,daysInMonth,record_date);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<LocalDate> daysInMonthArray(LocalDate date) {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = userSelectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        if (dayOfWeek == 7) {
            dayOfWeek = 0;
        }

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add(null);
            } else {
                daysInMonthArray.add(LocalDate.of(userSelectedDate.getYear(), userSelectedDate.getMonth(), i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    private String localeDateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        return date.format(formatter);
    }

    public void getDataInDB() {
        Cursor cursor = recordDB.readAllData();
        if (cursor.getCount() == 0) {
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
    }
}