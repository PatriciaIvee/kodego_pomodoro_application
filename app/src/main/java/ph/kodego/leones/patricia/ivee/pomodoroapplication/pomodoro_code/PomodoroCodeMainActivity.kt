package ph.kodego.leones.patricia.ivee.pomodoroapplication.pomodoro_code

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ph.kodego.leones.patricia.ivee.pomodoroapplication.R
import ph.kodego.leones.patricia.ivee.pomodoroapplication.databinding.ActivityPomodoroCodeMainBinding

class PomodoroCodeMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPomodoroCodeMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPomodoroCodeMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.tvStart.setOnClickListener {
            val studyTime = binding.etStudy.text.toString()
            val breakTime = binding.etBreak.text.toString()
            val roundCount = binding.etRound.text.toString()

            if (studyTime.isNotEmpty() && breakTime.isNotEmpty() && roundCount.isNotEmpty()){
                val intent = Intent(this,PomodoroCodeFeedActivity::class.java)
                intent.putExtra("study",studyTime.toInt())
                intent.putExtra("break",breakTime.toInt())
                intent.putExtra("round",roundCount.toInt())
                startActivity(intent)
            }else{
                Toast.makeText(this,"Fill fields above",Toast.LENGTH_SHORT).show()
            }

        }
    }
    }
