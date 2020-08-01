package `in`.asclepius.app.dailogs

import `in`.asclepius.app.R
import `in`.asclepius.app.databinding.LoadingViewBinding
import android.app.Dialog
import android.content.Context
import android.os.Bundle


class LoadingDialog(context: Context) : Dialog(context, R.style.Theme_Transparent) {

    lateinit var binding : LoadingViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=LoadingViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCancelable(false)
    }

    fun setTitle(message: String){
        binding.title.text=message
    }

}