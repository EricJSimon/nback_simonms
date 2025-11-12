package mobappdev.example.nback_cimpl.ui.viewmodels

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.GameApplication
import mobappdev.example.nback_cimpl.NBackHelper
import mobappdev.example.nback_cimpl.data.UserPreferencesRepository
import java.util.Locale

/**
 * This is the GameViewModel.
 *
 * It is good practice to first make an interface, which acts as the blueprint
 * for your implementation. With this interface we can create fake versions
 * of the viewmodel, which we can use to test other parts of our app that depend on the VM.
 *
 * Our viewmodel itself has functions to start a game, to specify a gametype,
 * and to check if we are having a match
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */


interface GameViewModel {
    val gameState: StateFlow<GameState>
    val score: StateFlow<Int>
    val highscore: StateFlow<Int>
    val nBack: Int

    fun setGameType(gameType: GameType)
    fun startGame()

    fun stopGame()

    fun checkMatch()

}

class GameVM(
    private val userPreferencesRepository: UserPreferencesRepository, private val context: Context
) : GameViewModel, ViewModel(), TextToSpeech.OnInitListener {

    private val _gameState = MutableStateFlow(GameState())
    override val gameState: StateFlow<GameState>
        get() = _gameState.asStateFlow()

    private lateinit var tts: TextToSpeech
    private var isTtsInitialized = false

    private var lastResponseTick = -1

    private val _score = MutableStateFlow(0)
    override val score: StateFlow<Int>
        get() = _score

    private val _highscore = MutableStateFlow(0)
    override val highscore: StateFlow<Int>
        get() = _highscore

    // nBack is currently hardcoded
    override val nBack: Int = 1

    private var job: Job? = null  // coroutine job for the game event
    private val eventInterval: Long = 2000L  // 2000 ms (2s)

    private val nBackHelper = NBackHelper()  // Helper that generate the event array
    private var events = emptyArray<Int>()  // Array with all events

    override fun setGameType(gameType: GameType) {
        // update the gametype in the gamestate
        _gameState.value = _gameState.value.copy(gameType = gameType)
    }

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language not supported")
            } else {
                isTtsInitialized = true
            }
        } else {
            Log.e("TTS", "Init Failed")
        }
    }

    override fun startGame() {
        job?.cancel()  // Cancel any existing game loop

        _score.value = 0
        lastResponseTick = -1
        _gameState.value = _gameState.value.copy(
            eventValue = -1, tick = 0
        )

        // Get the events from our C-model (returns IntArray, so we need to convert to Array<Int>)
        // Todo Higher Grade: currently the size etc. are hardcoded, make these based on user input
        events = nBackHelper.generateNBackString(10, 9, 30, nBack).toList().toTypedArray()
        Log.d("GameVM", "The following sequence was generated: ${events.contentToString()}")

        job = viewModelScope.launch {
            when (gameState.value.gameType) {
                GameType.Audio -> runAudioGame(events)
                GameType.AudioVisual -> runAudioVisualGame()
                GameType.Visual -> runVisualGame(events)
            }
            //_highscore.value = score.value
        }
    }

    override fun stopGame() {
        job?.cancel()

        if (_score.value > _highscore.value) {
            viewModelScope.launch {
                _highscore.value = score.value
            }
        }

        events = emptyArray()
        _score.value = 0
        lastResponseTick - 1
        _gameState.value = _gameState.value.copy(
            eventValue = -1, tick = 0
        )
    }


    override fun checkMatch() {
        val currentTick = _gameState.value.tick

        if (currentTick == lastResponseTick || currentTick == 0) {
            return
        }
        lastResponseTick = currentTick
        when (gameState.value.gameType) {
            GameType.Audio -> checkAudioMatch()
            GameType.AudioVisual -> checkAudioVisualMatch()
            GameType.Visual -> checkVisualMatch()

        }
    }

    private fun checkAudioMatch() {
        checkVisualMatch()
    }

    private fun checkVisualMatch() {
        val currentEventIndex = _gameState.value.tick - 1

        if (currentEventIndex < nBack) {
            _score.value--
            return
        }

        val currentEventValue = events[currentEventIndex]
        val nBackEventValue = events[currentEventIndex - nBack]

        Log.d(
            "CheckMatch",
            "Comparing current (index $currentEventIndex, value $currentEventValue) with n-back (index ${currentEventIndex - nBack}, value $nBackEventValue)"
        )


        if (currentEventValue == nBackEventValue) {
            _score.value++
        } else {
            _score.value--
        }
    }

    private fun checkAudioVisualMatch() {
        /*TODO*/
    }

    private suspend fun runAudioGame(events: Array<Int>) {
        if (!isTtsInitialized) {
            Log.e("TTS", "TTS not initialized")
            return
        }
        for (value in events) {
            _gameState.value = _gameState.value.copy(
                eventValue = value, tick = _gameState.value.tick + 1
            )
            val numberToLetter = (value + 64).toChar().toString()

            tts.speak(numberToLetter, TextToSpeech.QUEUE_FLUSH, null, null)
            delay(eventInterval)
        }
    }

    private suspend fun runVisualGame(events: Array<Int>) {
        for (value in events) {
            _gameState.value = _gameState.value.copy(
                eventValue = value, tick = _gameState.value.tick + 1
            )
            delay(eventInterval)
        }

    }

    private fun runAudioVisualGame() {
        // Todo: Make work for Higher grade
    }

    override fun onCleared() {
        if (this::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onCleared()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GameApplication)
                GameVM(
                    application.userPreferencesRespository, application.applicationContext
                )
            }
        }
    }

    init {
        // Code that runs during creation of the vm
        viewModelScope.launch {
            userPreferencesRepository.highscore.collect {
                _highscore.value = it
            }
        }
    }
}

// Class with the different game types
enum class GameType {
    Audio, Visual, AudioVisual
}

data class GameState(
    // You can use this state to push values from the VM to your UI.
    val gameType: GameType = GameType.Visual,  // Type of the game
    val eventValue: Int = -1,  // The value of the array string
    val tick: Int = 0
)

class FakeVM : GameViewModel {
    override val gameState: StateFlow<GameState>
        get() = MutableStateFlow(GameState()).asStateFlow()
    override val score: StateFlow<Int>
        get() = MutableStateFlow(2).asStateFlow()
    override val highscore: StateFlow<Int>
        get() = MutableStateFlow(42).asStateFlow()
    override val nBack: Int
        get() = 2

    override fun setGameType(gameType: GameType) {
    }

    override fun startGame() {
    }

    override fun stopGame() {
    }

    override fun checkMatch() {
    }
}