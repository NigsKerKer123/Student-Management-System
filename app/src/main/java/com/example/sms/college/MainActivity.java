package com.example.sms.college;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
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

public class MainActivity extends AppCompatActivity implements CollegeItemListener {
        //firebase authentication
        private FirebaseAuth mAuth;
        FirebaseUser user;

        //firebase database
        FirebaseDatabase database;
        DatabaseReference collegeDataBase, collegeIdRef;

        //UI
        DrawerLayout drawerLayout;
        NavigationView navigationView;
        ActionBarDrawerToggle drawerToggle;
        TextView textViewEmail;
        View logoutDialogView, addCollegeDialogView, editCollegeDialogView;
        Button btnYes, btnNo, addCollegeButton, addCancelCollegeButton, editCollegeButton, deleteCollegeButton;
        AlertDialog.Builder builder;
        AlertDialog logoutDialog, addCollegeDialog, editCollegeDialog;
        LayoutInflater inflater;
        FloatingActionButton addCollegeButtonShowDialog;

        //College
        EditText addCollegeNameEditText, EditCollegeNameEditText;
        String collegeName;
        String collegeId;
        Map<String, Object> updates;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            //para ma ilisan ang title sa activity
            getSupportActionBar().setTitle("Colleges");

            //Firebase initialization
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            database = FirebaseDatabase.getInstance();
            collegeDataBase = database.getReference("Colleges");

            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);
            textViewEmail = navigationView.getHeaderView(0).findViewById(R.id.email);

            //Dialog box initalization
            inflater = getLayoutInflater();
            builder = new AlertDialog.Builder(MainActivity.this);

            //Checks kung naay naka logged in or not
            if (user == null){
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }   else {
                String email = user.getEmail();
                textViewEmail.setText(email);
            }

            drawerToggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close);
            drawerLayout.addDrawerListener(drawerToggle);
            drawerToggle.syncState();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            navigationView.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();

                if (id == R.id.college){
                    Toast.makeText(MainActivity.this, "Colleges", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.course){
                    Toast.makeText(MainActivity.this, "Courses", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.section){
                    Toast.makeText(MainActivity.this, "Sections", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.student){
                    Toast.makeText(MainActivity.this, "Students", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.logout){
                    logoutDialog.show();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            });

            //On back
            OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else {
                        MainActivity.super.onBackPressed();
                    }
                }
            };

            getOnBackPressedDispatcher().addCallback(this, callback);
            recyclerView();

            //Dialog box
            createLogoutDialog();
            createAddCollegeDialog();
            createEditCollegeDialog();
        }

        //to toggle drawer
        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            if (drawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        //This is the college recycler view
        public void recyclerView(){
            ArrayList<ItemCollege> items = new ArrayList<>();
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            CollegeAdapter adapter = new CollegeAdapter(this, items, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            collegeDataBase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    items.clear();
                    int num = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        num++;
                        Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                        String collegeName = (String) data.get("collegeName");
                        String collegeId = (String) data.get("collegeId");

                        items.add(new ItemCollege(collegeName, String.valueOf(num), collegeId));
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Failed to load colleges.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //logout user from firebase method
        public void logout(){
            mAuth.signOut();
            Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }

        //Method to create logout dialogbox
        public void createLogoutDialog() {
            logoutDialogView = inflater.inflate(R.layout.logout_dialogbox, null);

            btnYes = logoutDialogView.findViewById(R.id.btn_yes);
            btnNo = logoutDialogView.findViewById(R.id.btn_no);

            builder.setView(logoutDialogView);
            logoutDialog = builder.create();

            btnYes.setOnClickListener(v -> {
                logoutDialog.dismiss();
                logout();
            });

            btnNo.setOnClickListener(v -> {
                // Dismiss the dialog
                logoutDialog.dismiss();
            });
        }

        //method to create add college dialogbox
        public void createAddCollegeDialog(){
            addCollegeDialogView = inflater.inflate(R.layout.add_college_dialogbox, null);

            addCollegeButton = addCollegeDialogView.findViewById(R.id.addCollegeButton);
            addCancelCollegeButton = addCollegeDialogView.findViewById(R.id.cancelCollegeButton);

            addCollegeNameEditText = addCollegeDialogView.findViewById(R.id.collegeName);

            addCollegeButtonShowDialog = findViewById(R.id.addCollegeButtonShowDialog);

            builder.setView(addCollegeDialogView);
            addCollegeDialog = builder.create();

            //Show add college dialog box
            addCollegeButtonShowDialog.setOnClickListener(v -> {
                addCollegeDialog.show();
            });

            //add button for college
            addCollegeButton.setOnClickListener(v -> {
                collegeName = addCollegeNameEditText.getText().toString().trim();

                if (!collegeName.isEmpty()) {
                    addCollegeDialog.dismiss();
                    String collegeId = collegeDataBase.push().getKey();

                    College college = new College(collegeId, collegeName);
                    collegeDataBase.child(collegeId).setValue(college);

                    Toast.makeText(MainActivity.this, "College Added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a college name", Toast.LENGTH_SHORT).show();
                }
            });

            //to close add college dialog box
            addCancelCollegeButton.setOnClickListener(v -> {
                addCollegeDialog.dismiss();
            });
        }

        //College item click listener para ma intent kas lahi na activity
        @Override
        public void onItemClicked(ItemCollege itemCollege) {
        }

        //Edit button on the items
        @Override
        public void actionButton(ItemCollege itemCollege) {
            collegeId = itemCollege.getCollegeId();
            collegeName = itemCollege.getCollegeName();

            //to reference the college id
            collegeIdRef = collegeDataBase.child(collegeId);
            EditCollegeNameEditText.setText(collegeName);
            editCollegeDialog.show();
        }

        //create edit college dialog
        public void createEditCollegeDialog(){
            editCollegeDialogView = inflater.inflate(R.layout.edit_delete_college_dialogbox, null);

            editCollegeButton = editCollegeDialogView.findViewById(R.id.editCollegeButton);
            deleteCollegeButton = editCollegeDialogView.findViewById(R.id.deleteCollegeButton);
            EditCollegeNameEditText = editCollegeDialogView.findViewById(R.id.collegeName);

            builder.setView(editCollegeDialogView);
            editCollegeDialog = builder.create();

            updates = new HashMap<>();

            //button to edit college data
            editCollegeButton.setOnClickListener(v -> {
                collegeName = EditCollegeNameEditText.getText().toString().trim();

                if (collegeName.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a college name", Toast.LENGTH_SHORT).show();
                    return;
                }

                updates.put("collegeName", collegeName);
                collegeIdRef.updateChildren(updates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            editCollegeDialog.dismiss();
                            Toast.makeText(MainActivity.this, "College updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to update college", Toast.LENGTH_SHORT).show();
                        }
                    });
            });

            //button to delete college data
            deleteCollegeButton.setOnClickListener(v -> {
                editCollegeDialog.dismiss();

                collegeIdRef.removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "College deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to delete college", Toast.LENGTH_SHORT).show();
                        }
                    });
            });
        }
}