package com.example.newskribble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {

    DrawerLayout          drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView        navigationView;
    View                  hView;
    TextView              nav_user;
    DatabaseReference     reff, reff1;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MenuFragment()).commit();

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        hView = navigationView.getHeaderView(0);
        nav_user = (TextView) hView.findViewById(R.id.username);

        // If it doesn't have an account id, then idk, someone fix it
        if (getIntent().hasExtra("currIdAccount")) {
            String accountId = getIntent().getStringExtra("currIdAccount");
            reff = FirebaseDatabase.getInstance().getReference().child("Member").child(accountId).child("userName");
            //reff1.child(accountId).child("userName");
            //username = (String) dataSnapshot.child("userName").getValue(String.class);
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public
                void onDataChange(@NonNull DataSnapshot snapshot) {
                    username = snapshot.getValue(String.class);
                    nav_user.setText(username);
                }

                @Override
                public
                void onCancelled(@NonNull DatabaseError error) {

                }
            });
            nav_user.setText(username);
        }



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    private boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.notesTab:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MenuFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.settingsTab:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.LanguageTab:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.MusicTab:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
        }

        return true;
    }
}