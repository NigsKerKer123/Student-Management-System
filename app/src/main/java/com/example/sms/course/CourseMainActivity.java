package com.example.sms.course;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
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
import com.example.sms.college.CollegeMainActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CourseMainActivity extends AppCompatActivity {
    //firebase auth
    private FirebaseAuth mAuth;
    FirebaseUser user;

    //UI drawer layout
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView textViewEmail;
    ActionBarDrawerToggle drawerToggle;

    //Dialog box UI
    Button btnYes, btnNo;
    View logoutDialogView;
    AlertDialog.Builder builder;
    AlertDialog logoutDialog;
    LayoutInflater inflater;

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

        //para ma ilisan ang title sa activity
        getSupportActionBar().setTitle("Courses");

        //Firebase authentication initialization
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        createDrawerLayout();
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


}