package com.example.e_mentor.Module;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_mentor.Adapters.ModuleAdapter;
import com.example.e_mentor.Helpers.Module;
import com.example.e_mentor.HomePage;
import com.example.e_mentor.Profile.Profile;
import com.example.e_mentor.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Recycler extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ModuleAdapter.ModuleHolder.ModuleInterface {
    private BottomNavigationView bottomNavigationView;
    private Button addModule;
    private RecyclerView rv;
    private ModuleAdapter adapter;
    private EditText searchEditText;

    // Call List array for Module Helper Class
    List<Module> moduleList = new ArrayList<>();
    private List<Module> filteredModuleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        LinearLayout backButton = findViewById(R.id.button2);
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.explore);
        addModule = findViewById(R.id.add_module);

        // Initialize RecyclerView
        rv = findViewById(R.id.module);
        rv.setLayoutManager(new LinearLayoutManager(Recycler.this));

        searchEditText = findViewById(R.id.textView11);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filteredModuleList.clear();
                for (Module module : moduleList) {
                    if (module.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        filteredModuleList.add(module);
                        Log.d("Recycler", "Filtered Module List: " + filteredModuleList);

                    }
                    Log.d("Recycler", "Filtered Module List: " + filteredModuleList);

                }
                Log.d("Recycler", "Filtered Module List: " + filteredModuleList);

                adapter.setData(filteredModuleList);
                adapter.notifyDataSetChanged();
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference("user").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get the userType directly from dataSnapshot
                String userType = dataSnapshot.child("userType").getValue(String.class);

                // Check userType - Admin - permissions
                if ("admin".equals(userType)) {
                    addModule.setVisibility(View.VISIBLE);
                } else {
                    addModule.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log any error if required
            }
        });

        filteredModuleList = new ArrayList<>(moduleList);
        Log.d("Recycler", "Initial Filtered Module List: " + filteredModuleList);

        Log.d("Recycler", "Initial Module List: " + moduleList);


        // Get data from Firebase and set up the adapter
        FirebaseDatabase.getInstance().getReference("Modules").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dss : snapshot.getChildren()) {
                    Module module = dss.getValue(Module.class);
                    moduleList.add(module);
                    Log.d("Recycler", "moduleList (in onDataChange): " + moduleList);
                }

                filteredModuleList.clear();
                filteredModuleList.addAll(moduleList);

                adapter = new ModuleAdapter(filteredModuleList, Recycler.this);
                rv.setAdapter(adapter);
                Collections.sort(filteredModuleList, new Comparator<Module>() {
                    @Override
                    public int compare(Module o1, Module o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        backButton.setOnClickListener(view -> onBackPressed());

        addModule.setOnClickListener(view -> {
            Intent intent = new Intent(Recycler.this, AddNewModule.class);
            startActivity(intent);
            finish();
        });
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                // Handle the Home menu item click
                Intent homeIntent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(homeIntent);
                return true;
            case R.id.explore:
                // Handle the Explore menu item click
                return true;
            case R.id.e_mentor:
                // Handle the E-Mentor menu item click
                return true;
            case R.id.learning_hub:
                // Handle the Learning Hub menu item click
                return true;
            case R.id.profile:
                // Handle the Profile menu item click
                Intent profileIntent = new Intent(getApplicationContext(), Profile.class);
                startActivity(profileIntent);
                return true;
            default:
                return false;
        }
    }


    // Override Adapter - and send to Module Details
    public void onModuleClick(int i) {
        if (!filteredModuleList.isEmpty()) {
            Intent intent = new Intent(Recycler.this, ModuleDetails.class);
            Module selectedModule = filteredModuleList.get(i);
            Log.d("Recycler", "Selected Module (before sending): " + selectedModule);
            intent.putExtra("Module", (Parcelable) selectedModule);
            startActivity(intent);
        }
    }








}
