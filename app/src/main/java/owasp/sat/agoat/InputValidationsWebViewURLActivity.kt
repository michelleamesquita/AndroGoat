package owasp.sat.agoat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InputValidationsWebViewURLActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_validations_web_view_url)
        val load = findViewById<Button>(R.id.load)
        val webview11: WebView = findViewById(R.id.webview1)
        val url = findViewById<TextView>(R.id.url)

        load.setOnClickListener {
            // webview1.webChromeClient= WebChromeClient()
            val webSettings1 = webview11.settings
            webSettings1.javaScriptEnabled = true

            val urlString: String = url.text.toString()
            CoroutineScope(Dispatchers.Main).launch {
                loadUrlInWebView(webview11, urlString)
            }
        }
    }

    private suspend fun loadUrlInWebView(webView: WebView, url: String) {
        withContext(Dispatchers.IO) {
            // Simulação de uma operação de carregamento, se necessário
            // Aqui você pode adicionar lógica adicional se precisar
        }
        webView.loadUrl(url)
    }
}
