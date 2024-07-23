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

import com.example.sms.Login;
import com.example.sms.R;
import com.example.sms.course.CourseMainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SectionMainActivity extends AppCompatActivity {
    //firebase auth
    private FirebaseAuth mAuth;
    FirebaseUser user;

    //UI drawer layout
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView textViewEmail;
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
    String courseId, courseName;

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
        if (courseName != null && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(courseName);
        }

        //Firebase authentication initialization
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //Dialog box initialization
        inflater = getLayoutInflater();
        builder = new AlertDialog.Builder(SectionMainActivity.this);

        //method calls to create objects
        createLogoutDialogBox();
        createDrawerLayout();
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

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.college) {
                Toast.makeText(SectionMainActivity.this, "Colleges", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.course) {
                Toast.makeText(SectionMainActivity.this, "Courses", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.section) {
                Toast.makeText(SectionMainActivity.this, "Sections", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.student) {
                Toast.makeText(SectionMainActivity.this, "Students", Toast.LENGTH_SHORT).show();
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
}