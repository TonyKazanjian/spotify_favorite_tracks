package com.tonykazanjian.spotifyproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log

import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import com.spotify.sdk.android.player.Config
import com.spotify.sdk.android.player.ConnectionStateCallback
import com.spotify.sdk.android.player.Error
import com.spotify.sdk.android.player.Player
import com.spotify.sdk.android.player.PlayerEvent
import com.spotify.sdk.android.player.Spotify
import com.spotify.sdk.android.player.SpotifyPlayer
import com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE
import com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE





class MainActivity : Activity(), Player.NotificationCallback, ConnectionStateCallback {

    private var mPlayer: Player? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val builder = AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
        builder.setScopes(arrayOf("user-read-private", "streaming"))
        val request = builder.build()

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode === REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, intent)
            if (response.type == AuthenticationResponse.Type.TOKEN) {
                val playerConfig = Config(this, response.accessToken, CLIENT_ID)
                Spotify.getPlayer(playerConfig, this, object : SpotifyPlayer.InitializationObserver {
                    override fun onInitialized(spotifyPlayer: SpotifyPlayer) {
                        mPlayer = spotifyPlayer
                        spotifyPlayer.addConnectionStateCallback(this@MainActivity)
                        spotifyPlayer.addNotificationCallback(this@MainActivity)
                    }

                    override fun onError(throwable: Throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.message)
                    }
                })
            }
        }
    }

    override fun onDestroy() {
        Spotify.destroyPlayer(this)
        super.onDestroy()
    }

    override fun onPlaybackEvent(playerEvent: PlayerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name)
        when (playerEvent) {
        // Handle event type as necessary
            else -> {
            }
        }
    }

    override fun onPlaybackError(error: Error) {
        Log.d("MainActivity", "Playback error received: " + error.name)
        when (error) {
        // Handle error type as necessary
            else -> {
            }
        }
    }

    override fun onLoggedIn() {
        Log.d("MainActivity", "User logged in")

        // This is the line that plays a song.
        mPlayer?.playUri(null, "spotify:track:2TpxZ7JUBn3uw46aR7qd6V", 0, 0);
    }

    override fun onLoggedOut() {
        Log.d("MainActivity", "User logged out")
    }

    override fun onLoginFailed(var1: Error) {
        Log.d("MainActivity", "Login failed")
    }

    override fun onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred")
    }

    override fun onConnectionMessage(message: String) {
        Log.d("MainActivity", "Received connection message: " + message)
    }

    companion object {
        private val CLIENT_ID = "552049c9e6154462afc26562a4f22719"

        private val REDIRECT_URI = "http://spotifylikedproject.com/callback"
    }
}
