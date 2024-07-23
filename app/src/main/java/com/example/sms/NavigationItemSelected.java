package com.example.sms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.sms.All.DisplayAllMainActivity;
import com.example.sms.college.CollegeMainActivity;
import com.google.android.material.navigation.NavigationView;

public class NavigationItemSelected {
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    AlertDialog logoutDialog;
    Context context;

    String field;

    public NavigationItemSelected(NavigationView navigationView, DrawerLayout drawerLayout, AlertDialog logoutDialog, Context context) {
        this.navigationView = navigationView;
        this.drawerLayout = drawerLayout;
        this.logoutDialog = logoutDialog;
        this.context = context;
    }

    public void itemSelected(){
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.college){
                Intent intent = new Intent(context, CollegeMainActivity.class);
                context.startActivity(intent);
            } else if (id == R.id.course){
                Intent intent = new Intent(context, DisplayAllMainActivity.class);
                field = "courses";
                intent.putExtra("FIELD", field);
                context.startActivity(intent);
            } else if (id == R.id.section){
                Intent intent = new Intent(context, DisplayAllMainActivity.class);
                field = "sections";
                intent.putExtra("FIELD", field);
                context.startActivity(intent);
            } else if (id == R.id.student){
                Intent intent = new Intent(context, DisplayAllMainActivity.class);
                field = "students";
                intent.putExtra("FIELD", field);
                context.startActivity(intent);
            } else if (id == R.id.logout){
                logoutDialog.show();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }
}
