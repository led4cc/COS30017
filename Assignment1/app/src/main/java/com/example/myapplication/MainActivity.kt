package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.widget.ScrollView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    companion object { private const val TAG = "Game" }

    private var state = GameState()

    private lateinit var scoreText: TextView
    private lateinit var holdText: TextView
    private lateinit var btnClimb: Button
    private lateinit var btnFall: Button
    private lateinit var btnReset: Button
    private lateinit var logText: TextView
    private lateinit var logScroll: ScrollView
    private val logs = ArrayList<String>()  // survives rotation via onSaveInstanceState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // <-- use our XML

        // restore on rotation
        savedInstanceState?.let {
            state = GameState(
                hold = it.getInt("hold", 0),
                score = it.getInt("score", 0),
                fallen = it.getBoolean("fallen", false)
            )
        }

        // find views
        scoreText = findViewById(R.id.scoreText)
        holdText  = findViewById(R.id.holdText)
        btnClimb  = findViewById(R.id.btnClimb)
        btnFall   = findViewById(R.id.btnFall)
        btnReset  = findViewById(R.id.btnReset)
        logText   = findViewById(R.id.logText)
        logScroll = findViewById(R.id.logScroll)

        // restore logs on rotation

        if (savedInstanceState == null) logEvent("App started: hold=${state.hold}, score=${state.score}")
        else renderLog()
        // handlers
        btnClimb.setOnClickListener {
            state = climb(state)
            Log.d(TAG, "climb -> hold=${state.hold} score=${state.score}")
            render()
        }
        btnFall.setOnClickListener {
            state = fall(state)
            Log.d(TAG, "fall  -> hold=${state.hold} score=${state.score} fallen=${state.fallen}")
            render()
        }
        btnReset.setOnClickListener {
            state = reset()
            Log.d(TAG, "reset -> hold=${state.hold} score=${state.score}")
            render()
        }
        btnClimb.setOnClickListener {
            state = climb(state)
            Log.d(TAG, "climb -> hold=${state.hold} score=${state.score}")
            logEvent("Climb → hold=${state.hold}, score=${state.score}")
            render()
        }

        btnFall.setOnClickListener {
            state = fall(state)
            Log.d(TAG, "fall  -> hold=${state.hold} score=${state.score} fallen=${state.fallen}")
            if (state.hold >= 9) logEvent("Fall at top → no penalty. Reset to try again.")
            else if (state.hold == 0) logEvent("Fall ignored at hold 0.")
            else logEvent("Fall → -3, score=${state.score}. Climb disabled until reset.")
            render()
        }

        btnReset.setOnClickListener {
            state = reset()
            Log.d(TAG, "reset -> hold=${state.hold} score=${state.score}")
            logEvent("Reset → hold=0, score=0")
            render()
        }

        render()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("hold", state.hold)
        outState.putInt("score", state.score)
        outState.putBoolean("fallen", state.fallen)
        outState.putStringArrayList("logs", ArrayList(logs))

        super.onSaveInstanceState(outState)
    }

    private fun render() {
        scoreText.text = getString(R.string.score_format, state.score)
        holdText.text  = getString(R.string.hold_format,  state.hold)

        val zoneColorRes = when (zoneForHold(state.hold)) {
            1 -> R.color.zoneBlue
            2 -> R.color.zoneGreen
            3 -> R.color.zoneRed
            else -> R.color.scoreDefault
        }
        scoreText.setTextColor(ContextCompat.getColor(this, zoneColorRes))

        btnClimb.isEnabled = !state.fallen && state.hold < 9
        btnFall.isEnabled  = state.hold > 0
    }
    private fun logEvent(msg: String) {
        val ts = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        logs.add("[$ts] $msg")
        if (logs.size > 200) logs.removeAt(0)  // keep it light
        renderLog()
    }

    private fun renderLog() {
        logText.text = logs.joinToString("\n")
        logScroll.post { logScroll.fullScroll(android.view.View.FOCUS_DOWN) }
    }

}
