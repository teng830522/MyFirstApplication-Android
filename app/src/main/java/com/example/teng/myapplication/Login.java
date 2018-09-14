package com.example.teng.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    EditText etUserName, etPassword;
    TextView tvRemind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        findView();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    public void login(View view) {
        //檢查欄位是否填寫
        if (etUserName.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
            tvRemind.setText("欄位不得為空");
        } else {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //檢查是否帳密都正確
                    if (dataSnapshot.child("UserName/" + etUserName.getText().toString()).exists() && etPassword.getText().toString().equals(
                            dataSnapshot.child("UserName/" + etUserName.getText().toString() + "/密碼").getValue().toString())) {
                        //切換到 MainActivity
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        //檢查是否帳號正確，密碼不正確
                    } else if (dataSnapshot.child("UserName/" + etUserName.getText().toString()).exists() && !(etPassword.getText().toString().equals(
                            dataSnapshot.child("UserName/" + etUserName.getText().toString() + "/密碼").getValue().toString()))) {
                        tvRemind.setText("密碼錯誤");
                    } else {
                        tvRemind.setText("查無此帳號");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void forget(View view) {
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }

    private void findView() {
        etUserName = findViewById(R.id.etLogin_UserName);
        etPassword = findViewById(R.id.etLogin_Password);
        tvRemind = findViewById(R.id.tvRemind);
    }
}
