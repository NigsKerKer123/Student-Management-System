package com.example.sms.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.Login;
import com.example.sms.R;
import com.example.sms.section.ItemSection;
import com.example.sms.section.Section;
import com.example.sms.section.SectionAdapter;
import com.example.sms.section.SectionMainActivity;
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
import java.util.Map;

public class StudentMainActivity extends AppCompatActivity implements StudentItemListener{
    //firebase auth
    private FirebaseAuth mAuth;
    FirebaseUser user;

    //Firebase database
    FirebaseDatabase database;
    DatabaseReference studentRef, databaseReference, studentIdRef;

    //UI drawer layout
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView textViewEmail, addDialogTextView, editDialogTextView;
    ActionBarDrawerToggle drawerToggle;

    //Dialog box UI
    Button btnYes, btnNo, addStudentButton, cancelAddStudentButton, editStudentButton, deleteStudentButton;
    View logoutDialogView, addStudentDialogView, editStudentDialogView;
    AlertDialog.Builder builder;
    AlertDialog logoutDialog, addDialog, editDialog;
    LayoutInflater inflater;
    EditText addRecordEditText, editRecordEditText;
    FloatingActionButton addStudentButtonShowDialog;

    //Student
    Map<String, Object> updates;
    String studentId, studentName;

    //Courses data came from the course activity through intent
    String courseId;

    //College data came from the course activity through intent
    String collegeId;

    //Section data came from the course activity through intent
    String sectionId, sectionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.student_drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //To get the data from the college activity via intent
        Intent intent = getIntent();
        collegeId = intent.getStringExtra("COLLEGE_ID");
        courseId = intent.getStringExtra("COURSE_ID");
        sectionId = intent.getStringExtra("SECTION_ID");
        sectionName = intent.getStringExtra("SECTION_NAME");

        if (sectionName != null && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(sectionName + " -> Students");
        }

        //Firebase authentication initialization
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //Firebase database initialization
        databaseReference = FirebaseDatabase.getInstance().getReference();
        studentRef = databaseReference
                .child("Colleges")
                .child(collegeId)
                .child("Courses")
                .child(courseId)
                .child("Section")
                .child(sectionId)
                .child("Students");

        //Dialog box initialization
        inflater = getLayoutInflater();
        builder = new AlertDialog.Builder(StudentMainActivity.this);

        //method calls to create objects
        createDrawerLayout();
        createLogoutDialogBox();
        createRecyclerView();
        createAddStudentDialogBox();
    }

    public void createDrawerLayout(){
        drawerLayout = findViewById(R.id.student_drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        textViewEmail = navigationView.getHeaderView(0).findViewById(R.id.email);
        String email = user.getEmail();
        textViewEmail.setText(email);

        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.college) {
                Toast.makeText(StudentMainActivity.this, "Colleges", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.course) {
                Toast.makeText(StudentMainActivity.this, "Courses", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.section) {
                Toast.makeText(StudentMainActivity.this, "Sections", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.student) {
                Toast.makeText(StudentMainActivity.this, "Students", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.logout) {
                logoutDialog.show();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
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
            Toast.makeText(StudentMainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(StudentMainActivity.this, Login.class);
            startActivity(intent);
            finish();
        });

        //close logout dialogbox
        btnNo.setOnClickListener(v -> {
            logoutDialog.dismiss();
        });
    }

    //Method to create recycler view for student list
    public void createRecyclerView(){
        ArrayList<ItemStudent> items = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        StudentAdapter adapter = new StudentAdapter(this, items, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        studentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                int num = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    num++;
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    String studentName = (String) data.get("studentName");
                    String studentId = (String) data.get("studentId");

                    items.add(new ItemStudent(String.valueOf(num), studentName, studentId));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
                Toast.makeText(StudentMainActivity.this, "Failed to load Students.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClicked(ItemStudent itemStudent) {

    }

    @Override
    public void actionButton(ItemStudent itemStudent) {

    }

    //Method to create Add Dialog Box
    public void createAddStudentDialogBox(){
        addStudentDialogView = inflater.inflate(R.layout.add_dialogbox, null);
        addRecordEditText = addStudentDialogView.findViewById(R.id.addRecordEditText);
        addStudentButtonShowDialog = findViewById(R.id.addButtonShowDialog);
        addStudentButton = addStudentDialogView.findViewById(R.id.addRecordButton);
        cancelAddStudentButton = addStudentDialogView.findViewById(R.id.cancelAddRecordButton);
        addDialogTextView = addStudentDialogView.findViewById(R.id.addDialogTextView);
        addDialogTextView.setText("Add Student");
        addRecordEditText.setHint("Student Name");

        builder.setView(addStudentDialogView);
        addDialog = builder.create();

        //Show add section dialog box
        addStudentButtonShowDialog.setOnClickListener(v -> {
            addDialog.show();
        });

        //add section button
        addStudentButton.setOnClickListener(v -> {
            studentName = addRecordEditText.getText().toString().trim();

            if (!studentName.isEmpty()){
                addDialog.dismiss();
                String studentId = studentRef.push().getKey();

                Student student = new Student(studentId, studentName);

                // Save the course to Firebase
                assert studentId != null;
                studentRef.child(studentId).setValue(student)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                addDialog.dismiss();

                                // Handle success
                                Toast.makeText(StudentMainActivity.this, "Student Added", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure
                                Toast.makeText(StudentMainActivity.this, "Failed to Add Student", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else{
                Toast.makeText(StudentMainActivity.this, "Please enter a student name", Toast.LENGTH_SHORT).show();
            }
            addRecordEditText.setText(null);
        });

        //cancel add section button
        cancelAddStudentButton.setOnClickListener(v -> {
            addDialog.dismiss();
        });
    }
}