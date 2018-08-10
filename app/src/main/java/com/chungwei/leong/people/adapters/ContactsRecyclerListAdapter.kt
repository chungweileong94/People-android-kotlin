package com.chungwei.leong.people.adapters

import android.content.Context
import android.net.Uri
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.chungwei.leong.people.R
import com.chungwei.leong.people.models.Contact
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import kotlinx.android.synthetic.main.contact_list_item.view.*

class ContactsRecyclerListAdapter(private val context: Context, private val contacts: ArrayList<Contact>, private val onClickListener: (View, Contact) -> Unit) :
        ListAdapter<Contact, ContactsRecyclerListAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Contact>() {
            override fun areItemsTheSame(p0: Contact, p1: Contact): Boolean = p0.id == p1.id
            override fun areContentsTheSame(p0: Contact, p1: Contact): Boolean = p0 == p1
        }),
        FastScrollRecyclerView.SectionedAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.contact_list_item, parent, false), onClickListener)

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(contacts[position])


    override fun getSectionName(position: Int): String = contacts[position].name.substring(0, 1).toUpperCase()


    class ViewHolder(itemView: View, private val onClickListener: (View, Contact) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: Contact) {
            itemView.nameTextView.text = contact.name

            val photoUriString = contact.photoThumbnailUri
            Glide.with(itemView)
                    .load(if (photoUriString.isNullOrEmpty()) R.drawable.ic_person_24dp else Uri.parse(photoUriString))
                    .into(itemView.profileImageView)

            itemView.setOnClickListener { onClickListener(itemView, contact) }
        }
    }
}