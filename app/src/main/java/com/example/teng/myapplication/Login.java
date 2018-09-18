package com.example.teng.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    //FireBase
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    //Login
    private EditText etUserName, etPassword;
    private TextView tvRemind;
    //Google
    private SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions googleSignInOptions;
    private static int RC_SIGN_IN = 100;
    //FB
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        findView();
        googleLogin();
        faceBokLogin();
        //測試

        FirebaseAuth.getInstance().signOut() ;
        tvRemind.setText("");

    }

    @Override
    public void onStart() {
        super.onStart();
        // 檢查用戶目前是否登入
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    public void register(View view) {
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }

    private void findView() {
        etUserName = findViewById(R.id.etLogin_UserName);
        etPassword = findViewById(R.id.etLogin_Password);
        tvRemind = findViewById(R.id.tvRemind);
    }

    // [帳號登入]-----------------------------------------------------------------------------------
    public void login(View view) {
        //取得database 回傳
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
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

    //[帳號登入]------------------------------------------------------------------------------------

    //[Google登入]----------------------------------------------------------------------------------
    private void googleLogin() {
        signInButton = findViewById(R.id.google_login_button);
        //signInButton.setSize(SignInButton.SIZE_STANDARD);
        setGooglePlusButtonText(signInButton ,"使用 Google 帳號登入");
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sinIn();
            }
        });
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    private void sinIn() {
        Intent sigInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(sigInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("Sucess", "handleSignInResult" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
        } else {
            // Signed in successfully, show authenticated UI.
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("FireBaseToGoogle", "fireBaseAuthWithGoogle" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Success!", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Fail", "signInWithCredential:failure", task.getException());
                            //Toast.makeText(Login.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    //[Google登入]----------------------------------------------------------------------------------


    //[FaceBook登入]--------------------------------------------------------------------------------
    private void faceBokLogin() {
        //初始化Firebase 的身分驗證
        mAuth = FirebaseAuth.getInstance();
        //初始化Facebook登錄按鈕
        callbackManager = CallbackManager.Factory.create();
        //LoginButton 是套件 LoginManager 中的 UI控制項。
        // 使用者點擊這個按鈕時，將會以LoginManager 中設定的權限初始化登入。
        // 此按鈕會追蹤登入狀態，並根據用戶的驗證狀態顯示正確文字。
        loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        //接收電子郵件地址、公開預設資料(包含id.姓(first_name).名(last_name).中間字(middle_name).姓名(name)
        //                                   姓名格式(name_format).大頭貼(picture).暱稱(short_name))
        loginButton.setReadPermissions("email", "public_profile" );
        // 設定當LoginButton 收到回傳(callback)的處理動作:成功、失敗、取消
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            //登入成功，APP會收到一組AccessToken(變數accessToken)，
            public void onSuccess(LoginResult loginResult) {
                Log.d("Success", "fackbook:onSuccess" + loginResult);
                //AccessToken(存取權限)
                //將登入成功(loginResult)回傳的(AccessToken)存取權限帶入方法。
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("Cancel", "fackbook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Error", "fackbook:onError", error);
            }
        });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("handle", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 登錄成功，使用已登錄用戶的信息更新UI
                            Log.d("Success!", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // 如果登錄失敗，則向用戶顯示一條消息
                            Log.w("File", "signInWithCredential:failure", task.getException());
                            //Toast.makeText(Login.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    //[FaceBook登入]--------------------------------------------------------------------------------

    private void updateUI(FirebaseUser user) {
        //調用FireBase使用者資料
        if (user != null) {
            tvRemind.setText("登入成功\n" + user.getDisplayName() + "\n" + user.getEmail());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != RESULT_CANCELED) {
            // Pass the activity result back to the Google
            if (requestCode == RC_SIGN_IN && data != null) {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            } else {
                // 將訊息結果傳遞回Facebook SDK
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }


    }
}

