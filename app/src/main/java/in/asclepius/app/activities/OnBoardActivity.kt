package `in`.asclepius.app.activities

import `in`.asclepius.app.MainActivity
import `in`.asclepius.app.adapters.OnBoardPagerAdapter
import `in`.asclepius.app.databinding.ActivityOnBoardBinding
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth


class OnBoardActivity : AppCompatActivity() {

    lateinit var binding: ActivityOnBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOnBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFeatureAdapter();
        binding.nextFab.visibility= View.VISIBLE
        binding.getStarted.visibility= View.GONE
        binding.getStarted.setOnClickListener(View.OnClickListener { startActivity(Intent(this@OnBoardActivity,LoginActivity::class.java)) })
    }

    private fun setFeatureAdapter() {
        val adapter= OnBoardPagerAdapter(3,this)
        binding.viewPager.adapter=adapter
        binding.viewPager.setPageTransformer(MarginPageTransformer(25))

        binding.nextFab.setOnClickListener(View.OnClickListener {
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem+1,true)
            if(binding.viewPager.currentItem==2)
            {
                binding.nextFab.visibility= View.GONE
                binding.getStarted.visibility= View.VISIBLE
            }
        })

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(binding.viewPager.currentItem==2)
                {
                    binding.nextFab.visibility= View.GONE
                    binding.getStarted.visibility= View.VISIBLE
                } else {
                    binding.nextFab.visibility = View.VISIBLE
                    binding.getStarted.visibility = View.GONE
                }
            }
        })

    }


    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}