package ph.kodego.leones.patricia.ivee.pomodoroapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import ph.kodego.leones.patricia.ivee.pomodoroapplication.databinding.ActivityMainBinding
import ph.kodego.leones.patricia.ivee.pomodoroapplication.fragments.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
//    private lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_home -> replaceFragment(HomeFragment())
                R.id.menu_set_task -> replaceFragment(SetTaskFragment())
                R.id.menu_timer -> replaceFragment(PomodoroTimerFragment())
                R.id.menu_settings -> replaceFragment(SettingsFragment())
                R.id.menu_stats -> replaceFragment(StatsFragment())

                else ->{

                }
            }
            true
        }

//        val bottomNav:BottomNavigationView = binding.bottomNav
//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.home_fragment,R.id.set_task_fragment,R.id.pomodoro_timer_fragment,R.id.stats_fragment, R.id.settings_fragment
//            )
//        )

//        navController= Navigation.findNavController(this,R.id.nav_host_fragment_activity_main)
//        setupWithNavController(binding.bottomNav,navController)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        bottomNav.setupWithNavController(navController)



    }

    private fun replaceFragment(fragment:Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main,fragment)
        fragmentTransaction.commit()

    }
}