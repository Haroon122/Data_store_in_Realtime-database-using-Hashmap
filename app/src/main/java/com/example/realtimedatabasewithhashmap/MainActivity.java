package com.example.realtimedatabasewithhashmap;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText name, age;
    Button save;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.editName);
        age = findViewById(R.id.editage);
        save = findViewById(R.id.saveBtn);
        progressDialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Data");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedata();
            }
        });
    }

    private void savedata() {
        String sname = name.getText().toString().trim();
        String sage = age.getText().toString().trim();

        if (sname.isEmpty() || sage.isEmpty()) {
            Toast.makeText(MainActivity.this, "Please fill both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("sname", sname);
        dataMap.put("sage", sage);

        databaseReference.push().setValue(dataMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                progressDialog.dismiss();
                if (error == null) {
                    Toast.makeText(MainActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                    // Clear input fields after successful save
                    name.setText("");
                    age.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }
}
