package com.example.pinart_ma.ui.fragments

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.Multimedia
import com.example.pinart_ma.service.model.Tag
import com.example.pinart_ma.ui.adapter.TagsFeedAdapter
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.MultimediaViewModel
import com.example.pinart_ma.viewModel.TagViewModel
import kotlinx.android.synthetic.main.fragment_feed_users.*
import kotlinx.android.synthetic.main.tag_fragment.*
import android.graphics.Color

class TagFragment(var idTag: String?) : Fragment() {

    companion object {
        fun newInstance(idTag: String?): TagFragment = TagFragment(idTag)
    }


    var followTag: Boolean = false
    var multimediaTag: ArrayList<Multimedia> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.tag_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDataTag(context)
        loadFollowButton(context)
        loadMultimediaTag(context)

        buttonFollowTag.setOnClickListener {
            loadFuntionalityButton(context)
        }
    }


    fun loadDataTag(context: Context?){
        var tagFactory = InjectorUtils.providerTagViewModelFactory()
        var tagViewModel = ViewModelProviders.of(this, tagFactory).get(TagViewModel::class.java)

        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        var token: String? = myPreferences.getString("token", "unknown")


        tagViewModel!!.getTagById(token, idTag).observe(viewLifecycleOwner, Observer {
            tag ->
            if(tag == null){}
            else{
                nameTagTag.text = tag.name
                descriptionTagTag.text = tag.description
            }
        })
    }



    fun loadFollowButton(context: Context?){
        var tagFactory = InjectorUtils.providerTagViewModelFactory()
        var tagViewModel = ViewModelProviders.of(this, tagFactory).get(TagViewModel::class.java)

        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        var token: String? = myPreferences.getString("token", "unknown")
        var id: String? = myPreferences.getString("id", "unknown")

        tagViewModel!!.getTagFolledByUser(token, id).observe(viewLifecycleOwner, Observer {
            listTags ->
            followTag  = false
            for(i in 0 until  listTags.size){
                if(listTags[i] == idTag){
                    followTag = true
                    break
                }
            }

            if(followTag == true){
                buttonFollowTag.setBackgroundResource(R.drawable.rounded_search)
                buttonFollowTag.setTextColor(Color.parseColor("#000000"))
                buttonFollowTag.text = "Dejar de Seguir"
            }
            else{
                buttonFollowTag.setBackgroundResource(R.drawable.rounded_followbutton)
                buttonFollowTag.setTextColor(Color.parseColor("#FFFFFF"))
                buttonFollowTag.text = "Seguir"
            }
        })
    }


    fun loadMultimediaTag(context: Context?){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        var token: String? = myPreferences.getString("token", "unknown")

        var multimediaFactory = InjectorUtils.providerMultimediaViewModelFactory()
        var multimediaViewModel = ViewModelProviders.of(this, multimediaFactory).get(MultimediaViewModel::class.java)

        multimediaViewModel!!.getMultimediaByTag(token, idTag).observe(viewLifecycleOwner, Observer {
            multimediaList ->
            for (i in 0 until multimediaList.size){
                multimediaTag.add(multimediaList[i])
            }

            containerTagFragment.layoutManager=  StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            containerTagFragment.adapter = TagsFeedAdapter(multimediaTag)
        })
    }

    fun loadFuntionalityButton(context: Context?){
        var tagFactory = InjectorUtils.providerTagViewModelFactory()
        var tagViewModel = ViewModelProviders.of(this, tagFactory).get(TagViewModel::class.java)

        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        var token: String? = myPreferences.getString("token", "unknown")
        var id: String? = myPreferences.getString("id", "unknown")

        if(followTag == false){
            tagViewModel!!.followTag(token, id, idTag).observe(viewLifecycleOwner, Observer {
                result ->
                if(result == 0){}
                else{
                    followTag = true
                    buttonFollowTag.text = "Dejar de Seguir"
                    buttonFollowTag.setBackgroundResource(R.drawable.rounded_search)
                    buttonFollowTag.setTextColor(Color.parseColor("#000000"))
                }

            })
        }
        else{
            tagViewModel!!.unFollowTag(token, id, idTag).observe(viewLifecycleOwner, Observer {
                result ->
                if(result == 0){}
                else{
                    followTag = false
                    buttonFollowTag.text = "Seguir"
                    buttonFollowTag.setBackgroundResource(R.drawable.rounded_followbutton)
                    buttonFollowTag.setTextColor(Color.parseColor("#FFFFFF"))
                }
            })

        }



    }
}