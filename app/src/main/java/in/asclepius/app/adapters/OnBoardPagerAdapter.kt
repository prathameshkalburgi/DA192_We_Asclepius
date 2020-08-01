package `in`.asclepius.app.adapters

import `in`.asclepius.app.R
import `in`.asclepius.app.databinding.OnboardFirstViewBinding
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.forEachIndexed
import androidx.recyclerview.widget.RecyclerView

class OnBoardPagerAdapter(
    private val count: Int,
    private val context: Context
) : RecyclerView.Adapter<OnBoardPagerAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = OnboardFirstViewBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.onboard_first_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return count;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (position) {
            0 -> with(holder.binding) {
                image.setAnimation(R.raw.find_doctors_anim)
                featureText.text = context.resources.getString(R.string.on_board_message_three)
                selectors.forEachIndexed { index, view ->
                    if (index == position) {
                        view.background.setTint(
                            ContextCompat.getColor(
                                context,
                                R.color.colorAccent
                            )
                        )
                    } else {
                        view.background.setTint(
                            ContextCompat.getColor(
                                context,
                                R.color.lightAccent
                            )
                        )
                    }
                }
            }


            1 -> with(holder.binding) {
                image.setAnimation(R.raw.waiting_doctors_anim)
                featureText.text = context.resources.getString(R.string.on_board_message_two)
                selectors.forEachIndexed { index, view ->
                    if (index == position) {
                        view.background.setTint(
                            ContextCompat.getColor(
                                context,
                                R.color.colorAccent
                            )
                        )
                    } else {
                        view.background.setTint(
                            ContextCompat.getColor(
                                context,
                                R.color.lightAccent
                            )
                        )
                    }
                }
            }

            2 -> with(holder.binding) {
                image.setAnimation(R.raw.opd_desk_anim)
                featureText.text = context.resources.getString(R.string.on_board_message_one)
                selectors.forEachIndexed { index, view ->
                    if (index == position) {
                        view.background.setTint(
                            ContextCompat.getColor(
                                context,
                                R.color.colorAccent
                            )
                        )
                    } else {
                        view.background.setTint(
                            ContextCompat.getColor(
                                context,
                                R.color.lightAccent
                            )
                        )
                    }
                }

            }


        }

    }
}
