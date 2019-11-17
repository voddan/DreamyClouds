package org.vodopyan.rainbowl.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.play_pause_button_view.view.*
import org.vodopyan.rainbowl.R
import org.vodopyan.rainbowl.utils.MediatorLiveData
import org.vodopyan.rainbowl.utils.observe

/**
 * Controls for an audio player
 */
@SuppressLint("ViewConstructor")
class PlayPauseButtonView<Parent>(parent: Parent, attrs: AttributeSet? = null)
    : ConstraintLayout(parent, attrs) where Parent: Context, Parent: LifecycleOwner
{
    val state: MutableLiveData<LiveData<Boolean>> = MutableLiveData()
    val playCallback: MutableLiveData<() -> Unit> = MutableLiveData()
    val pauseCallback: MutableLiveData<() -> Unit> = MutableLiveData()

    private val changeListeners = MediatorLiveData(playCallback, pauseCallback)

    private val playIcon = resources.getDrawable(R.drawable.ic_play_arrow_black_24dp, null)
    private val pauseIcon = resources.getDrawable(R.drawable.ic_pause_black_24dp, null)

    init {
        val view = LayoutInflater.from(parent).inflate(R.layout.play_pause_button_view, /*root=*/this, /*attachToRoot=*/true)

        state.observe(parent) { isPlayingState ->
            isPlayingState.observe(parent) { isPlaying ->
                val icon = if (isPlaying) pauseIcon else playIcon
                view.button.setImageDrawable(icon)
            }
        }

        changeListeners.observe(parent) {
            val playCallback = playCallback.value
            val pauseCallback = pauseCallback.value

            if(playCallback != null && pauseCallback != null) {
                view.button.setOnClickListener {
                    val isPlaying = state.value?.value
                    when(isPlaying) {
                        true -> pauseCallback()
                        false -> playCallback()
                        null -> { /*do nothing*/ }
                    }
                }
            }
        }
    }
}