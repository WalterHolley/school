package com.umsl.wdhq58.prj1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umsl.wdhq58.prj1.databinding.ActivityMainBinding
import com.umsl.wdhq58.prj1.model.ContactModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var friendNames: ArrayList<ContactModel> = ArrayList<ContactModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView: RecyclerView = findViewById<RecyclerView>(R.id.contentRecycler)

        friendNames = getContactList()

        val adapter = ContactAdapter(friendNames)
        recyclerView.adapter = adapter;
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

    }



    private fun getContactList():ArrayList<ContactModel>{
        val friendNames: ArrayList<ContactModel> = ArrayList<ContactModel>()
        friendNames.add(ContactModel("Carson", "Shermond", "3102 South Evergreen Terrace","636-412-0234"))
        friendNames.add(ContactModel("Hope", "Nathan", "3103 Evergreen St. NE","636-432-0234"))
        friendNames.add(ContactModel("Sanders", "Eric", "3105 Evergreen Way","636-214-0234"))
        friendNames.add(ContactModel("Carthage", "Kyle", "3106 Evergreen Pl.","636-312-0234"))
        friendNames.add(ContactModel("Manorahan", "Rohan", "3188 Evergreen Ct","636-784-0234"))
        friendNames.add(ContactModel("Hall", "Sean", "4234 Evergreen Blvd","636-345-0234"))
        friendNames.add(ContactModel("Tru", "Jeni", "5864 Evergreen Ave","636-614-0234"))
        friendNames.add(ContactModel("Gilean", "Val", "6732 Evergreen Parkway","487-412-0234"))
        friendNames.add(ContactModel("Morris", "Bob", "7789 Evergreen Road","636-351-0234"))
        friendNames.add(ContactModel("Lisk", "Gary", "9991 Evergreen St","636-742-0234"))

        return friendNames
    }
}