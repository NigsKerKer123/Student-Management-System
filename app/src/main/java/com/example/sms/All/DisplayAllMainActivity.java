package com.example.sms.All;

import android.app.AlertDialog;
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
import com.example.sms.course.CourseMainActivity;
import com.example.sms.student.ItemStudent;
import com.example.sms.student.StudentAdapter;
import com.example.sms.student.StudentMainActivity;
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
import java.util.Objects;

public class DisplayAllMainActivity extends AppCompatActivity implements AllItemListener{
    //firebase authentication
    private FirebaseAuth mAuth;
    FirebaseUser user;

    //firebase database
    FirebaseDatabase database;
    DatabaseReference databaseReference, dataRef ,idRef;

    //UI
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    TextView textViewEmail, addDialogTextView, editDeleteDialogTextView;
    View logoutDialogView, addDialogView, editDialogView;
    Button btnYes, btnNo, addButton, cancelAddButton, editButton, deleteButton;
    AlertDialog.Builder builder;
    AlertDialog logoutDialog, addDialog, editDialog;
    LayoutInflater inflater;
    FloatingActionButton addButtonShowDialog;

    //Generic item
    EditText addNameEditText, editNameEditText;
    String itemName;
    String itemId;
    Map<String, Object> updates;

    //Navigation
    NavigationItemSelected navigationItemSelected;

    //data came through intent
    String field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_display_all_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.all_drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //para ma ilisan ang title sa activity
        getSupportActionBar().setTitle("All");

        field = getIntent().getStringExtra("FIELD");

        //Firebase authentication initialization
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //Firebase database initialization
        dataRef = FirebaseDatabase.getInstance().getReference().child("Colleges");

        //Dialog box initalization
        inflater = getLayoutInflater();
        builder = new AlertDialog.Builder(DisplayAllMainActivity.this);

        //Method Calls to create objects
        createLogoutDialogBox();
        createDrawerLayout();
        createRecyclerView();
    }

    //method to create sidebar
    public void createDrawerLayout(){
        drawerLayout = findViewById(R.id.all_drawer_layout);
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
        navigationItemSelected = new NavigationItemSelected(navigationView, drawerLayout, logoutDialog, this);
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
            Toast.makeText(DisplayAllMainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DisplayAllMainActivity.this, Login.class);
            startActivity(intent);
            finish();
        });

        //close logout dialogbox
        btnNo.setOnClickListener(v -> {
            logoutDialog.dismiss();
        });
    }

    @Override
    public void onItemClicked(ItemAll itemAll) {
        Toast.makeText(this, itemAll.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void actionButton(ItemAll itemAll) {
        Toast.makeText(this, itemAll.getId(), Toast.LENGTH_SHORT).show();
    }

    //Method to create recycler view
    public void createRecyclerView(){
        ArrayList<ItemAll> items = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        AllAdapter adapter = new AllAdapter(this, items, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
                int num = 0;

                if (Objects.equals(field, "courses")) {
                    getSupportActionBar().setTitle("All Courses");
                    for (DataSnapshot collegeSnapshot : dataSnapshot.getChildren()) {
                        DataSnapshot coursesSnapshot = collegeSnapshot.child("Courses");
                        for (DataSnapshot courseSnapshot : coursesSnapshot.getChildren()) {
                            num++;
                            Map<String, Object> data = (Map<String, Object>) courseSnapshot.getValue();
                            String courseName = (String) data.get("courseName");
                            String courseId = courseSnapshot.getKey();

                            items.add(new ItemAll(String.valueOf(num), courseName, courseId));
                        }
                    }
                } else if (Objects.equals(field, "sections")) {
                    getSupportActionBar().setTitle("All Sections");
                    for (DataSnapshot collegeSnapshot : dataSnapshot.getChildren()) {
                        DataSnapshot coursesSnapshot = collegeSnapshot.child("Courses");
                        for (DataSnapshot courseSnapshot : coursesSnapshot.getChildren()) {
                            DataSnapshot sectionsSnapshot = courseSnapshot.child("Section");
                            for (DataSnapshot sectionSnapshot : sectionsSnapshot.getChildren()) {
                                num++;
                                Map<String, Object> data = (Map<String, Object>) sectionSnapshot.getValue();
                                String sectionName = (String) data.get("sectionName");
                                String sectionId = sectionSnapshot.getKey();

                                items.add(new ItemAll(String.valueOf(num), sectionName, sectionId));
                            }
                        }
                    }
                } else if (Objects.equals(field, "students")) {
                    getSupportActionBar().setTitle("All Students");
                    for (DataSnapshot collegeSnapshot : dataSnapshot.getChildren()) {
                        DataSnapshot coursesSnapshot = collegeSnapshot.child("Courses");
                        for (DataSnapshot courseSnapshot : coursesSnapshot.getChildren()) {
                            DataSnapshot sectionsSnapshot = courseSnapshot.child("Section");
                            for (DataSnapshot sectionSnapshot : sectionsSnapshot.getChildren()) {
                                DataSnapshot studentsSnapshot = sectionSnapshot.child("Students");
                                for (DataSnapshot studentSnapshot : studentsSnapshot.getChildren()) {
                                    num++;
                                    Map<String, Object> data = (Map<String, Object>) studentSnapshot.getValue();
                                    String studentName = (String) data.get("studentName");
                                    String studentId = studentSnapshot.getKey();

                                    items.add(new ItemAll(String.valueOf(num), studentName, studentId));
                                }
                            }
                        }
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
                Toast.makeText(DisplayAllMainActivity.this, "Failed to load Records.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}