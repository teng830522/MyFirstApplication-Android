package com.example.teng.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends android.support.v4.app.Fragment {

    private View view ;
    private TextView userID , userName , userMail ;
    private MainActivity mainActivity ;


    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        return view ;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser() ;
        FindView();
        if(currentUser != null){
            SetUserProfile();
        }else{
            Intent login = new Intent(getActivity(), Login.class);
            startActivity(login);
        }
    }

    private void FindView(){
        userID = view.findViewById(R.id.tv_myProfile_ID) ;
        userName = view.findViewById(R.id.tv_myProfile_name) ;
        userMail = view.findViewById(R.id.tv_myProfile_mail);
    }

    private void SetUserProfile(){
        Intent userProfile = getActivity().getIntent();
        userID.setText(userProfile.getStringExtra("userID"));
        userName.setText(userProfile.getStringExtra("userName"));
        userMail.setText(userProfile.getStringExtra("userMail")) ;
    }


}
