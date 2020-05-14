package com.example.pinart_ma.ui.fragments

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
            showFollowers()
        }

        followingFollows.setOnClickListener {
            list = "followings"
            showFollowings()
        }

        tagsFollows.setOnClickListener {
            list = "tags"
            showTags()
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