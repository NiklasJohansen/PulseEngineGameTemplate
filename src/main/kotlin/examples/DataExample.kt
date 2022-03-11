package examples

import no.njoh.pulseengine.core.PulseEngine
import no.njoh.pulseengine.core.PulseEngineGame
import no.njoh.pulseengine.core.shared.utils.Logger

fun main() = PulseEngine.run(DataExample::class)

class DataExample : PulseEngineGame()
{
    private var currentPosition: Float = 0f
    private var lastposition: Float = 0f

    override fun onCreate()
    {
        // Runtime stats
        val fps = engine.data.currentFps
        val renderTime = engine.data.renderTimeMs
        val updateTime = engine.data.updateTimeMS
        val fixedUpdateTime = engine.data.fixedUpdateTimeMS
        val totalMemory = engine.data.totalMemory
        val usedMemory = engine.data.usedMemory

        // Metrics will be graphed by the Profiler widget (above stats are graphed by default)
        engine.data.addMetric("Position", "px") { currentPosition }

        // Load game state from internal class path
        val internalState = engine.data.loadObject<GameState>("examples/data/internalGameState.dat", fromClassPath = true)
        Logger.info("Loaded internal game state: $internalState")

        // Check if external game state exists
        if (engine.data.exists("externalGameState.dat"))
        {
            // Load game state form external save directory (loadObjectAsync is a non-blocking alternative)
            val externalState = engine.data.loadObject<GameState>("externalGameState.dat")
            Logger.info("Loaded external game state: $externalState from ${engine.data.saveDirectory}")
        }
        else Logger.info("External game state does not yet exist at ${engine.data.saveDirectory}")

        // Set tick rate to low value to show interpolation in action
        engine.config.fixedTickRate = 5
    }

    override fun onFixedUpdate()
    {
        // Store last position in order to linearly interpolate between fixed update steps
        lastposition = currentPosition

        // Using fixedDeltaTime enables changes to fixedTickRate without speed of game logic being affected
        currentPosition += 100f * engine.data.fixedDeltaTime
    }

    override fun onUpdate()
    {
        // Use deltaTime to maintain consistent game speed in update step
        val dt = engine.data.deltaTime
    }

    override fun onRender()
    {
        // Draw text at current position
        engine.gfx.mainSurface.drawText("Not Interpolated", currentPosition, 300f)

        // Create linearly interpolated position
        val i = engine.data.interpolation
        val interpolatedPos = (1f - i) * lastposition + i * currentPosition

        // Draw text at interpolated position
        engine.gfx.mainSurface.drawText("Interpolated", interpolatedPos, 500f)
    }

    override fun onDestroy()
    {
        // Save game state to external directory (saveStateAsync is a non-blocking alternative)
        engine.data.saveObject(GameState("External game state!", 1234), "externalGameState.dat")
        Logger.info("Saved external game state to: ${engine.data.saveDirectory}")
    }

    // Example game state class
    data class GameState(val text: String, val number: Int)
}