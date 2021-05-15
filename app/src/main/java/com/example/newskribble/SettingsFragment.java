package com.example.newskribble;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.newskribble.Member;
import com.example.newskribble.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsFragment extends Fragment {

    EditText textUser,textEmail, textPass;
    Button            btnSave;
    DatabaseReference reff;
    Member            member;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public
    void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textUser = (EditText) view.findViewById(R.id.change_username);
        textEmail = (EditText) view.findViewById(R.id.change_email);
        textPass = (EditText) view.findViewById(R.id.change_password);
        btnSave = (Button) view.findViewById(R.id.save_button);
        member = new Member();

        reff = FirebaseDatabase.getInstance().getReference().child("Member");

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Current account ID
                String accountId = getActivity().getIntent().getStringExtra("currIdAccount");

                //see if Username contains value
                if(TextUtils.isEmpty(textUser.getText().toString()) == false){//check if username is different
                    //Toast.makeText(getApplicationContext(), "Username contains a value", Toast.LENGTH_LONG).show();
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            reff.child(accountId).child("userName").setValue(textUser.getText().toString());
                            //Toast.makeText(getActivity(), "Username successfully changed", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                //see if Password contains value
                if(TextUtils.isEmpty(textPass.getText().toString()) == false){
                    //Toast.makeText(getApplicationContext(), "Password contains a value", Toast.LENGTH_LONG).show();
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            reff.child(accountId).child("password").setValue(textPass.getText().toString());
                            //Toast.makeText(getActivity(), "Password successfully changed", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                //see if Email contains value
                if(TextUtils.isEmpty(textEmail.getText().toString()) == false){
                    //Toast.makeText(getApplicationContext(), "Email contains a value", Toast.LENGTH_LONG).show();
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            reff.child(accountId).child("email").setValue(textEmail.getText().toString());
                            //Toast.makeText(getActivity(), "Email successfully changed", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}
