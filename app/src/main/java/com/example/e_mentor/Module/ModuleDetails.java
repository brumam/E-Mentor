package com.example.e_mentor.Module;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_mentor.Helpers.Module;
import com.example.e_mentor.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class ModuleDetails extends AppCompatActivity {

    private ImageView iv2;
    private TextView tvName;
    private LinearLayout backBtn;
    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_details);

        iv2 = findViewById(R.id.iv2);
        tvName = findViewById(R.id.tvName);
        backBtn = findViewById(R.id.backbtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent i = getIntent();
        Module module = i.getParcelableExtra("Module");

        // Add a null check for the module object
        if (module != null) {
            Log.d("ModuleDetails", "Received Module: " + module);
            tvName.setText(module.getName());
            dbref = FirebaseDatabase.getInstance().getReference("Modules");
            Picasso.get().load(module.getImageURL()).fit().into(iv2);

        } else {
            Log.e("ModuleDetails", "Module is null");
        }
    }
}

