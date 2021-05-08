package com.example.newskribble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public
class Settings extends AppCompatActivity {

    EditText textUser,textEmail, textPass;
    Button btnSave;
    DatabaseReference reff;
    Member member;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        textUser = (EditText) findViewById(R.id.change_username);
        textEmail = (EditText) findViewById(R.id.change_email);
        textPass = (EditText) findViewById(R.id.change_password);
        btnSave = (Button) findViewById(R.id.save_button);
        member = new Member();

        reff = FirebaseDatabase.getInstance().getReference().child("Member");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
