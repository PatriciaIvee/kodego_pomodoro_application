package ph.kodego.leones.patricia.ivee.pomodoroapplication.pomodoro_code

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import ph.kodego.leones.patricia.ivee.pomodoroapplication.R
import ph.kodego.leones.patricia.ivee.pomodoroapplication.databinding.ActivityPomodoroCodeFeedBinding

class PomodoroCodeFeedActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPomodoroCodeFeedBinding
    private var studyMinute: Int? = null
    private var breakMinute: Int? = null
    private var roundCount: Int? = null

    private var restTimer: CountDownTimer? = null
    private var studyTimer: CountDownTimer? = null
    private var breakTimer: CountDownTimer? = null

    private var mRound = 1

    private var isStudy = true

    private var isStop = false

    private var mp: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPomodoroCodeFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Receive Extras
        studyMinute = intent.getIntExtra("study", 0) * 60 * 1000
        breakMinute = intent.getIntExtra("break", 0) * 60 * 1000
        roundCount = intent.getIntExtra("round", 0)
        // Set Rounds Text
        binding.tvRound.text = "$mRound/$roundCount"
        //Start Timer
        setRestTimer()
        // Reset Button
        binding.ivStop.setOnClickListener {
            resetOrStart()
        }
    }
    // Set Rest Timer
    private fun setRestTimer(){
        playSound()
        binding.tvStatus.text = "Get Ready"
        binding.progressBar.progress = 0
        binding.progressBar.max = 10
        restTimer = object : CountDownTimer(10500,1000) {
            override fun onTick(p0: Long) {
                binding.progressBar.progress = (p0 / 1000).toInt()
                binding.tvTimer.text = (p0 / 1000).toString()
            }
            override fun onFinish() {
                mp?.reset()
                if (isStudy){
                    setupStudyView()
                }else{
                    setupBreakView()
                }
            }
        }.start()
    }
    // Set Study Timer
    private fun setStudyTimer(){

        studyTimer = object : CountDownTimer(studyMinute!!.toLong() + 500,1000) {
            override fun onTick(p0: Long) {
                binding.progressBar.progress = (p0 /1000).toInt()
                binding.tvTimer.text = createTimeLabels((p0 / 1000).toInt())
            }
            override fun onFinish() {
                if(mRound < roundCount!!){
                    isStudy = false
                    setRestTimer()
                    mRound++
                }else{
                    clearAttribute()
                    binding.tvStatus.text = "You have finish your rounds :)"
                }
            }
        }.start()
    }
    // Set Break Timer
    private fun setBreakTimer() {
        breakTimer = object : CountDownTimer(breakMinute!!.toLong()+500, 1000 ) {
            override fun onTick(p0: Long) {
                binding.progressBar.progress = (p0 / 1000).toInt()
                binding.tvTimer.text = createTimeLabels((p0 / 1000).toInt())
            }

            override fun onFinish() {
                isStudy = true
                setRestTimer()
            }

        }.start()
    }
    // Prepare Screen for Study Timer
    private fun setupStudyView() {
        binding.tvRound.text = "$mRound/$roundCount"
        binding.tvStatus.text = "Study Time"
        binding.progressBar.max = studyMinute!!/1000

        if (studyTimer != null)
            studyTimer = null

        setStudyTimer()
    }
    // Prepare Screen for Study Timer
    private fun setupBreakView() {
        binding.tvStatus.text = "Break Time"
        binding.progressBar.max = breakMinute!!/1000

        if (breakTimer != null)
            breakTimer = null

        setBreakTimer()
    }
    // Initialize sound file to MediaPlayer
    private fun playSound() {

        try {
            val soundUrl = Uri.parse("android.resource://com.exo.pomodoro/" + R.raw.count_down)
            mp = MediaPlayer.create(this,soundUrl)
            mp?.isLooping = false
            mp?.start()
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
    // Rest Whole Attributes in FeedActivity
    private fun clearAttribute() {
        binding.tvStatus.text = "Press Play Button to Restart"
        binding.ivStop.setImageResource(R.drawable.ic_play)
        binding.progressBar.progress = 0
        binding.tvTimer.text = "0"
        mRound = 1
        binding.tvRound.text = "$mRound/$roundCount"
        restTimer?.cancel()
        studyTimer?.cancel()
        breakTimer?.cancel()
        mp?.reset()
        isStop = true
    }
    // Convert Received Numbers to Minutes and Seconds
    private fun createTimeLabels(time : Int): String {
        var timeLabel = ""
        val minutes = time / 60
        val secends = time % 60

        if (minutes < 10) timeLabel += "0"
        timeLabel += "$minutes:"

        if (secends < 10) timeLabel += "0"
        timeLabel += secends

        return timeLabel
    }
    // For Reset or Restart Pomodoro
    private fun resetOrStart() {
        if (isStop){
            binding.ivStop.setImageResource(R.drawable.ic_stop)
            setRestTimer()
            isStop = false
        }else
            clearAttribute()

    }
    // Clear Everything When App Destroyed
    override fun onDestroy() {
        super.onDestroy()
        restTimer?.cancel()
        studyTimer?.cancel()
        breakTimer?.cancel()
        mp?.reset()
    }


}
