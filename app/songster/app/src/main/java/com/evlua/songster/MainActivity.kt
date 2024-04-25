package com.evlua.songster

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.evlua.songster.ui.theme.SongsterTheme
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse


class MainActivity : ComponentActivity() {
    var loggedIn = false
    var firstGame = true
    var spotifyAppRemote: SpotifyAppRemote? = null
    val REQUEST_CODE = 2108
    val REDIRECT_URI = "https://localhost:8080/songster"
    val CLIENT_ID = "fdd2008fd2cb41048e9032d3349fca07"
    val connectionParams = ConnectionParams.Builder(CLIENT_ID)
        .setRedirectUri(REDIRECT_URI)
        .showAuthView(true)
        .build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SongsterTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column (verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                        OutlinedButton(onClick = { if (loggedIn){ handleCameraPermission()}else{ logIn()} }) {
                            Text(text = "Scan Card!")
                       }
                    }
                }
            }
        }
    }

    private fun logIn(){ //Without Auth library
        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("MainActivity", "Connected! Yay!")
                // Now you can start interacting with App Remote
                loggedIn = true
                handleCameraPermission()
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })
    }


    private val cameraPermissionRequestLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted: proceed with opening the camera
                startQR()
            } else {
                // Permission denied: inform the user to enable it through settings
                Toast.makeText(
                    this,
                    "Go to settings and enable camera permission to use this feature",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private val barcodeLauncher = registerForActivityResult<ScanOptions, ScanIntentResult>(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this@MainActivity, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            Log.d("songster", result.contents)
            var type = result.contents.split('/')[3]
            var id = result.contents.split('/')[4].split('?')[0]
            Log.d("songster", id)
            Log.d("songster", type)
            spotifyAppRemote?.playerApi?.play("spotify:"+type+":"+id)

        }
    }


    // Checks camera permission and either starts the camera directly or requests permission
    fun handleCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is already granted: start the camera
                startQR()
            }

            else -> {
                // Permission is not granted: request it
                cameraPermissionRequestLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    // Starts the default camera app for taking a picture
    private fun startQR() {
        // Camera app is available: launch it
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan a qrcode")
        options.setCameraId(0) // Use a specific camera of the device
        options.setOrientationLocked(false)
        barcodeLauncher.launch(options)

    }

}




