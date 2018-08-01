package com.chungwei.leong.people.adapters

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.chungwei.leong.people.R
import com.chungwei.leong.people.utils.getStringValue
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import kotlinx.android.synthetic.main.contact_list_item.view.*

class ContactsCursorRecyclerAdapter(private val context: Context, private val cursor: Cursor, private val onClickListener: (View, Int) -> Unit) : RecyclerView.Adapter<ContactsCursorRecyclerAdapter.ViewHolder>(), FastScrollRecyclerView.SectionedAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.contact_list_item, parent, false), onClickListener)

    override fun getItemCount(): Int = cursor.count

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cursor.moveToPosition(position)
        holder.bind(cursor)
    }

    override fun getSectionName(position: Int): String {
        cursor.moveToPosition(position)
        return cursor.getStringValue(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)!!.substring(0, 1).toUpperCase()
    }

    class ViewHolder(itemView: View, private val onClickListener: (View, Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bind(cursor: Cursor) {
            itemView.nameTextView.text = cursor.getStringValue(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)

            val photoUriString = cursor.getStringValue(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)
            Glide.with(itemView)
                    .load(if (photoUriString.isNullOrEmpty()) R.drawable.ic_person_150dp else Uri.parse(photoUriString))
                    .into(itemView.profileImageView)

            itemView.setOnClickListener { onClickListener(itemView, adapterPosition) }
        }
    }
}