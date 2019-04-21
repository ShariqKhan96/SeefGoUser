package com.webxert.seefgouser;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webxert.seefgouser.common.ConstantManager;
import com.webxert.seefgouser.interfaces.EditButtonListener;
import com.webxert.seefgouser.interfaces.ProceedVisibilityListener;
import com.webxert.seefgouser.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity implements EditButtonListener {

    EditButtonListener editButtonListener;
    EditText nameET, passwordET, emailET;
    ImageView edit;
    FrameLayout submit_btn;

    User user;
    User user_decrypted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        editButtonListener = this;
        user = Paper.book().read(ConstantManager.CURRENT_USER);
        user_decrypted = Paper.book().read(ConstantManager.USER_DECRYPTED_OBJECT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView toolbarTv = toolbar.findViewById(R.id.toolbarText);
        toolbarTv.setText("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        nameET = findViewById(R.id.user_name);
        passwordET = findViewById(R.id.password);
        emailET = findViewById(R.id.email);
        edit = toolbar.findViewById(R.id.edit);
        submit_btn = findViewById(R.id.submit_btn);


        nameET.setText(user.getUser_name());
        emailET.setText(user.getUser_email());
        passwordET.setText(user_decrypted.getUser_password());

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog dialog = new ProgressDialog(ProfileActivity.this, R.style.MyAlertDialogStyle);
                dialog.setTitle("Updating Profile");
                dialog.setMessage("Please Wait");
                dialog.show();
                StringRequest request = new StringRequest(Request.Method.POST, ConstantManager.BASE_URL + "update.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                dialog.dismiss();
                                try {
                                    JSONObject root = new JSONObject(response);
                                    Toast.makeText(ProfileActivity.this, "" + root.getString("message"), Toast.LENGTH_SHORT).show();
                                    if (root.getString("status").equals("1")) {
                                        user.setUser_email(emailET.getText().toString());
                                        user.setUser_name(nameET.getText().toString());

                                        user.setUser_password(passwordET.getText().toString());

                                        user_decrypted.setUser_email(emailET.getText().toString());
                                        user_decrypted.setUser_password(passwordET.getText().toString());
                                        Paper.book().delete(ConstantManager.USER_DECRYPTED_OBJECT);
                                        Paper.book().delete(ConstantManager.CURRENT_USER);
                                        Paper.book().write(ConstantManager.CURRENT_USER, user);
                                        Paper.book().write(ConstantManager.USER_DECRYPTED_OBJECT, user_decrypted);
                                        Home.profileCredentialsChangedListener.onChanged();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(ProfileActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Log.e("Error", error.getLocalizedMessage());
                        Toast.makeText(ProfileActivity.this, "" + error.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("name", nameET.getText().toString());
                        map.put("email", emailET.getText().toString());
                        Log.e("Password", passwordET.getText().toString());
                        map.put("pass", passwordET.getText().toString());
                        map.put("id", user.getUser_id());
                        map.put("check", 1 + "");
                        return map;
                    }
                };
                Volley.newRequestQueue(ProfileActivity.this).add(request);
            }

        });

        edit.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                editButtonListener.onEditButtonPressed();
            }
        });


    }

    @Override
    public void onEditButtonPressed() {
        nameET.setEnabled(true);
        passwordET.setEnabled(true);
        emailET.setEnabled(true);
        submit_btn.setVisibility(View.VISIBLE);
    }

}
