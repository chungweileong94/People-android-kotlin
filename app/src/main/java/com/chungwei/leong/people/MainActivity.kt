package com.chungwei.leong.people

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.chungwei.leong.people.adapters.MainViewPagerAdapter
import com.chungwei.leong.people.fragments.ContactsFragment
import com.chungwei.leong.people.utils.getStringValue
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ContactsFragment.OnContactItemClickListener {

    private lateinit var mViewPagerAdapter: MainViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewPagerAdapter = MainViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = mViewPagerAdapter

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                bottomNavigationView.selectedItemId = bottomNavigationView.menu.getItem(position).itemId
            }
        })

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
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
    }

    override fun onContactItemClicked(cursor: Cursor, position: Int) {
        cursor.moveToPosition(position)
        val bundle = Bundle()
        bundle.putString("name", cursor.getStringValue(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))

        val intent = Intent(this, ContactActivity::class.java)
        intent.putExtra("contact", bundle)
        startActivity(intent)
    }
}
