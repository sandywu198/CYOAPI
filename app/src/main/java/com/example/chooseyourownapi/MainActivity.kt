package com.example.chooseyourownapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var characterList: MutableList<String>
    private lateinit var rvCharacters: RecyclerView
    private lateinit var nameList: MutableList<String>
    private lateinit var rvName: RecyclerView
    private lateinit var fromList: MutableList<String>
    private lateinit var rvFrom: RecyclerView
    var disneyImageURL = ""
    var disneyName = ""
    var disneyMovieShow = ""
//    val from = 10
//    val to = 7526
//    val random = Random
//    var amplititudes  = IntArray(20) { random.nextInt(to - from) +  from }.asList()
//    var round = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvCharacters = findViewById(R.id.disneyList)
        characterList = mutableListOf()
        rvName = findViewById(R.id.disneyNameCharacter)
        nameList = mutableListOf()
        rvFrom = findViewById(R.id.disneyInfo)
        fromList = mutableListOf()
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
        for (i in 0 until 20) {
            val client = AsyncHttpClient()
            val num = Random.nextInt(10,7526)
//            val num = amplititudes[i]
            val string = "https://api.disneyapi.dev/character/$num"
            client[string, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON){
                    Log.d("Character", "response successful$json")
                    disneyImageURL = json.jsonObject.getString("imageUrl")
                    characterList.add(disneyImageURL)
                    disneyName = json.jsonObject.getString("name")
                    if(json.jsonObject.getString("films").equals("[]")){
                        if(json.jsonObject.getString("shortFilms").equals("[]")){
                            disneyMovieShow = json.jsonObject.getString("tvShows")
                            disneyMovieShow = disneyMovieShow.replace("[", " ")
                            disneyMovieShow = disneyMovieShow.replace("]", " ")
                            Log.d("Show", "response successful$json")
                        } else{
                            disneyMovieShow = json.jsonObject.getString("shortFilms")
                            disneyMovieShow = disneyMovieShow.replace("[", " ")
                            disneyMovieShow = disneyMovieShow.replace("]", " ")
                            Log.d("Show", "response successful$json")
                        }
                    } else{
                        disneyMovieShow = json.jsonObject.getString("films")
                        disneyMovieShow = disneyMovieShow.replace("[", " ")
                        disneyMovieShow = disneyMovieShow.replace("]", " ")
                        Log.d("Show", "response successful$json")
                    }
                    nameList.add(disneyName)
                    fromList.add(disneyMovieShow)
                    val adapter = DisneyAdapter(characterList,nameList,fromList)
                    rvCharacters.adapter = adapter
                    rvCharacters.layoutManager = LinearLayoutManager(this@MainActivity)
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
    class DisneyAdapter (private val characterList: List<String>, private val nameList: List<String>, private val fromList: List<String>): RecyclerView.Adapter<DisneyAdapter.ViewHolder>(){
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val disneyImage: ImageView
            var disneyName: TextView
            var disneyFrom: TextView

            init {
                // Find our RecyclerView item's ImageView for future use
                disneyImage = view.findViewById(R.id.characterImage)
                disneyName = view.findViewById(R.id.characterName)
                disneyFrom = view.findViewById(R.id.characterShow)
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
                .fitCenter()
                .into(holder.disneyImage)
            val string = fromList[position]
            holder.disneyFrom.text = "$string"
            val string2 = nameList[position]
            holder.disneyName.text = "$string2"
        }
    }
}