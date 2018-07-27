package com.chungwei.leong.people

import android.net.Uri
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.MenuItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_contact.*

class ContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        setSupportActionBar(contactToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.getBundleExtra("contact")
        nameTextView.text = bundle.getString("name")

        val photoUriString = bundle.getString("photo_uri")
        Glide.with(this)
                .load(if (photoUriString.isNullOrEmpty()) R.drawable.ic_person_120dp else Uri.parse(photoUriString))
                .into(profileImageView)

        contactAppBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            run {
                val layoutParams = nameTextViewWrapper.layoutParams as ConstraintLayout.LayoutParams

                val offset = Math.abs(verticalOffset) / 10 * 5 + 80

                if (offset < TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60F, resources.displayMetrics)) {
                    layoutParams.setMargins(offset, 0, 0, 0)
                    nameTextViewWrapper.layoutParams = layoutParams
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
