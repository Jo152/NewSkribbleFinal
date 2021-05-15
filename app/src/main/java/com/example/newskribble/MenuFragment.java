package com.example.newskribble;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public
class MenuFragment extends Fragment {
    private RecyclerView          recyclerView;
    private ArrayList<MyListData> myListData;
    private MyListAdapter         adapter;
    private FirebaseFirestore     db;
    private int                   n1 = 1;
    private String                info = "";
    private EditText              text;
    private String                str = "nope";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        myListData = new ArrayList<>();
        adapter = new MyListAdapter(myListData, getActivity());
        db = FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                deleteFromPosition(position);
            }
        }).attachToRecyclerView(recyclerView);

        getDataFromFirestore();

        Button b1 = (Button) view.findViewById(R.id.button_note);
        b1.setOnClickListener(myhandler1);
        Button b2 = (Button) view.findViewById(R.id.button_search);
        b2.setOnClickListener(myhandler2);

        text = (EditText) view.findViewById(R.id.edit_text);

        Button logout = view.findViewById(R.id.button_logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Login.class);
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

            // Go to new note activity
            Intent i = new Intent(getActivity(), Note.class);
            i.putExtra("id", id);
            startActivity(i);
        }
    };

    View.OnClickListener myhandler2 = new View.OnClickListener() {
        public void onClick(View view) {
            if(!text.getText().toString().equals("")){
                info = text.getText().toString();
                filterList(info);
            }
        }
    };

    private void filterList(String info){
        ArrayList<Integer> list = new ArrayList<Integer>();
        int i = 0;
        for(MyListData data : myListData){
            if (!data.getTitle().equals(info)){
                list.add(i);
                i++;
            }
        }
        myListData.removeAll(list);
        adapter.notifyDataSetChanged();
    }

    private void addDataToFirestore(int id, String title, String content) {
        Map<String, Object> note = new HashMap<>();
        note.put("id", id);
        note.put("title", title);
        note.put("content", content);
        db.collection("notes")
                .add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot written with ID: " + documentReference.getId());
                        n1++;
                        MyListData data = new MyListData(id, title, content);
                        myListData.add(myListData.size(), data);
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
//        db.collection("notes").document(String.valueOf(n1))
//                .set(note)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        n1++;
//                        MyListData data = new MyListData(id, title, content);
//                        myListData.add(myListData.size(), data);
//                        adapter.notifyDataSetChanged();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("TAG", "Error writing document", e);
//                    }
//                });
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
                                if(n1 < c.getId()){
                                    n1 = c.getId();
                                }
                            }
                            n1++;
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteFromPosition(int position){
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
                        str = names.get(position);
                        Log.d("TAG", "true name = " + str);
                        Log.d("TAG", "true name 2 = " + str);
                        deleteDocument(position + 1, str);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Error getting document");
            }
        });
    }

    private void deleteDocument(int id, String name){
        db.collection("notes").document(name)
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
