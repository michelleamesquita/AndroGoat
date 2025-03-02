package owasp.sat.agoat

import android.app.DownloadManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DownloadInvoiceService : Service() {

    override fun onCreate() {
        Log.i("DOWNLOAD", "Service onCreate")
        Toast.makeText(applicationContext, "Service Created", Toast.LENGTH_LONG).show()
        //stopSelf()
    }

    override fun onBind(intent: Intent): IBinder? {
        //TODO("Return the communication channel to the service.")
        //throw UnsupportedOperationException("Not yet implemented")
        Log.i("DOWNLOAD", "Service onBind")
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("DOWNLOAD", "Invoice is being downloaded")
        downloadFile()
        Toast.makeText(applicationContext, "Invoice is being downloaded", Toast.LENGTH_LONG).show()
        return Service.START_STICKY
    }

    override fun onDestroy() {
        Log.i("DOWNLOAD", "Service onDestroy")
    }

    fun downloadFile() {
        CoroutineScope(Dispatchers.IO).launch {
            val url1 = "https://github.com/satishpatnayak/MyTest/blob/master/AndroGoatInvoice.txt"
            val url: Uri = Uri.parse(url1)
            val request = DownloadManager.Request(url)
            val fileName: String = url.lastPathSegment ?: "downloaded_file"

            request.apply {
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                setTitle(fileName)
                setDescription("The File is downloading...")
                allowScanningByMediaScanner()
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${System.currentTimeMillis()}")
            }

            val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
        }
    }
}

