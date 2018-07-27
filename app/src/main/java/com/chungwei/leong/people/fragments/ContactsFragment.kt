package com.chungwei.leong.people.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.AsyncTask
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import com.chungwei.leong.people.R
import com.chungwei.leong.people.adapters.ContactsCursorRecyclerAdapter
import com.chungwei.leong.people.utils.PermissionRequestCode
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.fragment_contacts.view.*

class ContactsFragment : Fragment() {

    private val mContactAsyncTask = ContactsAsyncTask()

    private lateinit var mContactItemClickCallback: OnContactItemClickListener

    interface OnContactItemClickListener {
        fun onContactItemClicked(cursor: Cursor, view: View, position: Int)
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

        setHasOptionsMenu(true)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mContactAsyncTask.cancel(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (checkSelfPermission(context!!, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), PermissionRequestCode.READ_CONTACT.code)
        } else {
            getContacts()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PermissionRequestCode.READ_CONTACT.code ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts()
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val searchView = menu.findItem(R.id.appBarSearch)?.actionView as SearchView?

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true
            override fun onQueryTextChange(newText: String?): Boolean = getContacts(newText.toString())
        })
    }

    private fun getContacts(searchTerm: String = ""): Boolean {
        if (mContactAsyncTask.status != AsyncTask.Status.FINISHED) mContactAsyncTask.cancel(true)
        ContactsAsyncTask().execute(searchTerm.trim())
        return true
    }

    @SuppressLint("StaticFieldLeak")
    inner class ContactsAsyncTask : AsyncTask<String, Void, Cursor>() {

        private val mProjection = arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.Contacts.PHOTO_URI,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)
        private val mSelection = "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} LIKE ?"
        private var mSortOrder = "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} ASC"

        override fun doInBackground(vararg params: String?): Cursor? =
                if (isCancelled) null else context?.contentResolver?.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        mProjection,
                        mSelection,
                        arrayOf("%${params[0]}%"),
                        mSortOrder)


        override fun onPostExecute(result: Cursor?) {
            super.onPostExecute(result)

            if (result != null) {
                contactsRecyclerView.adapter = ContactsCursorRecyclerAdapter(context!!, result) { view, position ->
                    mContactItemClickCallback.onContactItemClicked(result, view, position)
                }
            }
        }
    }
}
