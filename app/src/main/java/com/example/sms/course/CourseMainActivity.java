package com.example.sms.course;

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
import java.util.HashMap;
import java.util.Map;

public class CourseMainActivity extends AppCompatActivity implements CourseItemListener{
    //firebase auth
    private FirebaseAuth mAuth;
    FirebaseUser user;

    //Firebase database
    DatabaseReference coursesRef, databaseReference, courseIdRef;

    //UI drawer layout
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView textViewEmail, addDialogTextView, editDialogTextView;
    ActionBarDrawerToggle drawerToggle;

    //Dialog box UI
    Button btnYes, btnNo, addCourseButton, cancelAddCourseButton, editCourseButton, deleteCourseButton;
    View logoutDialogView, addCourseDialogView, editCourseDialogView;
    AlertDialog.Builder builder;
    AlertDialog logoutDialog, addDialog, editDialog;
    LayoutInflater inflater;
    EditText addRecordEditText, editRecordEditText;
    FloatingActionButton addCourseButtonShowDialog;

    //Course
    String courseID, courseName;
    Map<String, Object> updates;

    //College
    String collegeID, collegeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.courses_drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //To get the data from the college activity via intent
        Intent intent = getIntent();
        collegeName = intent.getStringExtra("COLLEGE_NAME");
        collegeID = intent.getStringExtra("COLLEGE_ID");
        if (collegeName != null && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(collegeName);
        }

        //Firebase authentication initialization
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //Firebase database initialization
        databaseReference = FirebaseDatabase.getInstance().getReference();
        coursesRef = databaseReference
                .child("Colleges")
                .child(collegeID)
                .child("Courses");

        //Dialog box initalization
        inflater = getLayoutInflater();
        builder = new AlertDialog.Builder(CourseMainActivity.this);

        //method calls to create objects
        createLogoutDialogBox();
        createDrawerLayout();
        createRecyclerView();
        createAddCourseDialogBox();
        createEditCourseDialogBox();
    }

    //method to create sidebar
    public void createDrawerLayout(){
        drawerLayout = findViewById(R.id.courses_drawer_layout);
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

            if (id == R.id.college){
                Toast.makeText(CourseMainActivity.this, "Colleges", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.course){
                Toast.makeText(CourseMainActivity.this, "Courses", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.section){
                Toast.makeText(CourseMainActivity.this, "Sections", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.student){
                Toast.makeText(CourseMainActivity.this, "Students", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.logout){
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
            Toast.makeText(CourseMainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CourseMainActivity.this, Login.class);
            startActivity(intent);
            finish();
        });

        //close logout dialogbox
        btnNo.setOnClickListener(v -> {
            logoutDialog.dismiss();
        });
    }

    public void createRecyclerView(){
        ArrayList<ItemCourse> items = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        CourseAdapter adapter = new CourseAdapter(this, items, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        coursesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                int num = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    num++;
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    String courseName = (String) data.get("courseName");
                    String courseId = (String) data.get("courseId");

                    items.add(new ItemCourse(String.valueOf(num), courseName, courseId));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
                Toast.makeText(CourseMainActivity.this, "Failed to load courses.", Toast.LENGTH_SHORT).show();
            }
        });

        adapter.notifyDataSetChanged();
    }

    //Item clicke listener for the course items
    @Override
    public void onItemClicked(ItemCourse itemCourse) {
        courseID = itemCourse.getCourseId();
        courseName = itemCourse.getCourseName();
        Intent intent = new Intent(CourseMainActivity.this, SectionMainActivity.class);
        intent.putExtra("COURSE_NAME", courseName);
        intent.putExtra("COURSE_ID", courseID);
        intent.putExtra("COLLEGE_ID", collegeID);
        startActivity(intent);
    }

    //Action Button listeners for the course items
    @Override
    public void actionButton(ItemCourse itemCourse) {
        courseIdRef = coursesRef.child(itemCourse.getCourseId());
        editRecordEditText.setText(itemCourse.getCourseName());
        editDialog.show();
    }

    //To create add course dialog box
    public void createAddCourseDialogBox(){
        addCourseDialogView = inflater.inflate(R.layout.add_dialogbox, null);

        addRecordEditText = addCourseDialogView.findViewById(R.id.addRecordEditText);
        addCourseButtonShowDialog = findViewById(R.id.addButtonShowDialog);
        addCourseButton = addCourseDialogView.findViewById(R.id.addRecordButton);
        cancelAddCourseButton = addCourseDialogView.findViewById(R.id.cancelAddRecordButton);
        addDialogTextView = addCourseDialogView.findViewById(R.id.addDialogTextView);
        addDialogTextView.setText("Add Course");
        addRecordEditText.setHint("Course Name");

        builder.setView(addCourseDialogView);
        addDialog = builder.create();

        //show add course dialog box
        addCourseButtonShowDialog.setOnClickListener(v -> {
            addDialog.show();
        });

        //Add course button
        addCourseButton.setOnClickListener(v -> {
            courseName = addRecordEditText.getText().toString().trim();

            if (!courseName.isEmpty()){
                String courseID = coursesRef.push().getKey();

                Course course = new Course(courseID, courseName);

                // Save the course to Firebase
                assert courseID != null;
                coursesRef.child(courseID).setValue(course)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            addDialog.dismiss();

                            // Handle success
                            Toast.makeText(CourseMainActivity.this, "Course Added", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure
                            Toast.makeText(CourseMainActivity.this, "Failed to Add Course", Toast.LENGTH_SHORT).show();
                        }
                    });
            }else {
                Toast.makeText(CourseMainActivity.this, "Please enter a course name", Toast.LENGTH_SHORT).show();
            }
            addRecordEditText.setText(null);
        });

        //close add course dialogbox
        cancelAddCourseButton.setOnClickListener(v -> {
            addDialog.dismiss();
        });
    }

    public void createEditCourseDialogBox(){
        editCourseDialogView = inflater.inflate(R.layout.edit_delete_dialogbox, null);
        editRecordEditText = editCourseDialogView.findViewById(R.id.editRecordEditText);
        editCourseButton = editCourseDialogView.findViewById(R.id.editRecordButton);
        deleteCourseButton = editCourseDialogView.findViewById(R.id.deleteRecordButton);
        editDialogTextView = editCourseDialogView.findViewById(R.id.editDeleteDialogTextView);

        builder.setView(editCourseDialogView);
        editDialog = builder.create();

        updates = new HashMap<>();
        editRecordEditText.setHint("Course Name");
        editDialogTextView.setText("Add Course");

        //Edit course button
        editCourseButton.setOnClickListener(v -> {
            courseName = editRecordEditText.getText().toString().trim();

            if (courseName.isEmpty()){
                Toast.makeText(CourseMainActivity.this, "Please enter a course name", Toast.LENGTH_SHORT).show();
            }

            updates.put("courseName", courseName);

            courseIdRef.updateChildren(updates).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    editDialog.dismiss();
                    Toast.makeText(CourseMainActivity.this, "Course Updated", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(CourseMainActivity.this, "Failed to Update Course", Toast.LENGTH_SHORT).show();
                }
            });
        });

        //Delete course button
        deleteCourseButton.setOnClickListener(v -> {
            courseIdRef.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    editDialog.dismiss();
                    Toast.makeText(CourseMainActivity.this, "Course Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CourseMainActivity.this, "Failed to Delete Course", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}