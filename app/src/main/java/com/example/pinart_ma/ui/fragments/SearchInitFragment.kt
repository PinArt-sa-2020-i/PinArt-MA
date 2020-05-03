package com.example.pinart_ma.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pinart_ma.R

class SearchInitFragment: Fragment() {
    companion object {
        fun newInstance(): SearchInitFragment = SearchInitFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_search_init, container, false)

        return view
    }

}