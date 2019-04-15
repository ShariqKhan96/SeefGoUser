package com.webxert.seefgouser;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webxert.seefgouser.common.ConstantManager;
import com.webxert.seefgouser.models.User;
import com.webxert.seefgouser.models.Warehouse;
import com.webxert.seefgouser.models.Weights;
import com.webxert.seefgouser.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

public class FormActivity extends AppCompatActivity {

    EditText source, destination, name, weight, size;
    String approxWeights[];
    String price[];
    String ids[];
    int xFt = 1, yFt = 1;
    String selectedWeight;
    FrameLayout submit_btn;
    String source_id, dest_id;
    String comment = "";
    String pickedPrice;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        source = findViewById(R.id.item_source);
        name = findViewById(R.id.item_name);
        submit_btn = findViewById(R.id.submit_btn);


        user = Paper.book().read(ConstantManager.CURRENT_USER);
        getWeights();
        weight = findViewById(R.id.item_weight);
        size = findViewById(R.id.item_size);
        size.setText("1 x 1");
        destination = findViewById(R.id.item_desitnation);
        source_id = getIntent().getStringExtra("source");
        dest_id = getIntent().getStringExtra("destination");
        source.setText(getIntent().getStringExtra("warehouse_one"));
        destination.setText(getIntent().getStringExtra("warehouse_two"));

        source.setEnabled(false);
        destination.setEnabled(false);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(FormActivity.this);
                    View myView = LayoutInflater.from(FormActivity.this).inflate(R.layout.comment_dialog, null);

                    alert.setView(myView);
                    final EditText myCmntET = myView.findViewById(R.id.comment_et);
                    TextView priceTv = myView.findViewById(R.id.price_tv);

                    pickedPrice = String.valueOf(Integer.valueOf(pickedPrice) * 12);
                    priceTv.setText(Html.fromHtml("Estimate delivery amount : <b>" + (Integer.valueOf(pickedPrice) * 12) + "</b> Rs"));
                    alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            comment = myCmntET.getText().toString();

                            sendParcel();
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    alert.show();

                    //Toast.makeText(FormActivity.this, "Valid fields send request to server", Toast.LENGTH_LONG).show();
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
                        selectedWeight = ids[i];
                        pickedPrice = price[i];
                        weight.setText(approxWeights[i]);
                        dialogInterface.dismiss();
                        if (!name.getText().toString().isEmpty() && !weight.getText().toString().isEmpty())
                            submit_btn.setVisibility(View.VISIBLE);

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

                        size.setText(xFt + " x " + yFt);


                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
//                        if (!name.getText().toString().isEmpty() && !weight.getText().toString().isEmpty())
//                            submit_btn.setVisibility(View.VISIBLE);
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

    private void getWeights() {
        final ProgressDialog dialog = new ProgressDialog(FormActivity.this, R.style.MyAlertDialogStyle);
        dialog.setTitle("Getting Weights");
        dialog.setMessage("Please Wait");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConstantManager.BASE_URL + "weight.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Weights>>() {
                        }.getType();
                        List<Weights> weights = gson.fromJson(response, listType);

                        price = new String[weights.size()];
                        ids = new String[weights.size()];
                        approxWeights = new String[weights.size()];

                        for (int i = 0; i < weights.size(); i++) {
                            price[i] = weights.get(i).getPrice_range();
                            ids[i] = weights.get(i).getWeight_id();
                            approxWeights[i] = weights.get(i).getWeight_range();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(FormActivity.this, "" + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void sendParcel() {
        final ProgressDialog dialog = new ProgressDialog(FormActivity.this, R.style.MyAlertDialogStyle);
        dialog.setTitle("Submitting parcel");
        dialog.setMessage("Please Wait");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConstantManager.BASE_URL + "submitparcel.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            JSONObject root = new JSONObject(response);
                            Toast.makeText(FormActivity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(FormActivity.this, "" + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                /*
                $name     = $_POST['name'];
$width     = $_POST['width'];
$height     = $_POST['height'];
$start     = $_POST['start'];
$end    = $_POST['end'];
$id     = $_POST['userid'];
$weight     = $_POST['weight'];
$com     = $_POST['comment'];
                 */
                map.put("name", name.getText().toString());
                map.put("width", xFt + "");
                map.put("height", yFt + "");
                map.put("start", source_id);
                map.put("end", dest_id);
                map.put("weight", selectedWeight);
                map.put("comment", comment);
                map.put("userid", user.getUser_id());
              //  Log.e("Price", pickedPrice);
                map.put("price", String.valueOf(Integer.parseInt(pickedPrice) * 12));


                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private boolean validateFields() {
        if (!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(selectedWeight) && xFt != 0 && yFt != 0)
            return true;
        return false;
    }
}
