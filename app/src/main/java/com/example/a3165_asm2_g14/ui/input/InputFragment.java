package com.example.a3165_asm2_g14.ui.input;

import static android.R.layout.simple_spinner_item;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.a3165_asm2_g14.DatabaseHelper;
import com.example.a3165_asm2_g14.R;
import com.example.a3165_asm2_g14.databinding.FragmentInputBinding;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class InputFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    //HomeFragment components
    private InputViewModel inputViewModel;
    private FragmentInputBinding binding;

    //DatePickerDialog
    private Button btn_date_picker_dialog,btn_submit, btn_clear;
    private DatePickerDialog datePickerDialog;
    private int year, month, day;

    //RadioButton
    private RadioGroup rg_type;
    private RadioButton check_rb,rb_income;
    private final Integer[] radio_btn_LIST = {R.id.rb_income, R.id.rb_expense};
    private boolean isChecked;
    private String typeOfPayment;

    //input amount
    private EditText et_amount;

    //spinner
    private Spinner spr_method, spr_category;
    private ArrayAdapter incomeAdapter,expenseAdapter,methodAdapter;
    private String[] temp = {"Choose Category", "Salary", "Investment", "Others"};
    private final String[] category_income = {"Choose Category", "Salary", "Investment", "Others"};
    private final String[] category_expense = {"Choose Category", "Groceries", "Transportation","Entertainment","Bill Payment", "Other"};
    private final String[] method = {"Choose Method", "Cash", "Bank", "E-Payment", "Others"};

    //textarea
    private TextInputEditText ta_notes;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        InputViewModel inputViewModel =
                new ViewModelProvider(this).get(InputViewModel.class);

        binding = FragmentInputBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        getCurrentDate();
        initDatePicker();
        btn_date_picker_dialog = root.findViewById(R.id.btn_date_picker_dialog);
        btn_date_picker_dialog.setText(month + 1 + "/" + day + "/" + year);
        btn_date_picker_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        rb_income=root.findViewById(R.id.rb_income);
        rg_type = root.findViewById(R.id.rg_type);
        check_rb = rg_type.findViewById(rg_type.getCheckedRadioButtonId());
        typeOfPayment = check_rb.getText().toString();
        addListenerRadioButton();

        et_amount = root.findViewById(R.id.et_amount);

        spr_category = root.findViewById(R.id.spr_category);
        spr_category.setOnItemSelectedListener(this);
        //incomeAdapter
        incomeAdapter = new ArrayAdapter(getContext(), simple_spinner_item, category_income);
        incomeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //expenseAdapter
        expenseAdapter = new ArrayAdapter(getContext(), simple_spinner_item, category_expense);
        expenseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spr_category.setAdapter(incomeAdapter);

        spr_method = root.findViewById(R.id.spr_method);
        spr_method.setOnItemSelectedListener(this);
        methodAdapter = new ArrayAdapter(getContext(), simple_spinner_item, method);
        methodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spr_method.setAdapter(methodAdapter);

        ta_notes = root.findViewById(R.id.ta_notes);

        btn_submit = root.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        btn_clear = root.findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //===========================================================================================================================

    private void getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int inputYear, int inputMonth, int inputDay) {
                year = inputYear;
                month = inputMonth;
                day = inputDay;
                btn_date_picker_dialog.setText(inputMonth + 1 + "/" + inputDay + "/" + inputYear);
            }
        };
        datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private void addListenerRadioButton() {
        rg_type.setOnCheckedChangeListener((new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup Group, int checkedid) {
                check_rb = rg_type.findViewById(rg_type.getCheckedRadioButtonId());
                isChecked = check_rb.isChecked();
                if (isChecked) {
                    typeOfPayment = check_rb.getText().toString();
                    Integer radio_btn_index = ArrayUtils.indexOf(radio_btn_LIST, checkedid);
                    if (radio_btn_index == 0) {
                        temp = category_income;
                        spr_category.setAdapter(incomeAdapter);
                    } else if (radio_btn_index == 1) {
                        temp = category_expense;
                        spr_category.setAdapter(expenseAdapter);
                    }
                }
            }
        }));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        switch (view.getId()) {
            case R.id.btn_clear:
                rb_income.setChecked(true);
                typeOfPayment = "Income";
                et_amount.setText(null);
                spr_category.setSelection(0);
                spr_method.setSelection(0);
                ta_notes.setText(null);
                Toast.makeText(getContext().getApplicationContext(), "Cleared", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_submit:
                if (et_amount.getText() != null && spr_category.getSelectedItemPosition() != 0 && spr_method.getSelectedItemPosition() != 0) {
                    DatabaseHelper recordDB = new DatabaseHelper(getContext());
                    recordDB.addRecord(day + "/" + month + "/" + year,
                            typeOfPayment,
                            Double.parseDouble(et_amount.getText().toString()),
                            temp[spr_category.getSelectedItemPosition()],
                            method[spr_method.getSelectedItemPosition()],
                            ta_notes.getText().toString()
                    );
                    Toast.makeText(getContext().getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext().getApplicationContext(), "Please enter values to all the spaces provided", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}