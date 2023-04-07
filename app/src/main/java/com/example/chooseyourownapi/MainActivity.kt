package com.example.chooseyourownapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    var disneyImageURL = ""
    var disneyName = ""
    var disneyMovieShow = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getDisneyImage()
        val button = findViewById<Button>(R.id.characterButton)
        val imageView = findViewById<ImageView>(R.id.characterImage)
        val characterName = findViewById<TextView>(R.id.characterName)
        val characterFrom = findViewById<TextView>(R.id.characterFrom)
        Log.d("disneyImageURL", "disney image URL set")
        characterName.getNextImage(button, imageView,characterFrom,characterName)

    }
    private fun TextView.getNextImage(button: Button, imageView: ImageView, from: TextView, name : TextView) {
        button.setOnClickListener {
            getDisneyImage()

            Glide.with(this@MainActivity)
                . load(disneyImageURL)
                .fitCenter()
                .into(imageView)

            from.text = disneyName
            name.text = disneyMovieShow
        }
    }
    private fun getDisneyImage() {
        val client = AsyncHttpClient()
        val num = Random.nextInt(10,7526)
        val string = "https://api.disneyapi.dev/characters/$num"
        client[string, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Character", "response successful$json")
                disneyImageURL = json.jsonObject.getString("imageUrl")
                disneyName = json.jsonObject.getString("name")
                if(json.jsonObject.getString("films").equals("[]")){
                    if(json.jsonObject.getString("shortFilms").equals("[]")){
                        disneyMovieShow = json.jsonObject.getString("tvShows")
                        disneyMovieShow = disneyMovieShow.replace("[", " ")
                        disneyMovieShow = disneyMovieShow.replace("]", " ")
                    } else{
                        disneyMovieShow = json.jsonObject.getString("shortFilms")
                        disneyMovieShow = disneyMovieShow.replace("[", " ")
                        disneyMovieShow = disneyMovieShow.replace("]", " ")
                    }
                } else{
                    disneyMovieShow = json.jsonObject.getString("films")
                    disneyMovieShow = disneyMovieShow.replace("[", " ")
                    disneyMovieShow = disneyMovieShow.replace("]", " ")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Character Error", errorResponse)
            }
        }]
    }
}