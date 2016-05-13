/*
package com.xmiles.android;



import com.xmiles.android.fragment.EmConstrucao_Fragment;
import com.xmiles.android.fragment.Favorites_Fragment;
import com.xmiles.android.fragment.Feed_Fragment;
import com.xmiles.android.fragment.Profile_Fragment;
import com.xmiles.android.fragment.Ranking_Fragment;
import com.xmiles.android.slidingmenu.adapter.SlidingMenuLazyAdapter;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
 
import java.util.ArrayList;
import java.util.List;
 
 
public class MainActivity_backup_rev03_20160511 extends AppCompatActivity {
	 
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    
    CollapsingToolbarLayout collapsingToolbarLayout;
    
    private int[] tabIcons = {
            R.drawable.nav_news_feed,
            R.drawable.computer,
            R.drawable.phone
    };
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //-----------
		setupToolbar();
		//setupTablayout();
		setupCollapsingToolbarLayout();        
        //-----------

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
 
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        //------
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //------
        tabLayout.setupWithViewPager(viewPager);

        //--------------
        //setupTabIcons();
        //--------------        
    }
    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_frame, menu);

		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.about_it:
			
			Toast.makeText(getApplicationContext(), "Em construção", Toast.LENGTH_LONG).show();
			
			return true;
			
		case R.id.how_works:
			
			Uri uri = Uri.parse("https://www.youtube.com/embed/OvgtMaMftZw");
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			
			return true;

		case R.id.policies:
			
			Toast.makeText(getApplicationContext(), "Em construção", Toast.LENGTH_LONG).show();
			
			return true;
			
		case R.id.buscode_search:
			
			Toast.makeText(getApplicationContext(), "Em construção", Toast.LENGTH_LONG).show();
			
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }
    
	
	private void setupToolbar(){
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if(toolbar != null)
			setSupportActionBar(toolbar);

		// Show menu icon
        final android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        //-------------------------
        //ab.hide();
        //-------------------------        

	}
 
	private void setupCollapsingToolbarLayout(){
		
		collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
		
		//if(collapsingToolbarLayout != null){
			//collapsingToolbarLayout.setTitle(toolbar.getTitle());
			//collapsingToolbarLayout.setCollapsedTitleTextColor(0xED1C24);	
			//collapsingToolbarLayout.setExpandedTitleColor(0xED1C24);
		//}
	}


 
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Feed_Fragment(), "FEED");
        adapter.addFrag(new Ranking_Fragment(), "RANKING");
        
        //adapter.addFrag(new EmConstrucao_Fragment(), "FEED");
        //adapter.addFrag(new EmConstrucao_Fragment(), "RANKING");
        
        adapter.addFrag(new EmConstrucao_Fragment(), "HASHTAG");

        viewPager.setAdapter(adapter);
    }
 
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<Fragment>();
        private final List<String> mFragmentTitleList = new ArrayList<String>();
 
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
 
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
 
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
 
        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
 
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
            //return null;
        }
    }
}
*/