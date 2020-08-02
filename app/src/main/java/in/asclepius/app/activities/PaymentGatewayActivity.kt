package `in`.asclepius.app.activities

import `in`.asclepius.app.R
import `in`.asclepius.app.databinding.ActivityPaymentGatewayBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class PaymentGatewayActivity : AppCompatActivity() {

    lateinit var binding: ActivityPaymentGatewayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentGatewayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarCard.toolbar.title = getString(R.string.choose_payment_method)
    }
}