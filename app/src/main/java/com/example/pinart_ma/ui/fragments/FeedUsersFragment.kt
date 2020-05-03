package com.example.pinart_ma.ui.fragments

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelProviders.of
import androidx.lifecycle.ViewModelStores.of
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.Multimedia
import com.example.pinart_ma.ui.adapter.UserFeedAdapter
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.MultimediaViewModel
import kotlinx.android.synthetic.main.fragment_feed_users.*
import java.util.Optional.of

class FeedUsersFragment: Fragment() {
    companion object {
        fun newInstance(): FeedUsersFragment = FeedUsersFragment()
    }

    var feed = ArrayList<Multimedia>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_feed_users, container, false)
        data(context)
        return view
    }



    fun data(context: Context?) {

        var multimediaFactory =InjectorUtils.providerMultimediaViewModelFactory()
        var multimediaViewModel = ViewModelProviders.of(this, multimediaFactory).get(MultimediaViewModel::class.java)

        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val id: String? = myPreferences.getString("id", "unknown")
        var token: String? = myPreferences.getString("token", "unknown")


        multimediaViewModel!!.getFeedUsers(token, id).observe(viewLifecycleOwner, Observer {
            multimediaList ->
            for(i in 0 until multimediaList.size){
                feed.add(multimediaList[i])
            }
            Toast.makeText(context, feed.size.toString(), Toast.LENGTH_SHORT).show()
            recyclerViewFeedUsers.layoutManager =  GridLayoutManager(context, 2)
            recyclerViewFeedUsers.adapter = UserFeedAdapter(feed)

        })
    }



}