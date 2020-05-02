package com.example.pinart_ma.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.pinart_ma.R
import kotlinx.android.synthetic.main.fragment_feed.*


class FeedFragment: Fragment() {
    companion object {
        fun newInstance(): FeedFragment = FeedFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_feed, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Carga feed users por default
        changeFeed(FeedUsersFragment.newInstance())

        //Cargando FeedUser
        userButtonFeed.setOnClickListener {
            changeFeed(FeedUsersFragment.newInstance())
        }

        //Cargando FeedTags
        tagsButtonFeed.setOnClickListener {
            changeFeed(FeedTagsFragment.newInstance())
        }
    }



    fun changeFeed(fragment: Fragment){
        getChildFragmentManager().beginTransaction().apply {
            replace(R.id.containerFeed, fragment)
            commit()
        }
    }






}

