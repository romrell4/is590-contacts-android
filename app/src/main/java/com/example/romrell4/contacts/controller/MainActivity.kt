package com.example.romrell4.contacts.controller

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.romrell4.contacts.R
import com.example.romrell4.contacts.model.Contact
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.activity_main.*

private const val SIGN_IN_CODE = 1

class MainActivity: AppCompatActivity() {
    private var contacts: List<Contact> = listOf(
            Contact("Eric Romrell", R.drawable.eric_romrell, "BYU OIT", "Programmer", "eric_romrell@byu.edu", "503-679-0157", "Jessica", "2244 N Canyon Road #106, Provo, UT 84604", "This is me. I am a master's student at BYU part-time, while working full time as a mobile and web service developer. I really enjoy my job, but outside of working, I love playing tennis!"),
            Contact("Jessica Romrell", R.drawable.jessica_romrell, "Self-Employed", "Independent Contractor", "jessica_romrell@gmail.com", "801-678-1400", "Eric", "123 ABC St.", "This is my lovely wife. She is a lot of fun to be around, and really enjoys her new job!"),
            Contact("Mark Roth", R.drawable.mark_roth, "University of Pittsburgh", "Lab Assistant", "mzroth@gmail.com", "503-729-3912", "Zoey", "123 Pittsburgh Avenue, Pittsburgh, PA", "This is my best friend since middle school. He just recently got married, and then moved out east. I'm still kinda bitter that he ditched me"),
            Contact("Zoey Roth", R.drawable.zoey_roth, "Unemployed", null, "zoey_fishburn@gmail.com", null, "Mark", "123 Pittsburgh Avenue, Pittsburgh, PA", "This is Zoey. She recently married my best friend. She's a lot of fun, and enjoys the outdoors."),
            Contact("Bill Nye", R.drawable.bill_nye, "Unemployed", null, "bill_ney@thescienceguy.com", null, "Blair", null, "This is a biography that will hopefully take up a couple of lines..."),
            Contact("Bill Gates", R.drawable.bill_gates, "Microsoft", "CEO", "bill_gates@hotmail.com", null, "Melinda", "1835 73rd Ave NE, Medina, WA 98039", "This is a biography that will hopefully take up a couple of lines...")
    )
    private lateinit var signInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addContactButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "This feature has not yet been built", Toast.LENGTH_SHORT).show()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ContactsAdapter()

        //Login config
        signInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.login_menu, menu)
        //Hide either the log in or log out item
        menu?.getItem(if (GoogleSignIn.getLastSignedInAccount(this) == null) 1 else 0)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId) {
        R.id.login -> {
            startActivityForResult(signInClient.signInIntent, SIGN_IN_CODE)
            true
        }
        R.id.logout -> {
            signInClient.signOut().addOnCompleteListener { invalidateOptionsMenu() }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SIGN_IN_CODE -> {
                invalidateOptionsMenu()
            }
        }
    }

    inner class ContactsAdapter: RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {
        override fun getItemCount(): Int {
            return contacts.size
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ContactViewHolder {
            return ContactViewHolder(LayoutInflater.from(this@MainActivity).inflate(R.layout.contact_row, parent, false))
        }

        override fun onBindViewHolder(holder: ContactViewHolder?, position: Int) {
            holder?.bind(contacts.get(position))
        }

        inner class ContactViewHolder(view: View): RecyclerView.ViewHolder(view) {
            var imageView = view.findViewById<ImageView>(R.id.imageView)
            var textView = view.findViewById<TextView>(R.id.textView)

            fun bind(contact: Contact) {
                imageView.setImageDrawable(resources.getDrawable(contact.imageRes ?: R.drawable.ic_person_black_24dp, null))
                textView.text = contact.name
                itemView.setOnClickListener {
                    if (GoogleSignIn.getLastSignedInAccount(this@MainActivity) != null) {
                        startActivity(Intent(this@MainActivity, ContactDetailActivity::class.java).putExtra(ContactDetailActivity.CONTACT, contact))
                    } else {
                        Toast.makeText(this@MainActivity, "Please log in to view contact details", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
