package com.example.pinart_ma.ui.fragments

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.pinart_ma.R
import com.example.pinart_ma.service.model.Tag
import com.example.pinart_ma.service.model.User
import com.example.pinart_ma.ui.adapter.SearchTagsAdapter
import com.example.pinart_ma.ui.adapter.SearchUsersAdapter
import com.example.pinart_ma.utils.InjectorUtils
import com.example.pinart_ma.viewModel.TagViewModel
import com.example.pinart_ma.viewModel.UserViewModel
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment: Fragment() {
    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }

    var typeSearch: String = "users"

    var usersList: ArrayList<User> = ArrayList<User>()
    var tagsList: ArrayList<Tag> = ArrayList<Tag>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_search, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Carga el Inicio
        changeFragment(SearchInitFragment.newInstance())

        //Pone los funcionalidades botones
        addFuntionalityButtons()

        //Load User data
        loadUsers(context)

        loadTags(context)

        //Esta pendiente a la escucha del search
        searchViewSearch.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                proccesSearch(newText)
                return false
            }
        })

    }

    //Poner Botones a la escucha
    fun addFuntionalityButtons(){
        buttonUsersSearch.setOnClickListener {
            typeSearch = "users"
            proccesSearch(searchViewSearch.query)
        }

        buttonTagsSearch.setOnClickListener {
            typeSearch = "tags"
            proccesSearch(searchViewSearch.query)
        }
    }


    //Cambia el fragmento
    fun changeFragment(fragment: Fragment){
        getChildFragmentManager().beginTransaction().apply {
            replace(R.id.containerFrameLayaoutFragmentSearch, fragment)
            commit()
        }
    }

    //Procesando Change Text
    fun proccesSearch(newText: CharSequence?){
        if (newText.toString() == ""){
            changeFragment(SearchInitFragment.newInstance())
        }else {
            if (typeSearch == "tags") {
                var adapterTags: SearchTagsAdapter = SearchTagsAdapter(tagsList)
                adapterTags.getFilter().filter(newText)
                var fragment: SearchTagsFragment = SearchTagsFragment(adapterTags)
                changeFragment(fragment)
            }
            else{
                var adapterUsers: SearchUsersAdapter = SearchUsersAdapter(usersList)
                adapterUsers.getFilter().filter(newText)
                var fragment: SearchUsersFragment = SearchUsersFragment(adapterUsers)
                changeFragment(fragment)
            }
        }
    }


    fun loadUsers(context: Context?){
        var userFactory = InjectorUtils.provideUserViewModelFactory()
        var userViewModel = ViewModelProviders.of(this, userFactory).get(UserViewModel::class.java)

        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        var token: String? = myPreferences.getString("token", "unknown")

        userViewModel!!.getAllUsers(token).observe(viewLifecycleOwner, Observer {
            userList ->
            for (i in 0 until userList.size){
                usersList.add(userList[i])
            }
        })
    }

    fun loadTags(context: Context?){
        var tagFactory = InjectorUtils.providerTagViewModelFactory()
        var tagViewModel = ViewModelProviders.of(this, tagFactory).get(TagViewModel::class.java)

        val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        var token: String? = myPreferences.getString("token", "unknown")

        tagViewModel!!.getAllTags(token).observe(viewLifecycleOwner, Observer {
            tagList ->
            for (i in 0 until tagList.size){
                tagsList.add(tagList[i])
            }

        })
    }


    fun createDataTag(): ArrayList<Tag>{
        return arrayListOf(Tag("1", "Futbol", "Descrion Generiica"),
            Tag("2", "Rap", "Descrion Generiica"),
            Tag("3", "Messi", "Descrion Generiica"),
            Tag("4", "Otro", "Descrion Generiica"),
            Tag("2", "Rap", "Descrion Generiica"),
            Tag("3", "Messi", "Descrion Generiica"),
            Tag("4", "Otro", "Descrion Generiica"))
    }

    fun createDataUser() : ArrayList<User>{
        return  arrayListOf( User(1, "CamiloGil", "Camilo", "Gil", null, null),
                             User(2, "Andres", "Camilo", "Gil", null, null),
                             User(3, "Tom", "Camilo", "Gil", null, null),
                             User(4, "Mesier", "Camilo", "Gil", null, null))

    }

}


