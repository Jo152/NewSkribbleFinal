package com.example.newskribble;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<MyListData> myListData;
    private MyListAdapter adapter;
    private FirebaseFirestore db;
    private int n1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        myListData = new ArrayList<>();
        adapter = new MyListAdapter(myListData, this);
        db = FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        getDataFromFirestore();

        Button b1 = (Button) findViewById(R.id.button_note);
        b1.setOnClickListener(myhandler1);

        Button logout = findViewById(R.id.button_logout);
        Button settings = findViewById(R.id.button_settings);//temp

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Login.class);
                startActivity(i);
            }
        });

        //temporary settings
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String accountId = getIntent().getStringExtra("currIdAccount");
                Intent i = new Intent(Menu.this, Settings.class);
                i.putExtra("currIdAccount",  accountId);
                startActivity(i);
            }
        });
    }

    View.OnClickListener myhandler1 = new View.OnClickListener() {
        public void onClick(View view) {
            int id = n1;
            String title = "Note " + n1;;
            String content = "";
            addDataToFirestore(id, title, content);
        }
    };

    private void addDataToFirestore(int id, String title, String content) {
        Map<String, Object> note = new HashMap<>();
        note.put("id", id);
        note.put("title", title);
        note.put("content", content);
        db.collection("notes").document(String.valueOf(n1))
                .set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        n1++;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                    }
                });

        //temporary way to live update
        n1 = 1;
        getDataFromFirestore();
    }

    private void getDataFromFirestore(){
        db.collection("notes").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            myListData.clear();
                            myListData.trimToSize();
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                MyListData c = d.toObject(MyListData.class);
                                myListData.add(c);
                                n1++;
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(Menu.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Menu.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}