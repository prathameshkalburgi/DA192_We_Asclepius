package `in`.asclepius.app

import `in`.asclepius.app.activities.BookAppointment
import `in`.asclepius.app.databinding.ActivityMainBinding
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext=this

        binding.bookAppointmentCard.setOnClickListener(View.OnClickListener {
            startActivity(Intent(mContext, BookAppointment::class.java))
        })


    }
}