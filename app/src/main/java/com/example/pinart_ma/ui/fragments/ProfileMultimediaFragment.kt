package com.example.pinart_ma.ui.fragments

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.Multimedia
import com.example.pinart_ma.ui.adapter.ProfileMultimediaAdapter
import com.example.pinart_ma.ui.adapter.TagsFeedAdapter
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.MultimediaViewModel
import kotlinx.android.synthetic.main.fragment_feed_users.*
import kotlinx.android.synthetic.main.profile_multimedia_fragment.*

class ProfileMultimediaFragment(var idUsuario: String?, var mine: Boolean) : Fragment() {
    companion object {
        fun newInstance(idUsuario: String?, mine: Boolean): ProfileMultimediaFragment = ProfileMultimediaFragment(idUsuario, mine)
    }

    var multimedia = ArrayList<Multimedia>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.profile_multimedia_fragment, container, false)
        loadMultimediaUser(context)
        return view
    }


    fun loadMultimediaUser(context: Context?){
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val token = myPreferences.getString("token", "unknown")

        var multimediaFactory = InjectorUtils.providerMultimediaViewModelFactory()
        var multimediaViewModel = ViewModelProviders.of(this, multimediaFactory).get(
            MultimediaViewModel::class.java);

        multimediaViewModel!!.getMultimediaByUser(token, idUsuario).observe(viewLifecycleOwner, Observer {
                multimediaUserLive ->
                for(i in 0 until multimediaUserLive.size){
                    multimedia.add(multimediaUserLive[i])
                }
                Toast.makeText(context, multimedia.size.toString(), Toast.LENGTH_SHORT).show()

                recyclerViewProfileMultimedia.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                recyclerViewProfileMultimedia.adapter = ProfileMultimediaAdapter(multimedia, mine)

        })
    }


}