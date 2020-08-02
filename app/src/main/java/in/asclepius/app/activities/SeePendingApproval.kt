package `in`.asclepius.app.activities

import `in`.asclepius.app.databinding.ActivitySeePendingApprovalBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SeePendingApproval : AppCompatActivity() {

    lateinit var binding: ActivitySeePendingApprovalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeePendingApprovalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}