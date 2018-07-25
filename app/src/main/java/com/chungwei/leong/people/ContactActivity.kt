package com.chungwei.leong.people

import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.chungwei.leong.people.fragments.DetailsFragment
import kotlinx.android.synthetic.main.activity_contact.*

class ContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        setSupportActionBar(contactToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val detailsFragment = DetailsFragment()
        detailsFragment.arguments = intent.getBundleExtra("contact")

        supportFragmentManager
                .beginTransaction()
                .add(R.id.detailsFragmentContainer, detailsFragment)
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
