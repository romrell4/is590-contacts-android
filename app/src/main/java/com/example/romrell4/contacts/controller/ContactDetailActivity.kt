package com.example.romrell4.contacts.controller

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.romrell4.contacts.R
import com.example.romrell4.contacts.model.Contact
import kotlinx.android.synthetic.main.activity_contact_detail.*

class ContactDetailActivity: AppCompatActivity() {
    companion object {
        const val CONTACT = "Contact";
    }

    lateinit var list: List<Triple<String, String?, DetailItemType>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail)

        val contact = intent.getParcelableExtra<Contact>(CONTACT)

        imageView.setImageDrawable(getDrawable(contact.imageRes ?: R.drawable.ic_person_black_24dp))

        list = listOf(
                Triple("Name", contact.name, DetailItemType.DISPLAY_ONLY),
                Triple("Company", contact.company, DetailItemType.DISPLAY_ONLY),
                Triple("Position", contact.position, DetailItemType.DISPLAY_ONLY),
                Triple("Email", contact.email, DetailItemType.EMAIL),
                Triple("Phone", contact.cellPhone, DetailItemType.PHONE),
                Triple("Spouse", contact.spouseName, DetailItemType.DISPLAY_ONLY),
                Triple("Address", contact.address, DetailItemType.DISPLAY_ONLY),
                Triple("Bio", contact.bio, DetailItemType.MULTILINE)
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ContactDetailAdapter()
    }

    enum class DetailItemType(val viewType: Int) {
        DISPLAY_ONLY(R.layout.contact_detail_row),
        PHONE(R.layout.contact_detail_row),
        EMAIL(R.layout.contact_detail_row),
        MULTILINE(R.layout.contact_bio_row)
    }

    inner class ContactDetailAdapter: RecyclerView.Adapter<BindableViewHolder>() {

        override fun getItemCount(): Int {
            return list.size
        }

        override fun getItemViewType(position: Int): Int {
            return list[position].third.viewType
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BindableViewHolder {
            if (viewType == R.layout.contact_bio_row) {
                return ContactBioViewHolder(LayoutInflater.from(this@ContactDetailActivity).inflate(viewType, parent, false))
            } else {
                return ContactDetailViewHolder(LayoutInflater.from(this@ContactDetailActivity).inflate(viewType, parent, false))
            }
        }

        override fun onBindViewHolder(holder: BindableViewHolder?, position: Int) {
            holder?.bind(list[position])
        }

        inner class ContactDetailViewHolder(view: View): BindableViewHolder(view) {
            val textView = itemView.findViewById<TextView>(R.id.textView)
            val detailTextView = itemView.findViewById<TextView>(R.id.detailTextView)

            override fun bind(value: Triple<String, String?, DetailItemType>) {
                textView.text = value.first
                detailTextView.text = value.second
                when (value.third) {
                    DetailItemType.PHONE -> itemView.setOnClickListener {
                        val options = arrayOf("Call", "Text")
                        AlertDialog.Builder(this@ContactDetailActivity)
                                .setTitle("Contact Options")
                                .setItems(options, { _, i ->
                                    when (i) {
                                        0 -> startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${value.second}")))
                                        1 -> {
                                            try {
                                                startActivity(Intent(Intent.ACTION_SEND, Uri.parse("sms:${value.second}")))
                                            } catch (e: ActivityNotFoundException) {
                                                Toast.makeText(this@ContactDetailActivity, "No sms client installed", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                })
                                .show()
                    }
                    DetailItemType.EMAIL -> itemView.setOnClickListener {
                        try {
                            startActivity(Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_EMAIL, value.second))
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(this@ContactDetailActivity, "No email client installed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else -> {}
                }
            }
        }

        inner class ContactBioViewHolder(view: View): BindableViewHolder(view) {
            val bioTextView = itemView.findViewById<TextView>(R.id.bioTextView)

            override fun bind(value: Triple<String, String?, DetailItemType>) {
                bioTextView.text = value.second
            }
        }
    }

    abstract class BindableViewHolder(view: View): RecyclerView.ViewHolder(view) {
        abstract fun bind(value: Triple<String, String?, DetailItemType>)
    }
}
