package com.example.pinart_ma.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.Tag
import com.example.pinart_ma.ui.adapter.SearchTagsAdapter
import kotlinx.android.synthetic.main.fragment_search_tags.*
import kotlinx.android.synthetic.main.fragment_search_users.*
import java.lang.Exception

class SearchTagsFragment(var adapter: SearchTagsAdapter): Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_search_tags, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadRecycleView(view)
        verifyEmpty(view)


    }

    fun loadRecycleView(view: View){
        var recycler: RecyclerView = view.findViewById(R.id.recyclerViewSearchTags)
        recycler.layoutManager = LinearLayoutManager(recycler.context)
        recycler.setHasFixedSize(true)
        recycler.adapter = adapter
    }

    fun verifyEmpty(view: View){
        var recycler: RecyclerView = view.findViewById(R.id.recyclerViewSearchTags)

        try {
            if(recycler.adapter?.itemCount == 0){
                notResultsTextViewSearchTags.visibility = View.VISIBLE
                notResultsTextViewSearchTags.text = "No se encuentran etiquetas que coincidan."
            }
        }
        catch (e: Exception) {
            notResultsTextViewSearchTags.visibility = View.GONE
        }

    }

}