package com.umsl.wdhq58.prj1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.umsl.wdhq58.prj1.model.ContactModel

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val model: ContactModel = intent.getSerializableExtra("contactDetail") as ContactModel
        getActionBar()?.setDisplayHomeAsUpEnabled(true)

        findViewById<TextView>(R.id.txtFirstName).text = model.firstName
        findViewById<TextView>(R.id.txtLastName).text = model.lastName
        findViewById<TextView>(R.id.txtAddress).text = model.address
        findViewById<TextView>(R.id.txtPhone).text = model.phone

    }
}