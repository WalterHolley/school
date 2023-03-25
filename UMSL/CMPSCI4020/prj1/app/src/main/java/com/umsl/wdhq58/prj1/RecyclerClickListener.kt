package com.umsl.wdhq58.prj1

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.RecyclerView
import com.umsl.wdhq58.prj1.model.ContactModel

class RecyclerClickListener(val context: Context, val recyclerView: RecyclerView, val friendNames: ArrayList<ContactModel>): OnClickListener {

    override fun onClick(p0: View) {
        val position: Int = recyclerView.getChildAdapterPosition(p0)
        val detailIntent: Intent = Intent(context, DetailActivity::class.java)
        detailIntent.putExtra("contactDetail", friendNames[position])
        context.startActivity(detailIntent)
    }
}