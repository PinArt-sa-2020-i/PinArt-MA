package com.example.pinart_ma.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ActionBarContainer
import androidx.fragment.app.Fragment
import com.example.pinart_ma.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_feed.*
import java.util.zip.Inflater

class FeedFragment: Fragment() {
    companion object {
        fun newInstance(): FeedFragment = FeedFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_feed, container, false)
        return view
    }


    override fun onStart() {
        super.onStart()
        textoFeed.text = "sdf clsdajnvkñasdnivklzdsv<s"
    }



}

