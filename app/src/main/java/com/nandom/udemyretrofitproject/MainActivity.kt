package com.nandom.udemyretrofitproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivityTAG"
    private lateinit var retService : AlbumService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retService = RetrofitInstance.getRetrofitInstance().create(AlbumService::class.java)

        getRequestWithQueryParameters()
//        getRequestWithPathParameters()

    }

    private fun getRequestWithQueryParameters()     {

        val responseLiveData: LiveData<Response<Albums>> = liveData {
//            val response = retService.getAlbums()
            val response = retService.getSortedAlbums(3)
            emit(response)
        }

        responseLiveData.observe(this, Observer {
            val albumsList = it.body()?.listIterator()

            if (albumsList != null) {
                while (albumsList.hasNext()) {
                    val albumsItem = albumsList.next()
                    val result = " Album Title : ${albumsItem.title} \n" +
                            " Album id : ${albumsItem.id} \n" +
                            " Album userID : ${albumsItem.userId} \n\n\n"
                    textView.append(result)
                    Log.i(TAG, "onCreate: " + albumsItem.title)
                }
            }
        })
    }

    private fun getRequestWithPathParameters(){
        val pathResponse : LiveData<Response<AlbumsItem>> = liveData {
            val response = retService.getAlbum(3)
            emit(response)
        }

        pathResponse.observe(this, Observer {
            val title = it.body()?.title
            Toast.makeText(this, title, Toast.LENGTH_SHORT).show()
        })
    }
}