package com.example.sms.section;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.Login;
import com.example.sms.NavigationItemSelected;
import com.example.sms.R;
import com.example.sms.student.StudentMainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SectionMainActivity extends AppCompatActivity implements SectionItemListener{
    //firebase auth
    private FirebaseAuth mAuth;
    FirebaseUser user;

    //Firebase database
    FirebaseDatabase database;
    DatabaseReference sectionRef, databaseReference, sectionIdRef;

    //UI drawer layout
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView textViewEmail, addDialogTextView, editDialogTextView;
    ActionBarDrawerToggle drawerToggle;

    //Dialog box UI
    Button btnYes, btnNo, addSectionButton, cancelAddSectionButton, editSectionButton, deleteSectionButton;
    View logoutDialogView, addSectionDialogView, editSectionDialogView;
    AlertDialog.Builder builder;
    AlertDialog logoutDialog, addDialog, editDialog;
    LayoutInflater inflater;
    EditText addRecordEditText, editRecordEditText;
    FloatingActionButton addSectionButtonShowDialog;;

    //Section
    String sectionId, sectionName;
    Map<String, Object> updates;

    //Courses data came from the course activity through intent
    String courseId, courseName;

    //College data came from the course activity through intent
    String collegeId;

    //Navigation
    NavigationItemSelected navigationItemSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_section_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.section_drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //To get the data from the college activity via intent
        Intent intent = getIntent();
        courseName = intent.getStringExtra("COURSE_NAME");
        courseId = intent.getStringExtra("COURSE_ID");
        collegeId = intent.getStringExtra("COLLEGE_ID");

        if (courseName != null && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(courseName  + " -> Sections");
        }

        //Firebase authentication initialization
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //Firebase database initialization
        databaseReference = FirebaseDatabase.getInstance().getReference();
        sectionRef = databaseReference
                .child("Colleges")
                .child(collegeId)
                .child("Courses")
                .child(courseId)
                .child("Section");

        //Dialog box initialization
        inflater = getLayoutInflater();
        builder = new AlertDialog.Builder(SectionMainActivity.this);

        //method calls to create objects
        createLogoutDialogBox();
        createDrawerLayout();
        createRecyclerView();
        createAddSectionDialogBox();
        createEditSectionDialogBox();
    }

    public void createDrawerLayout(){
        drawerLayout = findViewById(R.id.section_drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        textViewEmail = navigationView.getHeaderView(0).findViewById(R.id.email);
        String email = user.getEmail();
        textViewEmail.setText(email);

        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Navigation item selected in OOP approach
        navigationItemSelected = new NavigationItemSelected(SectionMainActivity.this, logoutDialog, drawerLayout, navigationView);
        navigationItemSelected.itemSelected();
    }

    //to toggle drawer
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //logout dialogbox
    public void createLogoutDialogBox(){
        logoutDialogView = inflater.inflate(R.layout.logout_dialogbox, null);

        btnYes = logoutDialogView.findViewById(R.id.btn_yes);
        btnNo = logoutDialogView.findViewById(R.id.btn_no);

        builder.setView(logoutDialogView);
        logoutDialog = builder.create();

        //Logout button
        btnYes.setOnClickListener(v -> {
            logoutDialog.dismiss();
            mAuth.signOut();
            Toast.makeText(SectionMainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SectionMainActivity.this, Login.class);
            startActivity(intent);
            finish();
        });

        //close logout dialogbox
        btnNo.setOnClickListener(v -> {
            logoutDialog.dismiss();
        });
    }

    public void createRecyclerView(){
        ArrayList<ItemSection> items = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        SectionAdapter adapter = new SectionAdapter(this, items, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sectionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                int num = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    num++;
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    String sectionName = (String) data.get("sectionName");
                    String sectionId = (String) data.get("sectionId");

                    items.add(new ItemSection(String.valueOf(num), sectionName, sectionId));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
                Toast.makeText(SectionMainActivity.this, "Failed to load Sections.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //Section on item clicked listerner
    @Override
    public void onItemClicked(ItemSection itemSection) {
        sectionId = itemSection.getSectionId();
        sectionName = itemSection.getSectionName();
        Intent intent = new Intent(SectionMainActivity.this, StudentMainActivity.class);
        intent.putExtra("SECTION_NAME", sectionName);
        intent.putExtra("SECTION_ID", sectionId);
        intent.putExtra("COURSE_ID", courseId);
        intent.putExtra("COLLEGE_ID", collegeId);
        startActivity(intent);
    }

    //action button on click listener
    @Override
    public void actionButton(ItemSection itemSection) {
        sectionIdRef = sectionRef.child(itemSection.getSectionId());
        editRecordEditText.setText(itemSection.getSectionName());
        editDialog.show();
    }

    //Method to create Add Dialog Box
    public void createAddSectionDialogBox(){
        addSectionDialogView = inflater.inflate(R.layout.add_dialogbox, null);
        addRecordEditText = addSectionDialogView.findViewById(R.id.addRecordEditText);
        addSectionButtonShowDialog = findViewById(R.id.addButtonShowDialog);
        addSectionButton = addSectionDialogView.findViewById(R.id.addRecordButton);
        cancelAddSectionButton = addSectionDialogView.findViewById(R.id.cancelAddRecordButton);
        addDialogTextView = addSectionDialogView.findViewById(R.id.addDialogTextView);
        addDialogTextView.setText("Add Section");
        addRecordEditText.setHint("Section Name");

        builder.setView(addSectionDialogView);
        addDialog = builder.create();

        //Show add section dialog box
        addSectionButtonShowDialog.setOnClickListener(v -> {
            addDialog.show();
        });

        //add section button
        addSectionButton.setOnClickListener(v -> {
            sectionName = addRecordEditText.getText().toString().trim();

            if (!sectionName.isEmpty()){
                addDialog.dismiss();
                String sectionId = sectionRef.push().getKey();

                Section section = new Section(sectionId, sectionName);

                // Save the course to Firebase
                assert sectionId != null;
                sectionRef.child(sectionId).setValue(section)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            addDialog.dismiss();

                            // Handle success
                            Toast.makeText(SectionMainActivity.this, "Section Added", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure
                            Toast.makeText(SectionMainActivity.this, "Failed to Add Section", Toast.LENGTH_SHORT).show();
                        }
                    });
            } else{
                Toast.makeText(SectionMainActivity.this, "Please enter a section name", Toast.LENGTH_SHORT).show();
            }
            addRecordEditText.setText(null);
        });

        //cancel add section button
        cancelAddSectionButton.setOnClickListener(v -> {
            addDialog.dismiss();
        });
    }

    public void createEditSectionDialogBox(){
        editSectionDialogView = inflater.inflate(R.layout.edit_delete_dialogbox, null);
        editRecordEditText = editSectionDialogView.findViewById(R.id.editRecordEditText);
        editSectionButton = editSectionDialogView.findViewById(R.id.editRecordButton);
        deleteSectionButton = editSectionDialogView.findViewById(R.id.deleteRecordButton);
        editDialogTextView = editSectionDialogView.findViewById(R.id.editDeleteDialogTextView);

        builder.setView(editSectionDialogView);
        editDialog = builder.create();

        updates = new HashMap<>();
        editRecordEditText.setHint("Section Name");
        editDialogTextView.setText("Edit Section");

        //Edit Section button
        editSectionButton.setOnClickListener(v -> {
            sectionName = editRecordEditText.getText().toString().trim();

            if (sectionName.isEmpty()){
                Toast.makeText(SectionMainActivity.this, "Please enter a section", Toast.LENGTH_SHORT).show();
            }

            updates.put("sectionName", sectionName);

            sectionIdRef.updateChildren(updates).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    editDialog.dismiss();
                    Toast.makeText(SectionMainActivity.this, "Section Updated", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SectionMainActivity.this, "Failed to Update Section", Toast.LENGTH_SHORT).show();
                }
            });
        });

        //Delete section button
        deleteSectionButton.setOnClickListener(v -> {
            sectionIdRef.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    editDialog.dismiss();
                    Toast.makeText(SectionMainActivity.this, "Section Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SectionMainActivity.this, "Failed to Delete Section", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}