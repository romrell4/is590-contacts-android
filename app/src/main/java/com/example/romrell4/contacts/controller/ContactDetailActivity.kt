package com.example.romrell4.contacts.controller

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.view.inputmethod.InputMethodManager
import com.example.romrell4.contacts.R
import com.example.romrell4.contacts.model.Contact
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.activity_contact_detail.*
import android.provider.MediaStore
import android.content.Intent
import android.graphics.Bitmap


class ContactDetailActivity: AppCompatActivity() {
    companion object {
        const val CONTACT = "Contact"
        const val TAKE_PICTURE_CODE = 1
    }

    private lateinit var contact: Contact
    private var editing: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                invalidateOptionsMenu()
                toggleViewSwitchers()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail)

        contact = intent.getParcelableExtra(CONTACT)

        imageView.setOnClickListener {
            if (editing) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                takePictureIntent.resolveActivity(packageManager)?.let {
                    startActivityForResult(takePictureIntent, TAKE_PICTURE_CODE)
                }
            }
        }

        setupUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (contact.email == GoogleSignIn.getLastSignedInAccount(this)?.email) {
            menuInflater.inflate(R.menu.edit_menu, menu)

            //Hide either the edit or done item
            menu?.getItem(if (editing) 0 else 1)?.isVisible = false
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.edit -> {
            editing = true
            true
        }
        R.id.done -> {
            if (currentFocus != null) {
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(currentFocus.windowToken, 0)
            }
            updateContactFromUI()
            setupUI()
            editing = false
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TAKE_PICTURE_CODE && resultCode == Activity.RESULT_OK) {
            (data?.extras?.get("data") as? Bitmap)?.let {
                contact.bitmap = it
                imageView.setImageBitmap(it)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setupUI() {
        if (contact.bitmap != null) {
            imageView.setImageBitmap(contact.bitmap)
        } else {
            imageView.setImageDrawable(getDrawable(contact.imageRes ?: R.drawable.ic_person_black_24dp))
        }
        nameTextView.text = contact.name
        companyTextView.text = contact.company
        positionTextView.text = contact.position
        emailTextView.text = contact.email
        phoneTextView.text = contact.cellPhone
        spouseTextView.text = contact.spouseName
        addressTextView.text = contact.address
        bioTextView.text = contact.bio

        nameEditText.hint = contact.name
        companyEditText.hint = contact.company
        positionEditText.hint = contact.position
        emailEditText.hint = contact.email
        phoneEditText.hint = contact.cellPhone
        spouseEditText.hint = contact.spouseName
        addressEditText.hint = contact.address
        bioEditText.hint = contact.bio
    }

    private fun updateContactFromUI() {
        if (nameEditText.text.isNotEmpty()) contact.name = nameEditText.text
        if (companyEditText.text.isNotEmpty()) contact.company = companyEditText.text
        if (positionEditText.text.isNotEmpty()) contact.position = positionEditText.text
        if (emailEditText.text.isNotEmpty()) contact.email = emailEditText.text
        if (phoneEditText.text.isNotEmpty()) contact.cellPhone = phoneEditText.text
        if (spouseEditText.text.isNotEmpty()) contact.spouseName = spouseEditText.text
        if (addressEditText.text.isNotEmpty()) contact.address = addressEditText.text
        if (bioEditText.text.isNotEmpty()) contact.bio = bioEditText.text
    }

    private fun toggleViewSwitchers() {
        if (editing) {
            nameViewSwitcher.showNext()
            companyViewSwitcher.showNext()
            positionViewSwitcher.showNext()
            emailViewSwitcher.showNext()
            phoneViewSwitcher.showNext()
            spouseViewSwitcher.showNext()
            addressViewSwitcher.showNext()
            bioViewSwitcher.showNext()
        } else {
            nameViewSwitcher.showPrevious()
            companyViewSwitcher.showPrevious()
            positionViewSwitcher.showPrevious()
            emailViewSwitcher.showPrevious()
            phoneViewSwitcher.showPrevious()
            spouseViewSwitcher.showPrevious()
            addressViewSwitcher.showPrevious()
            bioViewSwitcher.showPrevious()
        }
    }
}
