package com.chungwei.leong.people.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.chungwei.leong.people.fragments.ContactsFragment
import com.chungwei.leong.people.fragments.FavoritesFragment

class MainViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> ContactsFragment()
            1 -> FavoritesFragment()
            else -> null
        }
    }

    override fun getCount(): Int = 2
}