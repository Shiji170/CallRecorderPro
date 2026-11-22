package com.example.callrecorderpro

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.widget.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var currentFile: File? = null
    private lateinit var status: TextView
    private lateinit var list: LinearLayout
    private lateinit var recordButton: Button
    private val requestCode = 41

    private fun recordingsDir(): File {
        val dir = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "CallRecorderPro")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUi()
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), requestCode)
        } else refreshFiles()
    }

    private fun buildUi() {
        val scroll = ScrollView(this)
        val panel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 28, 24, 24)
            setBackgroundColor(0xFFF5F7FA.toInt())
        }
        val title = TextView(this).apply {
            text = "Call Recorder Pro"
            textSize = 26f
            setTextColor(0xFF152238.toInt())
            gravity = Gravity.CENTER
        }
        val subtitle = TextView(this).apply {
            text = "No Ads  •  Private audio recordings"
            textSize = 14f
            gravity = Gravity.CENTER
            setPadding(0, 8, 0, 22)
        }
        status = TextView(this).apply {
            text = "Ready to record"
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(8, 16, 8, 16)
        }
        recordButton = Button(this).apply {
            text = "●  START RECORDING"
            setOnClickListener { if (recorder == null) startRecording() else stopRecording() }
        }
        val note = TextView(this).apply {
            text = "Important: This app records sound received by the phone microphone. For the other caller's voice, speakerphone may be needed. Android/WhatsApp may block call audio capture on some devices. Test legally and inform the other participant."
            textSize = 13f
            setTextColor(0xFF7A4B00.toInt())
            setPadding(12, 16, 12, 18)
        }
        val heading = TextView(this).apply {
            text = "Saved recordings"
            textSize = 20f
            setTextColor(0xFF152238.toInt())
            setPadding(0, 12, 0, 8)
        }
        list = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        panel.addView(title)
        panel.addView(subtitle)
        panel.addView(status)
        panel.addView(recordButton)
        panel.addView(note)
        panel.addView(heading)
        panel.addView(list)
        scroll.addView(panel)
        setContentView(scroll)
    }

    private fun startRecording() {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), requestCode)
            return
        }
        try {
            val stamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            currentFile = File(recordingsDir(), "Recording_$stamp.m4a")
            val r = if (Build.VERSION.SDK_INT >= 31) MediaRecorder(this) else MediaRecorder()
            r.setAudioSource(MediaRecorder.AudioSource.MIC)
            r.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            r.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            r.setAudioEncodingBitRate(128000)
            r.setAudioSamplingRate(44100)
            r.setOutputFile(currentFile!!.absolutePath)
            r.prepare()
            r.start()
            recorder = r
            status.text = "● Recording…"
            recordButton.text = "■  STOP RECORDING"
        } catch (e: Exception) {
            recorder?.release()
            recorder = null
            currentFile?.delete()
            currentFile = null
            status.text = "Could not start recording: ${e.localizedMessage ?: "Unknown error"}"
        }
    }

    private fun stopRecording() {
        try {
            recorder?.stop()
            status.text = "Recording saved"
        } catch (_: Exception) {
            currentFile?.delete()
            status.text = "Recording failed or was too short"
        } finally {
            recorder?.release()
            recorder = null
            currentFile = null
            recordButton.text = "●  START RECORDING"
            refreshFiles()
        }
    }

    private fun refreshFiles() {
        list.removeAllViews()
        val files = recordingsDir().listFiles()?.filter { it.isFile && it.extension == "m4a" }
            ?.sortedByDescending { it.lastModified() } ?: emptyList()
        if (files.isEmpty()) {
            list.addView(TextView(this).apply { text = "No recordings yet"; setPadding(4, 12, 4, 12) })
        }
        files.forEach { file ->
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(12, 12, 12, 12)
                setBackgroundColor(0xFFFFFFFF.toInt())
            }
            val name = TextView(this).apply {
                text = file.name
                textSize = 15f
                setTextColor(0xFF152238.toInt())
            }
            val actions = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
            val play = Button(this).apply {
                text = "PLAY"
                setOnClickListener { playFile(file) }
            }
            val delete = Button(this).apply {
                text = "DELETE"
                setOnClickListener {
                    stopPlayer()
                    file.delete()
                    refreshFiles()
                }
            }
            actions.addView(play, LinearLayout.LayoutParams(0, -2, 1f))
            actions.addView(delete, LinearLayout.LayoutParams(0, -2, 1f))
            row.addView(name)
            row.addView(actions)
            list.addView(row)
            val gap = Space(this)
            list.addView(gap, LinearLayout.LayoutParams(1, 10))
        }
    }

    private fun playFile(file: File) {
        stopPlayer()
        try {
            player = MediaPlayer().apply {
                setDataSource(file.absolutePath)
                setOnCompletionListener { stopPlayer() }
                prepare()
                start()
            }
            status.text = "Playing: ${file.name}"
        } catch (e: Exception) {
            status.text = "Cannot play this recording"
            stopPlayer()
        }
    }

    private fun stopPlayer() {
        try { player?.stop() } catch (_: Exception) {}
        player?.release()
        player = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.requestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) refreshFiles()
            else status.text = "Microphone permission is required to record"
        }
    }

    override fun onDestroy() {
        if (recorder != null) stopRecording()
        stopPlayer()
        super.onDestroy()
    }
}
