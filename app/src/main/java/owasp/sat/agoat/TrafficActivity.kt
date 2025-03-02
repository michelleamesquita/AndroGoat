package owasp.sat.agoat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import okhttp3.*
import java.io.IOException

class TrafficActivity : AppCompatActivity() {
    var httpurl = "http://demo.testfire.net"
    var httpsurl = "https://owasp.org"

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic)

        val txtString = findViewById<TextView>(R.id.result)
        val HttpsButton = findViewById<Button>(R.id.httpsButton)
        val PinningButton = findViewById<Button>(R.id.PinningButton)
        val HttpButton = findViewById<Button>(R.id.httpButton)

        // Enviar requisição HTTP
        HttpButton.setOnClickListener {
            runRequest(httpurl)
        }

        // Enviar requisição HTTPS
        HttpsButton.setOnClickListener {
            runRequest(httpsurl)
        }

        // Chamada de Pinning
        PinningButton.setOnClickListener {
            doPinning()
        }
    }

    private fun runRequest(url: String) {
        try {
            val request = Request.Builder()
                .url(url)
                .build()

            Toast.makeText(this, "Request sent to $url. Please intercept using Proxy", Toast.LENGTH_LONG).show()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("HTTP Request", "Failed: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.d("HTTP Request", "Response: ${response.body?.string()}")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun doPinning() {
        GlobalScope.launch(Dispatchers.IO) {  // Executando em background
            val url = "owasp.org"
            try {
                val pinner = CertificatePinner.Builder()
                    .add(url, "sha256/gdU/UHClHJBFbIdeKuyHm/Lq/aQvMLyuTtcvTEE/1JQ=")
                    .add(url, "sha256/YLh1dUR9y6Kja30RrAn7JKnbQG/uEtLMkBgFF2Fuihg=")
                    .add(url, "sha256/Vjs8r4z+80wjNcr1YKepWQboSIRi63WsWXhIMN+eWys=")
                    .build()

                val client = OkHttpClient.Builder().certificatePinner(pinner).build()
                val request = Request.Builder()
                    .url("https://$url")
                    .build()

                val response = client.newCall(request).execute()
                val responseData = response.body?.string()

                // Atualizando UI na thread principal
                withContext(Dispatchers.Main) {
                    Log.v("Response", responseData ?: "Empty Response")
                    Toast.makeText(this@TrafficActivity, "Pinning Verification Passed", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@TrafficActivity, "Pinning Verification Failed", Toast.LENGTH_LONG).show()
                }
                e.printStackTrace()
            }
        }
    }
}
