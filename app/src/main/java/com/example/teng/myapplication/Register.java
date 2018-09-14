package com.example.teng.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    DatabaseReference myDatabase, addUserName;
    EditText etUserName, etPassword, etConfirmPassword, etName, etBirthday, etMail;
    Button btRegister;
    String stUserName, stPassword, stConfirmPassword, stName, stBirthday, stMail;
    TextView tvRegister_Remind ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        FindView();
        myDatabase = FirebaseDatabase.getInstance().getReference();

    }

    public void register(View view) {
        getString();
        //檢查欄位是否為空
        if (stUserName.equals("") || stPassword.equals("") || stConfirmPassword .equals("") || stName.equals("") ||
                stBirthday.equals("") || stMail.equals("")) {
            tvRegister_Remind.setText("欄位不得為空");
        }else{
            //檢查兩個密碼是否一致
            if(stPassword.equals(stConfirmPassword)){
                tvRegister_Remind.setText("密碼一致");
                myDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("UserName/" + stUserName).exists()){
                            tvRegister_Remind.setText("帳號已被使用過");
                        }else{
                            addFireBase();
                            Intent intent = new Intent(Register.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }else{
                tvRegister_Remind.setText("請確認密碼是否填寫一致");
            }
        }
    }

    private void FindView() {
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etName = findViewById(R.id.etName);
        etBirthday = findViewById(R.id.etBirthday);
        etMail = findViewById(R.id.etMail);
        btRegister = findViewById(R.id.btRegister);
        tvRegister_Remind = findViewById(R.id.tvRegister_Remind);
    }

    private void getString() {
        stUserName = etUserName.getText().toString();
        stPassword = etPassword.getText().toString();
        stConfirmPassword = etConfirmPassword.getText().toString();
        stName = etName.getText().toString();
        stBirthday = etBirthday.getText().toString();
        stMail = etMail.getText().toString();
    }

    private void addFireBase() {
        addUserName = FirebaseDatabase.getInstance().getReference().child("UserName/" + stUserName);
        Map<String, Object> datas;
        datas = new HashMap<String, Object>();
        datas.put("密碼", stPassword);
        datas.put("姓名", stName);
        datas.put("生日", stBirthday);
        datas.put("郵箱", stMail);
        addUserName.setValue(datas);
    }

}
