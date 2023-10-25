package com.example.themet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Headers
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.random.Random


data class Artwork(val title: String, val artist: String, val primaryImage: String)

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestArtwork()
    }

    private suspend fun fetchArtwork(): Artwork? = suspendCancellableCoroutine { continuation ->
        try {
            val randomNumber = Random.nextInt(1, 1000)
            val client = AsyncHttpClient()
            val params = RequestParams()
            params["limit"] = "5"
            params["page"] = "0"

            client.get(
                "https://collectionapi.metmuseum.org/public/collection/v1/objects/$randomNumber",
                params, object : JsonHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Headers,
                        json: JSON
                    ) {
                        val jsonObject = json.jsonObject
                        val artwork = Artwork(
                            jsonObject.getString("title"),
                            jsonObject.getString("artistDisplayName"),
                            jsonObject.getString("primaryImage"),
                            )
                        continuation.resume(artwork)
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        errorResponse: String,
                        t: Throwable?
                    ) {
                        continuation.resume(null)
                    }
                })
        } catch (e: Exception) {
            continuation.resumeWithException(e)
        }
    }

    private fun updateUI(result: List<Artwork>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = ArtworkAdapter(result)
    }


    private fun requestArtwork() {
        GlobalScope.launch(Dispatchers.Main) {
            val artwork1 = fetchArtwork()
            val artwork2 = fetchArtwork()
            val artwork3 = fetchArtwork()
            val artwork4 = fetchArtwork()
            val artwork5 = fetchArtwork()
            val artworkList = mutableListOf<Artwork>()
            artworkList.add(artwork1!!)
            artworkList.add(artwork2!!)
            artworkList.add(artwork3!!)
            artworkList.add(artwork4!!)
            artworkList.add(artwork5!!)
            updateUI(artworkList)
        }
    }
}


