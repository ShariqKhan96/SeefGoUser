package com.webxert.seefgouser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.webxert.seefgouser.common.ConstantManager;
import com.webxert.seefgouser.interfaces.EditButtonListener;
import com.webxert.seefgouser.interfaces.ProceedVisibilityListener;
import com.webxert.seefgouser.models.User;

import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity implements EditButtonListener {

    EditButtonListener editButtonListener;
    EditText nameET, passwordET, emailET;
    ImageView edit;
    FrameLayout submit_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        editButtonListener = this;
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

        User user = Paper.book().read(ConstantManager.CURRENT_USER);
        nameET.setText(user.getUser_name());
        emailET.setText(user.getUser_email());
        passwordET.setText("(md5)=>" + user.getUser_password());

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "TODO UPDATE API INTEGRATION", Toast.LENGTH_LONG).show();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
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
