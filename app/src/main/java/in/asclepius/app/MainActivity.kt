package `in`.asclepius.app

import `in`.asclepius.app.activities.BookAppointment
import `in`.asclepius.app.activities.SeePendingApproval
import `in`.asclepius.app.databinding.ActivityMainBinding
import `in`.asclepius.app.others.Constants
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var mContext: Context
    var roleOfUser: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this

        binding.bookAppointmentCard.setOnClickListener(View.OnClickListener {
            startActivity(Intent(mContext, BookAppointment::class.java))
        })

        roleOfUser = intent.getStringExtra(Constants.USER_TYPE)

        if (roleOfUser != null) {
            if (roleOfUser == Constants.ADMIN_ROLES[1]) {
                binding.normalUserLayout.visibility = View.GONE
                binding.superAdminLayout.visibility = View.VISIBLE
            } else if (roleOfUser == Constants.ADMIN_ROLES[2]) {
                binding.normalUserLayout.visibility = View.GONE
                binding.organizationHeadLayout.visibility = View.VISIBLE
            }
        }

        binding.seePendingApprovals.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@MainActivity, SeePendingApproval::class.java))
        })

    }
}