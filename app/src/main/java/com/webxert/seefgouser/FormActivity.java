package com.webxert.seefgouser;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class FormActivity extends AppCompatActivity {

    EditText source, destination, name, weight, size;
    String approxWeights[] = {"0.5kg - 2kg", "2kg - 4kg", "4kg - 7kg", "7kg - 10kg", "10kg - onwards"};

    int xFt = 1, yFt = 1;
    String selectedWeight, itemName;
    FrameLayout submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        source = findViewById(R.id.item_source);
        name = findViewById(R.id.item_name);
        submit_btn = findViewById(R.id.submit_btn);

        weight = findViewById(R.id.item_weight);
        size = findViewById(R.id.item_size);
        size.setText("1 x 1");
        destination = findViewById(R.id.item_desitnation);
        source.setText(getIntent().getStringExtra("warehouse_one"));
        destination.setText(getIntent().getStringExtra("warehouse_two"));

        source.setEnabled(false);
        destination.setEnabled(false);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    Toast.makeText(FormActivity.this, "Valid fields send request to server", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(FormActivity.this, "Some fields are empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(FormActivity.this);
                dialog.setTitle("Select Approximate Weight");
                dialog.setItems(approxWeights, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedWeight = approxWeights[i];
                        weight.setText(selectedWeight);
                        dialogInterface.dismiss();

                    }
                });
                dialog.show();
            }
        });
        size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View numberPickerDialogView = LayoutInflater.from(FormActivity.this).inflate(R.layout.number_picker_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(FormActivity.this);
                builder.setTitle("Pick Size");
                builder.setView(numberPickerDialogView);
                NumberPicker xAxis = numberPickerDialogView.findViewById(R.id.nmX);
                NumberPicker yAxis = numberPickerDialogView.findViewById(R.id.nmY);

                yAxis.setMinValue(1);
                yAxis.setMaxValue(2000);
                xAxis.setMinValue(1);
                xAxis.setMaxValue(2000);


                xAxis.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        xFt = numberPicker.getValue();
                    }
                });
                yAxis.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        yFt = numberPicker.getValue();

                        size.setText("0 x 0");


                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if (!name.getText().toString().isEmpty() && !weight.getText().toString().isEmpty())
                            submit_btn.setVisibility(View.VISIBLE);
                    }
                });
                builder.show();

            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTv = toolbar.findViewById(R.id.toolbarText);
        toolbarTv.setText("Parcel Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private boolean validateFields() {
        if (!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(selectedWeight) && xFt != 0 && yFt != 0)
            return true;
        return false;
    }
}
