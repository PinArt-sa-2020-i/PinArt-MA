package com.example.pinart_ma.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pinart_ma.R
import com.example.pinart_ma.ui.adapter.FollowTagAdapter
import com.example.pinart_ma.ui.adapter.FollowUserAdapter
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.TagViewModel
import com.example.pinart_ma.viewModel.UserViewModel
import kotlinx.android.synthetic.main.fragment_follows.*

class FollowsFragment(var list: String?) : Fragment() {
    companion object {
        fun newInstance(list: String?): FollowsFragment = FollowsFragment(list)
    }

    var followers: ArrayList<ArrayList<String>> = arrayListOf()
    var followings: ArrayList<ArrayList<String>> = arrayListOf()
    var tags: ArrayList<String> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_follows, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followersFollows.setOnClickListener {
            list = "followers"
            followersFollows.setBackgroundResource(R.drawable.rounded_button_feed)
            followersFollows.setTextColor(Color.parseColor("#FFFFFF"))
            followingFollows.setBackgroundColor(Color.TRANSPARENT)
            followingFollows.setTextColor(Color.parseColor("#000000"))
            tagsFollows.setBackgroundColor(Color.TRANSPARENT)
            tagsFollows.setTextColor(Color.parseColor("#000000"))
            likesFollows.setBackgroundColor(Color.TRANSPARENT)
            likesFollows.setTextColor(Color.parseColor("#000000"))

            showFollowers()
        }

        followingFollows.setOnClickListener {
            list = "followings"
            followingFollows.setBackgroundResource(R.drawable.rounded_button_feed)
            followingFollows.setTextColor(Color.parseColor("#FFFFFF"))
            followersFollows.setBackgroundColor(Color.TRANSPARENT)
            followersFollows.setTextColor(Color.parseColor("#000000"))
            tagsFollows.setBackgroundColor(Color.TRANSPARENT)
            tagsFollows.setTextColor(Color.parseColor("#000000"))
            likesFollows.setBackgroundColor(Color.TRANSPARENT)
            likesFollows.setTextColor(Color.parseColor("#000000"))

            showFollowings()
        }

        tagsFollows.setOnClickListener {
            list = "tags"
            tagsFollows.setBackgroundResource(R.drawable.rounded_button_feed)
            tagsFollows.setTextColor(Color.parseColor("#FFFFFF"))
            followersFollows.setBackgroundColor(Color.TRANSPARENT)
            followersFollows.setTextColor(Color.parseColor("#000000"))
            followingFollows.setBackgroundColor(Color.TRANSPARENT)
            followingFollows.setTextColor(Color.parseColor("#000000"))
            likesFollows.setBackgroundColor(Color.TRANSPARENT)
            likesFollows.setTextColor(Color.parseColor("#000000"))

            showTags()
        }

        likesFollows.setOnClickListener{
            list = "likes"
            likesFollows.setBackgroundResource(R.drawable.rounded_button_feed)
            likesFollows.setTextColor(Color.parseColor("#FFFFFF"))
            followersFollows.setBackgroundColor(Color.TRANSPARENT)
            followersFollows.setTextColor(Color.parseColor("#000000"))
            followingFollows.setBackgroundColor(Color.TRANSPARENT)
            followingFollows.setTextColor(Color.parseColor("#000000"))
            tagsFollows.setBackgroundColor(Color.TRANSPARENT)
            tagsFollows.setTextColor(Color.parseColor("#000000"))


        }
    }

    override fun onResume() {
        super.onResume()
        if(list == "followers"){showFollowers()}
        if(list == "followings"){showFollowings()}
        if(list == "tags"){showTags()}
    }


    fun showFollowers(){
        followers = arrayListOf()
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val token = myPreferences.getString("token", "unknown")
        val id = myPreferences.getString("id", "unknown")

        var userFactory = InjectorUtils.provideUserViewModelFactory()
        var userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java);

        userViewModel.getAllUserFollow(token).observe(viewLifecycleOwner, Observer { results ->
            for(result in results){
                if(result[2] == id){followers.add(result)}
            }
            recyclerViewFollows.layoutManager = LinearLayoutManager(context)
            recyclerViewFollows.adapter = FollowUserAdapter(followers, "followers", this)
        })
    }

    fun showFollowings(){
        followings = arrayListOf()
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val token = myPreferences.getString("token", "unknown")
        val id = myPreferences.getString("id", "unknown")

        var userFactory = InjectorUtils.provideUserViewModelFactory()
        var userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java);

        userViewModel.getAllUserFollow(token).observe(viewLifecycleOwner, Observer { results ->
            for(result in results){
                if(result[1] == id){followings.add(result)}
            }
            recyclerViewFollows.layoutManager = LinearLayoutManager(context)
            recyclerViewFollows.adapter = FollowUserAdapter(followings, "following", this)
        })
    }

    fun showTags(){
        tags = arrayListOf()
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val token = myPreferences.getString("token", "unknown")
        val id = myPreferences.getString("id", "unknown")

        var tagFactory = InjectorUtils.providerTagViewModelFactory()
        var tagViewModel = ViewModelProviders.of(this, tagFactory).get(TagViewModel::class.java);


        tagViewModel!!.getTagFolledByUser(token, id).observe(viewLifecycleOwner, Observer {
            results ->
            for(result in results){
                tags.add(result)
            }
            recyclerViewFollows.layoutManager = LinearLayoutManager(context)
            recyclerViewFollows.adapter = FollowTagAdapter(tags, this)
        })
    }


}