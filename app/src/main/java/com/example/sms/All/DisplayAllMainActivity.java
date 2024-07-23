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

import com.example.sms.Login;
import com.example.sms.NavigationItemSelected;
import com.example.sms.R;
import com.example.sms.course.CourseMainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class DisplayAllMainActivity extends AppCompatActivity {
    //firebase authentication
    private FirebaseAuth mAuth;
    FirebaseUser user;

    //firebase database
    FirebaseDatabase database;
    DatabaseReference databaseReference, idRef;

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

    //Generic item (previously College)
    EditText addNameEditText, editNameEditText;
    String itemName;
    String itemId;
    Map<String, Object> updates;

    //Navigation
    NavigationItemSelected navigationItemSelected;

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

        //Firebase authentication initialization
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //firebase database initialization
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Colleges");

        //Dialog box initalization
        inflater = getLayoutInflater();
        builder = new AlertDialog.Builder(DisplayAllMainActivity.this);

        //Method Calls to create objects
        createDrawerLayout();
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
        navigationItemSelected = new NavigationItemSelected(DisplayAllMainActivity.this, logoutDialog, drawerLayout, navigationView);
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
}