package stark.prm.project.uiHelper;
import android.content.Intent;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;


import com.google.android.material.navigation.NavigationView;

import stark.prm.project.HomeworkActivity;
import stark.prm.project.MainActivity;
import stark.prm.project.NoteActivity;
import stark.prm.project.ProgressActivity;
import stark.prm.project.R;

public class UiSideMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener   {
    AppCompatActivity context;
    DrawerLayout drawerLayout;
    public UiSideMenu(AppCompatActivity context, DrawerLayout drawerLayout){
        this.context = context;
        this.drawerLayout = drawerLayout;
    };


    public void handleSideMenu(NavigationView navigationView, Toolbar toolbar, ActionBar bar){
        navigationView.bringToFront();
        if(bar != null){
            bar.setTitle(null);
            bar.setDisplayHomeAsUpEnabled(true);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        String currentLayout = context.getClass().getName();

        if(itemID == R.id.nav_home && !currentLayout.equals("stark.prm.project.MainActivity")){
            context.startActivity(new Intent(context.getApplicationContext(), MainActivity.class));
        } else if(itemID == R.id.nav_homework && !currentLayout.equals("stark.prm.project.Homework")){
            context.startActivity(new Intent(context.getApplicationContext(), HomeworkActivity.class));
        }else if(itemID == R.id.nav_notes && !currentLayout.equals("stark.prm.project.Notes")) {
            context.startActivity(new Intent(context.getApplicationContext(), NoteActivity.class));
        }else if(itemID ==R.id.nav_progress && !currentLayout.equals("stark.prm.project.Progress")){
            context.startActivity(new Intent(context.getApplicationContext(), ProgressActivity.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
