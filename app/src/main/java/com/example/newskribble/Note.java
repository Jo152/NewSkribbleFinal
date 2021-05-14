package com.example.newskribble;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Note extends AppCompatActivity {
    int id = 0;
    MyListData data = new MyListData();
    private FirebaseFirestore db;
    EditText e1;
    EditText e2;
    String title;
    String content;
    String newTitle;
    String newContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Bundle extras = getIntent().getExtras();
        id = extras.getInt("id");

        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("notes").document(String.valueOf(id));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        data = document.toObject(MyListData.class);
                        setData();
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    public void setData(){
        e1 = (EditText) findViewById(R.id.note_title);
        e1.setText(data.getTitle());
        e2 = (EditText) findViewById(R.id.note_text);
        e2.setText(data.getContent());

        title = e1.getText().toString();
        content = e2.getText().toString();

        Button b1 = (Button) findViewById(R.id.button_editNote);
        Button b2 = (Button) findViewById(R.id.button_goMenu);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeContent();
                Intent i = new Intent(Note.this, Menu.class);
                startActivity(i);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Note.this, Menu.class);
                startActivity(i);
            }
        });
    }

    public void changeContent(){
        newTitle = e1.getText().toString();
        newContent = e2.getText().toString();

        Map<String, Object> data = new HashMap<>();

        if(newTitle != title || newContent != content){
            if(newTitle != title){
                data.put("title", newTitle);
            }
            if(newContent != content){
                data.put("content", newContent);
            }
            db.collection("notes").document(String.valueOf(id))
                    .set(data, SetOptions.merge());
        }
    }
}

