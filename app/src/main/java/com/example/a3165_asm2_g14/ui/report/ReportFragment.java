package com.example.a3165_asm2_g14.ui.report;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a3165_asm2_g14.DatabaseHelper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.a3165_asm2_g14.R;
import com.example.a3165_asm2_g14.databinding.FragmentReportBinding;
import com.google.android.material.button.MaterialButtonToggleGroup;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.*;
import java.util.Calendar;

public class ReportFragment extends Fragment {

    private FragmentReportBinding binding;
    private DatePickerDialog datePickerDialog,datePickerDialog2;
    private Button btn_inc,btn_exp,btn_enter;
    private Button btn_date0,btn_date1,btn_date2,btn_date3,btn_date4,btn_date5, btn_date6;
    private int reportype; //daily,monthly
    private int getyear,getmonth,getday,getyear1,getmonth1,getday1;
    //Database
    private DatabaseHelper recordDB;
    private ArrayList<String> record_id, record_date, record_type, record_amount, record_category, record_method, record_note;
    private ArrayList<String> dayarray = new ArrayList<String>();
    private ArrayList<String> montharray = new ArrayList<String>();
    private ArrayList<String> yeararray = new ArrayList<String>();
    private ArrayList<String> salarysum = new ArrayList<String>();
    private ArrayList<String> investsum = new ArrayList<String>();
    private ArrayList<String> otherssum = new ArrayList<String>();
    private ArrayList<String> grocersum = new ArrayList<String>();
    private ArrayList<String> transpsum = new ArrayList<String>();
    private ArrayList<String> entertainsum = new ArrayList<String>();
    private ArrayList<String> billsum = new ArrayList<String>();
    private ArrayList<String> otherssum1 = new ArrayList<String>();
    private String textofscale,textofcategory, recordstext ,textofmoney;
    private String deposit = "Income";
    private Calendar cal = Calendar.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReportViewModel dashboardViewModel =
                new ViewModelProvider(this).get(ReportViewModel.class);

