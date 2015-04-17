package comp313.g2.smartgrocery;

import comp313.g2.smartgrocery.adapters.NavDrawerListAdapter;
import comp313.g2.smartgrocery.fragments.ListsFragment;
import comp313.g2.smartgrocery.fragments.NotificationsFragment;
import comp313.g2.smartgrocery.fragments.NutritionInfoFragment;
import comp313.g2.smartgrocery.fragments.ReportsFragment;
import comp313.g2.smartgrocery.fragments.SettingsFragment;
import comp313.g2.smartgrocery.models.NavDrawerItem;
import comp313.g2.smartgrocery.services.ReminderService;

import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ListView;
 
public class MainActivity extends Activity {
	private SharedPreferences prefs;
	
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
 
    // nav drawer title
    private CharSequence mDrawerTitle;
 
    // used to store app title
    private CharSequence mTitle;
 
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
 
    //fragments 
    private ListsFragment listFragment;
    private SettingsFragment settingsFragment;
    private NotificationsFragment notificationsFragment;
    private ReportsFragment reportsFragment;
    private NutritionInfoFragment nutritionInfoFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        mTitle = mDrawerTitle = getTitle();
 
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
 
        navDrawerItems = new ArrayList<NavDrawerItem>();
 
        // adding nav drawer items to array
        navDrawerItems.add(new NavDrawerItem("Notifications", R.drawable.ic_menu_notifications));
        navDrawerItems.add(new NavDrawerItem("Lists", android.R.drawable.ic_menu_save));
        navDrawerItems.add(new NavDrawerItem("Reports", android.R.drawable.ic_menu_recent_history));
        navDrawerItems.add(new NavDrawerItem("Nutrition Info", android.R.drawable.ic_menu_info_details));
        navDrawerItems.add(new NavDrawerItem("Settings", android.R.drawable.ic_menu_manage));
        navDrawerItems.add(new NavDrawerItem("Sign Out", android.R.drawable.ic_lock_power_off));
        
 
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
 
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);
 
        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
 
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }
 
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //initialize fragments
        listFragment = new ListsFragment();
        settingsFragment = new SettingsFragment();
        notificationsFragment = new NotificationsFragment();
        reportsFragment = new ReportsFragment();
        nutritionInfoFragment = new NutritionInfoFragment();
        
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        
        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
        
        startService(new Intent(MainActivity.this, ReminderService.class));
    }
 
    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
        
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
         
        return super.onOptionsItemSelected(item);
    }
 
    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
 
    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
        case 0:
        	fragment = notificationsFragment;
        	break;
        case 1:
            fragment = listFragment;
            break;
        case 2:
        	fragment = reportsFragment;
        	break;
        case 3:
        	fragment = nutritionInfoFragment;
        	break;
        case 4:
        	fragment = settingsFragment;
        	break;
        case 5:
        	Editor editor = prefs.edit();
        	editor.clear();
        	editor.commit();
        	
        	//starting login activity
        	Intent i = new Intent(MainActivity.this, LoginActivity.class);
        	i.addCategory(Intent.CATEGORY_DEFAULT);
    		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		i.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        	i.setAction(Intent.ACTION_MAIN);
        	startActivity(i);
        	break;
        default:
            break;
        }
 
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
 
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }
 
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
 
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
 
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
 
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
 
}