package com.example.chooseyourownapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    var disneyImageURL = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getDisneyImage()
        Log.d("disneyImageURL", "disney image URL set")
        val button = findViewById<Button>(R.id.characterButton)
        val imageView = findViewById<ImageView>(R.id.characterImage)
        getNextImage(button, imageView)
    }
    private fun getNextImage(button: Button, imageView: ImageView) {
        button.setOnClickListener {
            getDisneyImage()

            Glide.with(this)
                . load(disneyImageURL)
                .fitCenter()
                .into(imageView)
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