package com.example.newskribble;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.app.AppCompatActivity;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {

    MyListData[] myListData = new MyListData[] {
            new MyListData("Note 1"),
            new MyListData("Note 2"),
            new MyListData("Note 3")
    };
    MyListAdapter adapter = new MyListAdapter(myListData);
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        b1 = (Button) findViewById(R.id.button_note);
        b1.setOnClickListener(myhandler1);

        Button logout = findViewById(R.id.button_logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this, Login.class);
                startActivity(i);
            }
        });
    }

    View.OnClickListener myhandler1 = new View.OnClickListener() {
        public void onClick(View view) {
            MyListData[] myNewListData = new MyListData[myListData.length+1];
            for(int i = 0; i < myNewListData.length; i++){
                if(i == myNewListData.length-1){
                    myNewListData[i] = new MyListData("Note " + (i+1));
                }
                else{
                    myNewListData[i] = myListData[i];
                }
            }
            myListData = myNewListData;
            adapter.setListData(myListData);
        }
    };
}