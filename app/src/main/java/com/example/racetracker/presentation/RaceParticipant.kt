package com.example.racetracker.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.racetracker.presentation.RaceParticipant
import kotlinx.coroutines.delay
import kotlin.coroutines.cancellation.CancellationException

class RaceParticipant(
    val name: String,
    val maxProgress: Int = 100 ,
    val progressDelayMillis: Long = 500L,
    private  val progressIncrement: Int=1,
    private val  initialProgress : Int =0
){
    init{
        require(maxProgress > 0){"maxProgress = $maxProgress; must be > 0"}
        require(progressIncrement > 0){"progressIncrement = $progressIncrement; must be > 0"}
    }
    /**
     * Indicates the race participant's current progress
     */
    var currentProgress by mutableStateOf(initialProgress)
    private set

    suspend fun run(){
       try {
           while (currentProgress < maxProgress) {
               delay(progressDelayMillis)
               currentProgress += progressIncrement
           }
       } catch (e: CancellationException) {
            Log.e("RaceParticipant", "$name: ${e.message}")
            throw e // Always re-throw CancellationException.
        }
    }

    /**
     * Regardless of the value of [initialProgress] the reset function will reset the
     * [currentProgress] to 0
     */
    fun reset(){
        currentProgress = 0
    }
}
/**
 * The Linear progress indicator expects progress value in the range of 0-1. This property
 * calculate the progress factor to satisfy the indicator requirements.
 * This property calculates the participant's progress as a percentage of the maximum progress.
 */

val RaceParticipant.progressFactor: Float
    get() = currentProgress/maxProgress.toFloat()