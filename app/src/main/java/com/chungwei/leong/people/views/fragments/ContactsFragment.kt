package com.chungwei.leong.people.views.fragments

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import com.chungwei.leong.people.R
import com.chungwei.leong.people.adapters.ContactsCursorRecyclerAdapter
import com.chungwei.leong.people.utils.PermissionRequestCode
import com.chungwei.leong.people.viewModels.MainViewModel
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.fragment_contacts.view.*

class ContactsFragment : Fragment() {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mContactItemClickCallback: OnContactItemClickListener

    interface OnContactItemClickListener {
        fun onContactItemClicked(view: View)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            mContactItemClickCallback = context as OnContactItemClickListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must be implement OnContactItemClickListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)
        view.contactsRecyclerView.layoutManager = LinearLayoutManager(context!!)
        view.contactsRecyclerView.setHasFixedSize(true)
        view.contactsRecyclerView.setItemViewCacheSize(50)

        setHasOptionsMenu(true)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (checkSelfPermission(context!!, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), PermissionRequestCode.READ_CONTACT.code)
        } else {
            setupViewModel()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PermissionRequestCode.READ_CONTACT.code ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupViewModel()
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val searchView = menu.findItem(R.id.appBarSearch)?.actionView as SearchView?

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true
            override fun onQueryTextChange(newText: String?): Boolean = searchContacts(newText.toString())
        })
    }

    private fun setupViewModel(): Boolean {
        mViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        mViewModel.getContacts().observe(this, Observer {
            contactsRecyclerView.adapter = ContactsCursorRecyclerAdapter(context!!, it!!) { view, contact ->
                mViewModel.selectContact(contact)
                mContactItemClickCallback.onContactItemClicked(view)
            }
        })
        return true
    }

    private fun searchContacts(searchTerm: String = ""): Boolean {
        mViewModel.loadContacts(searchTerm.trim())
        return true
    }
}
