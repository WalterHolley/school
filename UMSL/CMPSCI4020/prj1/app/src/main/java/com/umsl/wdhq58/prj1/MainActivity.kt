package com.umsl.wdhq58.prj1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umsl.wdhq58.prj1.databinding.MainCardlayoutBinding


class MainActivity : AppCompatActivity() {
   private lateinit var binding: MainCardlayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.contentRecycler)
        var friendNames = ArrayList<String>()
        friendNames.add("Shermond")
        friendNames.add("Nathan")
        friendNames.add("Eric")
        friendNames.add("Kyle")
        friendNames.add("Rohan")
        friendNames.add("Sean")
        friendNames.add("Jeni")
        friendNames.add("Val")
        friendNames.add("Bob")
        friendNames.add("Gary")
        binding = MainCardlayoutBinding.inflate(layoutInflater)
        val adapter = ContactAdapter(friendNames)
        recyclerView.adapter = adapter;
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        setContentView(binding.root)
    }
}