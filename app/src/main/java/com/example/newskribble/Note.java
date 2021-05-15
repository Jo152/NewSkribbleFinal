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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    int otherId = 0;
    String string = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        db = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        id = extras.getInt("id");

        if (getIntent().hasExtra("otherId")) {
            otherId = extras.getInt("otherId");
            next(id);
        }
        else{
            string = String.valueOf(id);
            nextNext(string);
        }
    }

    private void next(int position){
        db.collection("notes").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<String> names = new ArrayList<String>();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                names.add(d.getId());
                                Log.d("TAG", "name = " + d.getId());
                            }
                        } else {
                            Log.d("TAG", "Error document empty");
                        }
                        String str = names.get(position);
                        Log.d("TAG", "true name = " + str);
                        nextNext(str);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Error getting document");
            }
        });
    }

    public void nextNext(String name){
        DocumentReference docRef = db.collection("notes").document(name);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        data = document.toObject(MyListData.class);
                        setData(name);
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    public void setData(String name){
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
                changeContent(name);
                Intent i = new Intent(Note.this, Home.class);
                startActivity(i);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Note.this, Home.class);
                startActivity(i);
            }
        });
    }

    public void changeContent(String name){
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
            db.collection("notes").document(name)
                    .set(data, SetOptions.merge());
        }
    }
}

