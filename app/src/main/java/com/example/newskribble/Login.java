package com.example.newskribble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class Login extends AppCompatActivity {

    EditText textUserName1, textPassword;
    Button loginBtn;
    DatabaseReference reff;
    Member member;
    long maxId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textPassword = (EditText)findViewById(R.id.textPass);
        textUserName1 = (EditText)findViewById(R.id.textUsername);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        member = new Member();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                member.setUserName(textUserName1.getText().toString().trim());
                member.setPassword(textPassword.getText().toString().trim());
                reff = FirebaseDatabase.getInstance().getReference().child("Member");
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Member member1 = new Member();
                        maxId = snapshot.getChildrenCount();

                        for(int i = 1; i <= maxId; i++){
                            //String u1 = snapshot.child(Integer.toString(i)).getValue().toString();
                            String userNameFetch = snapshot.child(Integer.toString(i)).child("userName").getValue().toString();
                            if(userNameFetch.equals(member.getUserName())){
                                String passFetch = snapshot.child(Integer.toString(i)).child("password").getValue().toString();
                                if(passFetch.equals(member.getPassword())){
                                    //Go to main menu activity with i (current account)
                                    Toast.makeText(getApplicationContext(),"Login Successful", Toast.LENGTH_SHORT).show();
                                    goToMenu(i);
                                    break;
                                }
                                else if(i == maxId) {
                                    Toast.makeText(getApplicationContext(),"Username or Password is incorrect. Try Again", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                            else if(i == maxId){
                                Toast.makeText(getApplicationContext(),"Username or Password is incorrect. Try Again", Toast.LENGTH_SHORT).show();
                                break;
                            }

                        }


                        //String passFetch = snapshot.child("1").child("password").getValue().toString();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    public void goToRegister(View view) {
        startActivity(new Intent(this, Register.class));
    }


    public void goToMenu(int accountId) {

        Intent intent = new Intent(this, Home.class);
        //send id to next activity
        intent.putExtra("currIdAccount",  Integer.toString(accountId));
        startActivity(intent);
        finish();
    }
}