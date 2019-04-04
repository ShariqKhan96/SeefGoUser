package com.webxert.seefgouser;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.webxert.seefgouser.common.Common;
import com.webxert.seefgouser.common.ConstantManager;
import com.webxert.seefgouser.models.User;
import com.webxert.seefgouser.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class RegisterActivity extends AppCompatActivity {
    EditText emailET, passwordET, userNameET;
    FrameLayout registerBT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUi();

        registerBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForEmptyFields(emailET.getText().toString().trim(), passwordET.getText().toString().trim(), userNameET.getText().toString().trim())) {
                    if (checkValidEmail(emailET.getText().toString().trim())) {
                        if (checkValidPassword(passwordET.getText().toString().trim())) {
                            callService();
                        } else {
                            showAlertBox("Password should not less than 6 letters");
                        }
                    } else {
                        showAlertBox("Invalid Email");
                    }
                } else {
                    showAlertBox("Fields Empty");


                }
            }
        });
    }

    private void initUi() {

        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        registerBT = findViewById(R.id.register_button);
        userNameET = findViewById(R.id.user_name);

    }

    private boolean checkForEmptyFields(String emailText, String passwordText, String userNameText) {
        if ((emailText.isEmpty() || emailText.equals("")) || (passwordText.isEmpty() || passwordText.equals(""))
                || (emailText.isEmpty() || userNameText.equals(""))) {
            return false;
        }
        return true;
    }

    private boolean checkValidEmail(String emailText) {
        return (!TextUtils.isEmpty(emailText) && Patterns.EMAIL_ADDRESS.matcher(emailText).matches());
    }

    private boolean checkValidPassword(String passwordText) {
        if (TextUtils.isEmpty(passwordText) || passwordText.length() < 6)
            return false;
        return true;
    }

    private void showAlertBox(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Validation Failed");
        builder.setMessage(s);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void callService() {


        final ProgressDialog dialog = new ProgressDialog(RegisterActivity.this, R.style.MyAlertDialogStyle);
        dialog.setTitle("Registering");
        dialog.setMessage("Please Wait");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConstantManager.BASE_URL + "userregistration.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            JSONObject root = new JSONObject(response);
                            if (root.getString("status").equals("1")) {
                                //User user = new Gson().fromJson(root.getJSONObject("user").toString(), User.class);
                                //User user = new User("", )
                                User user = new User(root.getString("id"), userNameET.getText().toString(), emailET.getText().toString(), passwordET.getText().toString());

                                Paper.book().write(ConstantManager.CURRENT_USER, user);

                                Log.e("Wrote", Paper.book().read(ConstantManager.CURRENT_USER).toString());
                                Common.savePrefs(emailET.getText().toString(), passwordET.getText().toString(), RegisterActivity.this);
                                startActivity(new Intent(RegisterActivity.this, Home.class));
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("JSONException", e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "" + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", emailET.getText().toString());
                map.put("pass", passwordET.getText().toString());
                map.put("name", userNameET.getText().toString());
                return map;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}
