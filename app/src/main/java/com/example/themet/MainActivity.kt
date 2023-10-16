package com.example.themet

import com.squareup.picasso.Picasso
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            requestArtwork()
        }
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

    private fun updateUI(result: Artwork?) {
        if (result != null) {
            val textView = findViewById<TextView>(R.id.text_view)
            textView.text = "Artwork Title: ${result.title}"
            val textView2 = findViewById<TextView>(R.id.text_view2)
            if (result.artist == "") {
                textView2.text = "Artwork Artist: Unknown"
            } else {
                textView2.text = "Artwork Artist: ${result.artist}"
            }
            val textView3 = findViewById<TextView>(R.id.text_view3)
            if (result.primaryImage == "") {
                textView3.text = "Artwork Photo: Unknown"
            } else {
                textView3.text = "Artwork Photo"
                val imageUrl = result.primaryImage
                val imageView = findViewById<ImageView>(R.id.image_view)
                Picasso.get().load(imageUrl).into(imageView)
            }

        } else {
            // Handle error
        }
    }

    private fun requestArtwork() {
        GlobalScope.launch(Dispatchers.Main) {
            val artwork = fetchArtwork()
            updateUI(artwork)
        }
    }
}
