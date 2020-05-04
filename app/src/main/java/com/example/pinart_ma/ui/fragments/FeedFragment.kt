package com.example.pinart_ma.ui.fragments

import android.content.Context
import android.graphics.Color
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
        changeFeed(FeedTagsFragment.newInstance())

        tagsButtonFeed.setBackgroundResource(R.drawable.rounded_button_feed)
        tagsButtonFeed.setTextColor(Color.parseColor("#FFFFFF"))

        //Cargando FeedUser
        userButtonFeed.setOnClickListener {
            userButtonFeed.setBackgroundResource(R.drawable.rounded_button_feed)
            userButtonFeed.setTextColor(Color.parseColor("#FFFFFF"))
            tagsButtonFeed.setBackgroundColor(Color.TRANSPARENT)
            tagsButtonFeed.setTextColor(Color.parseColor("#000000"))
            changeFeed(FeedUsersFragment.newInstance())
        }

        //Cargando FeedTags
        tagsButtonFeed.setOnClickListener {
            tagsButtonFeed.setBackgroundResource(R.drawable.rounded_button_feed)
            tagsButtonFeed.setTextColor(Color.parseColor("#FFFFFF"))
            userButtonFeed.setBackgroundColor(Color.TRANSPARENT)
            userButtonFeed.setTextColor(Color.parseColor("#000000"))
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

