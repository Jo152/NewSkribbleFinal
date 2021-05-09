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
import androidx.recyclerview.widget.ItemTouchHelper;
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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                position++;
                deleteDocument(position);
            }
        }).attachToRecyclerView(recyclerView);

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
            String title = "New Note";;
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
                        MyListData data = new MyListData(id, title, content);
                        myListData.add(myListData.size(), data);
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                    }
                });

    }

    private void getDataFromFirestore(){
        db.collection("notes").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
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

    public void deleteDocument(int id){
        db.collection("notes").document(String.valueOf(id))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error deleting document", e);
                    }
                });
        n1--;
        myListData.remove(id-1);
        recyclerView.removeViewAt(id-1);
        adapter.notifyItemRemoved(id);
        adapter.notifyItemRangeChanged(id, myListData.size());
    }
}