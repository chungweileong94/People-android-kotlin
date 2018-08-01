package com.chungwei.leong.people.fragments


import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.chungwei.leong.people.R
import kotlinx.android.synthetic.main.fragment_details.view.*

class DetailsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        view.nameTextView.text = arguments?.getString("name")
        val photoUriString = arguments?.getString("photo_uri")
        Glide.with(view)
                .load(if (photoUriString.isNullOrEmpty()) R.drawable.ic_person_150dp else Uri.parse(photoUriString))
                .into(view.profileImageView)

        return view
    }
}
