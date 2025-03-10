package examples

import no.njoh.pulseengine.core.PulseEngine
import no.njoh.pulseengine.core.PulseEngineGame
import no.njoh.pulseengine.core.shared.utils.Logger

fun main() = PulseEngine.run(DataExample::class)

class DataExample : PulseEngineGame()
{
    private var currentPosition: Float = 0f
    private var lastPosition: Float = 0f

    override fun onCreate()
    {
        // Runtime stats are found in the data module, e.g. current FPS
        val fps = engine.data.currentFps

        // Metrics will be graphed by the MetricViewer widget (above stats are graphed by default)
        engine.data.addMetric("Position (px)") { sample(currentPosition) }

        // Load game state from internal class path
        val internalState = engine.data.loadObject<GameState>("examples/data/internal_game_state.dat", fromClassPath = true)
        Logger.info { "Loaded internal game state: $internalState" }

        // Check if external game state exists
        if (engine.data.exists("external_game_state.dat"))
        {
            // Load game state form external save directory (loadObjectAsync is a non-blocking alternative)
            val externalState = engine.data.loadObject<GameState>("external_game_state.dat")
            Logger.info { "Loaded external game state: $externalState from ${engine.data.saveDirectory}" }
        }
        else Logger.info {"External game state does not yet exist at ${engine.data.saveDirectory}" }

        // Set tick rate to low value to show interpolation in action
        engine.config.fixedTickRate = 5
    }

    override fun onFixedUpdate()
    {
        // Store last position in order to linearly interpolate between fixed update steps
        lastPosition = currentPosition

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
        val interpolatedPos = (1f - i) * lastPosition + i * currentPosition

        // Draw text at interpolated position
        engine.gfx.mainSurface.drawText("Interpolated", interpolatedPos, 500f)
    }

    override fun onDestroy()
    {
        // Save game state to external directory (saveStateAsync is a non-blocking alternative)
        engine.data.saveObject(GameState("External game state!", 1234), "external_game_state.dat")
        Logger.info { "Saved external game state to: ${engine.data.saveDirectory}" }
    }

    // Example game state class
    data class GameState(val text: String, val number: Int)
}