        binding = FragmentReportBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    public void onStart() {
        super.onStart();
        //Reset checked button in toggle group
        MaterialButtonToggleGroup toggleGroup = getView().findViewById(R.id.togglegroup);
        MaterialButtonToggleGroup toggleGroup1 = getView().findViewById(R.id.togglegroup1);
        toggleGroup.clearChecked();
        toggleGroup1.clearChecked();
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
        //set title
        getActivity().setTitle("Report");
        //initial components
        PieChart mPieChart = (PieChart) getView().findViewById(R.id.piechart);
        btn_inc = (Button) getView().findViewById(R.id.btn_inc);
        btn_exp = (Button) getView().findViewById(R.id.btn_exp);btn_date0= (Button) getView().findViewById(R.id.btn_date0);
        btn_date1 = (Button) getView().findViewById(R.id.btn_date1);
        btn_date2 = (Button) getView().findViewById(R.id.btn_date2);
        btn_date3 = (Button) getView().findViewById(R.id.btn_date3);
        btn_date4 = (Button) getView().findViewById(R.id.btn_date4);
        btn_date5 = (Button) getView().findViewById(R.id.btn_date5);
        btn_date6 = (Button) getView().findViewById(R.id.btn_date6);
        btn_enter = (Button) getView().findViewById(R.id.btn_enter);

        TableRow monthpicker = (TableRow) getView().findViewById(R.id.monthpicker);
        TableRow daypicker = (TableRow) getView().findViewById(R.id.daypicker);
        TableRow datepicker = (TableRow) getView().findViewById(R.id.datepicker);
        TextView indextext = (TextView) getView().findViewById(R.id.indextext);
        TextView eventtext = (TextView) getView().findViewById(R.id.eventtext);
        TextView moneytext = (TextView) getView().findViewById(R.id.moneytext);
        TextView recordtext = (TextView) getView().findViewById(R.id.recordnotext);

        initDatePicker();
        initDatePicker2();
        btn_date1.setText(getTodaydDate());
        btn_date4.setText(getTodaydDate());
        btn_date5.setText(getTodaydDate());
        btn_date6.setText(getTodaydMonth());
        getyear =  cal.get(Calendar.YEAR)  ;
        getmonth = cal.get(Calendar.MONTH)+1;
        getday = cal.get(Calendar.DAY_OF_MONTH);
        getyear1 =   cal.get(Calendar.YEAR) ;
        getmonth1 = cal.get(Calendar.MONTH)+1;
        getday1 = cal.get(Calendar.DAY_OF_MONTH);

        mPieChart.addPieSlice(new PieModel("Click Button To view Report", 0, Color.parseColor("#FE6DA8")));


        //set button activities
        btn_inc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                deposit = "Income";
            }           //income button
        });

        btn_exp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                deposit = "Expense";
            }           //expense button
        });


        btn_date0.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {                               //Daily button
                datepicker .setVisibility(View.GONE);
                monthpicker.setVisibility(View.GONE);
                daypicker.setVisibility(View.VISIBLE);
                reportype = 1;
            }
        });

        btn_date1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }      //Pick Date button
        });

        btn_date2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                datepicker .setVisibility(View.GONE);
                monthpicker.setVisibility(View.VISIBLE);                     //monthly button
                daypicker.setVisibility(View.GONE);
                reportype = 2;
            }
        });
        btn_date3.setOnClickListener(new View.OnClickListener(){     //customize button
            @Override
            public void onClick(View v) {
                daypicker.setVisibility(View.GONE);
                monthpicker.setVisibility(View.GONE);
                datepicker .setVisibility(View.VISIBLE);
                reportype = 3;
            }
        });
        btn_date4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }      //Pick Date button
        });

        btn_date5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                datePickerDialog2.show();
            }     //Pick Date button
        });

        btn_date6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }      //Pick Date button2
        });

        btn_enter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {                                //Comfirm Date button
                salarysum.clear();
                investsum.clear();
                otherssum.clear();
                grocersum.clear();
                transpsum.clear();
                entertainsum.clear();
                billsum.clear();
                otherssum1.clear();
                textofcategory = "Category";
                recordstext = "Records";
                textofscale = "Scale";
                textofmoney = "Money";
                mPieChart.clearChart();

                for (int i = 0; i < record_id.size(); i++) {
                    if (reportype == 1) {                         //check daily, monthly or customize date, then check date of the record
                        if ((Integer.parseInt(dayarray.get(i)) == getday) & (Integer.parseInt(montharray.get(i)) + 1 == getmonth) & (Integer.parseInt(yeararray.get(i)) == getyear) & (record_type.get(i).equals(deposit))) {
                            if (record_category.get(i).equals("Salary")) {                             //check category
                                salarysum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Investment")) {
                                investsum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Others")) {
                                otherssum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Groceries")) {
                                grocersum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Transportation")) {
                                transpsum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Entertainment")) {
                                entertainsum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Bill Payment")) {
                                billsum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Other")) {
                                otherssum1.add(record_amount.get(i));
                            }

                        }
                    }
                    if (reportype == 2) {  //monthly

                        if ((Integer.parseInt(montharray.get(i))+1 == getmonth) & (Integer.parseInt(yeararray.get(i)) == getyear) & (record_type.get(i).equals(deposit))) {
                            if (record_category.get(i).equals("Salary")) {
                                salarysum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Investment")) {
                                investsum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Others")) {
                                otherssum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Groceries")) {
                                grocersum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Transportation")) {
                                transpsum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Entertainment")) {
                                entertainsum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Bill Payment")) {
                                billsum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Other")) {
                                otherssum1.add(record_amount.get(i));
                            }

                        }
                    }

                    if (reportype == 3) { //customize

                        int dayval = Integer.parseInt(dayarray.get(i));
                        int monthval = Integer.parseInt(montharray.get(i));
                        int yearval = Integer.parseInt(yeararray.get(i));

                        if ((getdateval(dayval, monthval, yearval) >= getdateval(getday, getmonth-1, getyear)) &
                                (getdateval(dayval, monthval, yearval) <= getdateval(getday1, getmonth1-1, getyear1) ) &(record_type.get(i).equals(deposit))) {
                            if (record_category.get(i).equals("Salary")) {
                                salarysum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Investment")) {
                                investsum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Others")) {
                                otherssum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Groceries")) {
                                grocersum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Transportation")) {
                                transpsum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Entertainment")) {
                                entertainsum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Bill Payment")) {
                                billsum.add(record_amount.get(i));
                            }
                            if (record_category.get(i).equals("Other")) {
                                otherssum1.add(record_amount.get(i));
                            }


                        }
                    }

                    //Set display Text
                }
                if (deposit.equals("Income")) {
                    if (salarysum.size() != 0) {
                        textofcategory = textofcategory + "\n" + "Salary";       //if have record, display category
                        recordstext = recordstext + "\n" + salarysum.size();    //if have record, display amount of record
                        textofscale = textofscale + "\n" + String.valueOf(Math.round((sumAllInlist(salarysum) * 100 / (sumAllInlist(salarysum) + sumAllInlist(investsum) + sumAllInlist(otherssum))) * 100.0) / 100.0) + "%";//if have record, display scale
                        textofmoney = textofmoney + "\n" + "$"+ sumAllInlist(salarysum);   //if have record, display amount of money
                        mPieChart.addPieSlice(new PieModel("Salary", Float.parseFloat(String.valueOf(sumAllInlist(salarysum))), Color.parseColor("#f58d42")));//if have record, add value into piechart
                    }
                    if (investsum.size() != 0) {
                        textofcategory = textofcategory + "\n" + "Investment";
                        recordstext = recordstext + "\n" + investsum.size();
                        textofscale = textofscale + "\n" + String.valueOf(Math.round((sumAllInlist(investsum) * 100 / (sumAllInlist(salarysum) + sumAllInlist(investsum) + sumAllInlist(otherssum))) * 100.0) / 100.0) + "%";
                        textofmoney = textofmoney + "\n" + "$"+ sumAllInlist(investsum);
                        mPieChart.addPieSlice(new PieModel("Investment", Float.parseFloat(String.valueOf(sumAllInlist(investsum))), Color.parseColor("#c5f542")));
                    }
                    if (otherssum.size() != 0) {
                        textofcategory = textofcategory + "\n" + "Others";
                        recordstext = recordstext + "\n" + otherssum.size();
                        textofscale = textofscale + "\n" + String.valueOf(Math.round((sumAllInlist(otherssum) * 100 / (sumAllInlist(salarysum) + sumAllInlist(investsum) + sumAllInlist(otherssum))) * 100.0) / 100.0) + "%";
                        textofmoney = textofmoney + "\n" + "$"+ sumAllInlist(otherssum);
                        mPieChart.addPieSlice(new PieModel("Others", Float.parseFloat(String.valueOf(sumAllInlist(otherssum))), Color.parseColor("#42f5c5")));
                    }
                }

                    if (deposit.equals("Expense")) {
                        if (grocersum.size() != 0) {
                            textofcategory = textofcategory + "\n" + "Groceries";
                            recordstext = recordstext + "\n" + grocersum.size();
                            textofscale = textofscale + "\n" + String.valueOf(Math.round((sumAllInlist(grocersum) * 100 / (sumAllInlist(transpsum) + sumAllInlist(entertainsum) + sumAllInlist(billsum) + sumAllInlist(otherssum1) + sumAllInlist(grocersum))) * 100.0) / 100.0) + "%";
                            textofmoney = textofmoney + "\n" + "$"+ sumAllInlist(grocersum);
                            mPieChart.addPieSlice(new PieModel("Groceries", Float.parseFloat(String.valueOf(sumAllInlist(grocersum))), Color.parseColor("#f58d42")));
                        }
                        if (transpsum.size() != 0) {
                            textofcategory = textofcategory + "\n" + "Transport";
                            recordstext = recordstext + "\n" + transpsum.size();
                            textofscale = textofscale + "\n" + String.valueOf(Math.round((sumAllInlist(transpsum) * 100 / (sumAllInlist(transpsum) + sumAllInlist(entertainsum) + sumAllInlist(billsum) + sumAllInlist(otherssum1) + sumAllInlist(grocersum))) * 100.0) / 100.0) + "%";
                            textofmoney = textofmoney + "\n" + "$"+ sumAllInlist(transpsum);
                            mPieChart.addPieSlice(new PieModel("Transport", Float.parseFloat(String.valueOf(sumAllInlist(transpsum))), Color.parseColor("#c5f542")));
                        }
                        if (entertainsum.size() != 0) {
                            textofcategory = textofcategory + "\n" + "Entertain";
                            recordstext = recordstext + "\n" + entertainsum.size();
                            textofscale = textofscale + "\n" + String.valueOf(Math.round((sumAllInlist(entertainsum) * 100 / (sumAllInlist(transpsum) + sumAllInlist(entertainsum) + sumAllInlist(billsum) + sumAllInlist(otherssum1) + sumAllInlist(grocersum))) * 100.0) / 100.0) + "%";
                            textofmoney = textofmoney + "\n" + "$"+ sumAllInlist(entertainsum);
                            mPieChart.addPieSlice(new PieModel("Entertain", Float.parseFloat(String.valueOf(sumAllInlist(entertainsum))), Color.parseColor("#42f5c5")));
                        }
                        if (billsum.size() != 0) {
                            textofcategory = textofcategory + "\n" + "Bill Payment";
                            recordstext = recordstext + "\n" + billsum.size();
                            textofscale = textofscale + "\n" + String.valueOf(Math.round((sumAllInlist(billsum) * 100 / (sumAllInlist(transpsum) + sumAllInlist(entertainsum) + sumAllInlist(billsum) + sumAllInlist(otherssum1) + sumAllInlist(grocersum))) * 100.0) / 100.0) + "%";
                            textofmoney = textofmoney + "\n" + "$"+ sumAllInlist(billsum);
                            mPieChart.addPieSlice(new PieModel("Bill Payment", Float.parseFloat(String.valueOf(sumAllInlist(billsum))), Color.parseColor("#425af5")));
                        }
                        if (otherssum1.size() != 0) {
                            textofcategory = textofcategory + "\n" + "Others";
                            recordstext = recordstext + "\n" + otherssum1.size();
                            textofscale = textofscale + "\n" + String.valueOf(Math.round((sumAllInlist(otherssum1) * 100 / (sumAllInlist(transpsum) + sumAllInlist(entertainsum) + sumAllInlist(billsum) + sumAllInlist(otherssum1) + sumAllInlist(grocersum))) * 100.0) / 100.0) + "%";
                            textofmoney = textofmoney + "\n" + "$"+ sumAllInlist(otherssum1);
                            mPieChart.addPieSlice(new PieModel("Others", Float.parseFloat(String.valueOf(sumAllInlist(otherssum1))), Color.parseColor("#a442f5")));
                        }
                    }
                int recordcount = 0;
                double recordsum = 0.0;
                recordcount = otherssum.size()+ billsum.size() + entertainsum.size() + transpsum.size() +grocersum.size()+ otherssum1.size()+salarysum.size()+investsum.size();
                recordsum = sumAllInlist(otherssum1)+sumAllInlist(billsum)+sumAllInlist(entertainsum)+sumAllInlist(grocersum)+sumAllInlist(transpsum)+sumAllInlist(salarysum)+sumAllInlist(investsum)+sumAllInlist(otherssum);

                textofcategory = textofcategory + "\n" +  "All" ;
                recordstext = recordstext + "\n" + recordcount ;
                textofscale = textofscale + "\n" + "100%";
                textofmoney = textofmoney + "\n" + "$"+ recordsum ;

                eventtext.setText(textofcategory);
                recordtext.setText(recordstext);
                indextext.setText(textofscale);
                moneytext.setText(textofmoney);          //Display result to the TextView

                mPieChart.startAnimation();
            }

