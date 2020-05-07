package com.example.pinart_ma.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pinart_ma.R
import com.example.pinart_ma.ui.adapter.SearchUsersAdapter
import kotlinx.android.synthetic.main.fragment_search_users.*

class SearchUsersFragment(var adapter: SearchUsersAdapter) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_search_users, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadRecycleView(view)
        verifyEmpty(view)
    }

    fun loadRecycleView(view: View){
        var recycler: RecyclerView = view.findViewById(R.id.recyclerViewSearchUsers)
        //recycler.layoutManager = LinearLayoutManager(recycler.context)
        recycler.layoutManager = FixLayoutManager(recycler.context)
        recycler.setHasFixedSize(true)
        recycler.adapter = adapter
    }

    fun verifyEmpty(view: View){
        var recycler: RecyclerView = view.findViewById(R.id.recyclerViewSearchUsers)
        try {
            if(recycler.adapter?.itemCount == 0){
                notResultsTextViewSearchUsers.visibility = View.VISIBLE
                notResultsTextViewSearchUsers.text = "No se encuentran usuarios que coincidan."
            }
        }
        catch (e: Exception) {
            notResultsTextViewSearchUsers.visibility = View.GONE
        }
    }

}


class FixLayoutManager(var context: Context?) : LinearLayoutManager(context) {

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }



}

