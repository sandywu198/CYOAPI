package com.example.chooseyourownapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
//import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {
    private lateinit var characterList: MutableList<String>
    private lateinit var rvCharacters: RecyclerView
//    var disneyImageURL = ""
//    var disneyName = ""
//    var disneyMovieShow = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvCharacters = findViewById(R.id.disneyList)
        characterList = mutableListOf()
        getDisneyImage()
//        val button = findViewById<Button>(R.id.characterButton)
//        val imageView = findViewById<ImageView>(R.id.characterImage)
//        val characterName = findViewById<TextView>(R.id.characterName)
//        val characterFrom = findViewById<TextView>(R.id.characterFrom)
//        Log.d("disneyImageURL", "disney image URL set")
//        characterName.getNextImage(button, imageView,characterFrom,characterName)

    }
//    private fun TextView.getNextImage(button: Button, imageView: ImageView, from: TextView, name : TextView) {
//        button.setOnClickListener {
//            getDisneyImage()
//
//            Glide.with(this@MainActivity)
//                . load(disneyImageURL)
//                .fitCenter()
//                .into(imageView)
//
//            from.text = disneyName
//            name.text = disneyMovieShow
//        }
//    }
    private fun getDisneyImage() {
        val client = AsyncHttpClient()
        client["https://api.disneyapi.dev/characters", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                val characterArray = json.jsonObject.getJSONArray("Character")
                for (i in 0 until characterArray.length()) {
                    characterList.add(characterArray.getString(i))
                }
                val adapter = DisneyAdapter(characterList)
                rvCharacters.adapter = adapter
                rvCharacters.layoutManager = LinearLayoutManager(this@MainActivity)
                Log.d("Character", "response successful$json")
//                disneyImageURL = json.jsonObject.getString("imageUrl")
//                disneyName = json.jsonObject.getString("name")
//                if(json.jsonObject.getString("films").equals("[]")){
//                    if(json.jsonObject.getString("shortFilms").equals("[]")){
//                        disneyMovieShow = json.jsonObject.getString("tvShows")
//                        disneyMovieShow = disneyMovieShow.replace("[", " ")
//                        disneyMovieShow = disneyMovieShow.replace("]", " ")
//                    } else{
//                        disneyMovieShow = json.jsonObject.getString("shortFilms")
//                        disneyMovieShow = disneyMovieShow.replace("[", " ")
//                        disneyMovieShow = disneyMovieShow.replace("]", " ")
//                    }
//                } else{
//                    disneyMovieShow = json.jsonObject.getString("films")
//                    disneyMovieShow = disneyMovieShow.replace("[", " ")
//                    disneyMovieShow = disneyMovieShow.replace("]", " ")
//                }
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
    class DisneyAdapter (private val characterList: List<String>): RecyclerView.Adapter<DisneyAdapter.ViewHolder>(){
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val petImage: ImageView

            init {
                // Find our RecyclerView item's ImageView for future use
                petImage = view.findViewById(R.id.characterImage)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.disney_item, parent, false)

            return ViewHolder(view)
        }

        override fun getItemCount() = characterList.size


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Glide.with(holder.itemView)
                .load(characterList[position])
                .centerCrop()
                .into(holder.petImage)    }
    }
}