package com.example.newskribble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    EditText textUser,textEmail, textPass;
    Button btnReg;
    DatabaseReference reff;
    Member member;
    long maxId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textUser = (EditText) findViewById(R.id.usernameText);
        textEmail = (EditText) findViewById(R.id.emailText);
        textPass = (EditText) findViewById(R.id.passwordText);
        btnReg = (Button)findViewById(R.id.registerButton);
        member = new Member();

        reff = FirebaseDatabase.getInstance().getReference().child("Member");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxId = (snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = textUser.getText().toString();
                String email = textEmail.getText().toString();
                String password = textPass.getText().toString();
                member.setUserName(userName);
                member.setEmail(email);
                member.setPassword(password);

                //reff.push().setValue(member);
                reff.child(String.valueOf(maxId+1)).setValue(member);

            }
        });

    }


    public void goToReg(View view) {
        startActivity(new Intent(this,Login.class));

    }
}