package com.chungwei.leong.people.views

import android.app.ActivityOptions
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.chungwei.leong.people.R
import com.chungwei.leong.people.adapters.MainViewPagerAdapter
import com.chungwei.leong.people.views.fragments.ContactsFragment
import com.chungwei.leong.people.models.Contact
import com.chungwei.leong.people.viewModels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.contact_list_item.view.*
import android.util.Pair as UtilPair

class MainActivity : AppCompatActivity(), ContactsFragment.OnContactItemClickListener {

    private lateinit var mViewPagerAdapter: MainViewPagerAdapter
    private var mSelectedContact: Contact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainToolbar)

        mViewPagerAdapter = MainViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = mViewPagerAdapter

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                mainBottomNavigationView.selectedItemId = mainBottomNavigationView.menu.getItem(position).itemId
            }
        })

        mainBottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_item_contacts -> {
                    viewPager.currentItem = 0
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_item_favorites -> {
                    viewPager.currentItem = 1
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getSelectedContact().observe(this, Observer {
            mSelectedContact = it
        })
    }

    override fun onContactItemClicked(view: View) {
        val bundle = Bundle()
        bundle.putString("lookup_key", mSelectedContact?.lookupKey)
        bundle.putString("name", mSelectedContact?.name)
        bundle.putString("photo_uri", mSelectedContact?.photoUri)

        val intent = Intent(this, ContactActivity::class.java)
        intent.putExtra("contact", bundle)

        val options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                UtilPair.create(view.profileImageView as View, resources.getString(R.string.transition_profile_image)),
                UtilPair.create(view.nameTextView as View, resources.getString(R.string.transition_name)))
        startActivity(intent, options.toBundle())
    }
}
