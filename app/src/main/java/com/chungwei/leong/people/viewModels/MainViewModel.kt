package com.chungwei.leong.people.viewModels

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.os.AsyncTask
import android.provider.ContactsContract
import com.chungwei.leong.people.models.Contact
import com.chungwei.leong.people.utils.getStringValue


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var contacts: MutableLiveData<ArrayList<Contact>>? = null

    fun getContacts(): LiveData<ArrayList<Contact>> {
        if (contacts == null) {
            contacts = MutableLiveData()
            loadContacts()
        }

        return contacts as MutableLiveData<ArrayList<Contact>>
    }

    fun loadContacts(query: String = "") {
        @SuppressLint("StaticFieldLeak")
        val task = object : AsyncTask<String, Void, ArrayList<Contact>>() {

            private val mProjection = arrayOf(
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                    ContactsContract.Contacts.PHOTO_URI,
                    ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)
            private val mSelection = "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} LIKE ?"
            private var mSortOrder = "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} ASC"

            override fun doInBackground(vararg params: String?): ArrayList<Contact> {
                val cursor = getApplication<Application>().contentResolver.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        mProjection,
                        mSelection,
                        arrayOf("%${params[0]}%"),
                        mSortOrder)

                val result = ArrayList<Contact>()

                cursor?.let {
                    it.moveToFirst()

                    while (!it.isAfterLast) {
                        result.add(
                                Contact(
                                        it.getStringValue(ContactsContract.Contacts._ID)!!,
                                        it.getStringValue(ContactsContract.Contacts.LOOKUP_KEY)!!,
                                        it.getStringValue(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)!!,
                                        it.getStringValue(ContactsContract.Contacts.PHOTO_URI),
                                        it.getStringValue(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)
                                )
                        )

                        it.moveToNext()
                    }
                }

                cursor.close()

                return result
            }

            override fun onPostExecute(result: ArrayList<Contact>?) {
                super.onPostExecute(result)

                contacts?.value = result
            }
        }

        task.execute(query)
    }
}