//
        });

        mPieChart.startAnimation();

    }


    private String getTodaydDate() {                       //method of getting today day,show in datepickerdialog
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return  makeDateString(day,month,year);

    }
    private String getTodaydMonth() {                    //method of getting today month,show in datepickerdialog
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        return  makeMonthString(month,year);
    }

    //get date
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int iyear, int imonth, int iday) {
                imonth = imonth + 1;
                String date = makeDateString(iday, imonth, iyear);
                String date_month = makeMonthString(imonth, iyear);
                btn_date1.setText(date);
                btn_date4.setText(date);
                btn_date6.setText(date_month);
                getyear = iyear;
                getmonth = imonth;
                getday = iday;
            }
        };

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(getContext(),style,dateSetListener,year,month,day);

    }

    private void initDatePicker2() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker2, int iyear, int imonth, int iday) {
                imonth = imonth + 1;
                String date = makeDateString(iday, imonth, iyear);
                btn_date5.setText(date);
                getyear1 = iyear;
                getmonth1 = imonth;
                getday1 = iday;
            }
        };
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog2 = new DatePickerDialog(getContext(),style,dateSetListener,year,month,day);

    }

    //return Date of specific format
    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String makeMonthString( int month, int year) {
        return getMonthFormat(month) + " " + year;
    }
    //return Month in word
    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "ARP";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";
        return "JAN";
    }

    //method to calculate a value from date. used to compare in the customize date report
    public int getdateval(int day, int month, int year) {
        return year*372 + month*31 + day;
    }


    //get database value into arraylist
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
        for (int i = 0; i < record_id.size(); i++) {         //turn the date into three part
            String dateS = record_date.get(i);
            String[] date = dateS.split("/", 10);
            dayarray.add(date[0]);
            montharray.add(date[1]);
            yeararray.add(date[2]);

        }

    }
    //sum all value in the arraylist
    public double sumAllInlist(ArrayList list){
        double sum = 0.0;
        for (int k=0;k < list.size();k++) {
            sum = sum+ Double. parseDouble(String.valueOf(list.get(k)));
        }
        return sum;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
