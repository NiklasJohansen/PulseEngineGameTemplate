package examples

import no.njoh.pulseengine.core.PulseEngine
import no.njoh.pulseengine.core.PulseEngineGame
import no.njoh.pulseengine.core.asset.types.Sound
import no.njoh.pulseengine.core.input.Key
import no.njoh.pulseengine.core.input.MouseButton
import no.njoh.pulseengine.core.shared.primitives.Color
import no.njoh.pulseengine.core.shared.utils.Logger
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun main() = PulseEngine.run(AudioExample::class)

class AudioExample : PulseEngineGame()
{
    private var angle = 0f
    private var loopingSource = -1

    override fun onCreate()
    {
        // Get all available device names
        engine.audio.getOutputDevices().forEach { Logger.info { "Sound device: $it" } }

        // Get name of default output device
        val defaultOutputDevice = engine.audio.getDefaultOutputDevice()
        Logger.info { "Default output device: $defaultOutputDevice" }

        // Set output device
        engine.audio.setOutputDevice(defaultOutputDevice)

        // Load sound assets
        engine.asset.load(Sound("examples/assets/hollow.ogg", "hollow"))
        engine.asset.load(Sound("examples/assets/sound_asset.ogg", "heart_beat"))
    }

    override fun onUpdate()
    {
        if (loopingSource == -1)
        {
            // Get sound asset
            val heartBeat = engine.asset.getOrNull<Sound>("heart_beat") ?: throw RuntimeException("Missing asset")

            // Create new looping source
            loopingSource = engine.audio.createSource(heartBeat, volume = 2f, looping = true)

            // Play looping source
            engine.audio.playSource(loopingSource)
        }

        // Create new sound source
        if (engine.input.wasClicked(MouseButton.LEFT))
        {
            val soundAsset = engine.asset.getOrNull<Sound>("hollow") ?: throw RuntimeException("Missing asset")
            val sourceId = engine.audio.createSource(soundAsset)
            engine.audio.setSourcePitch(sourceId, 1f)
            engine.audio.setSourceVolume(sourceId, 0.8f)
            engine.audio.setSourceLooping(sourceId, false)
            engine.audio.setSourcePosition(sourceId, 0f, 0f)
            engine.audio.playSource(sourceId)
        }

        // Play sound asset directly by name
        if (engine.input.wasClicked(MouseButton.RIGHT))
            engine.audio.playSound("hollow")

        // Play looping sound
        if (engine.input.wasClicked(Key.S))
            engine.audio.playSource(loopingSource)

        // Pause looping sound
        if (engine.input.wasClicked(Key.P))
            engine.audio.pauseSource(loopingSource)

        // Stop all audio sources
        if (engine.input.wasClicked(Key.BACKSPACE))
            engine.audio.stopAllSources()

        // Loop through sources and update position
        engine.audio.getSources().forEach { sourceId ->
            engine.audio.setSourcePosition(sourceId, x = cos(angle) * 10f, y = sin(angle) * 10f)
        }
    }

    override fun onFixedUpdate()
    {
        // Update angle
        angle += 0.5f * engine.data.fixedDeltaTime
        if (angle > PI * 2)
            angle = 0f
    }

    override fun onRender()
    {
        // Render origin position of sound
        val xCenter = engine.window.width / 2f
        val yCenter = engine.window.height / 2f
        engine.gfx.mainSurface.setDrawColor(Color.WHITE)
        engine.gfx.mainSurface.drawQuad(xCenter + cos(angle) * xCenter, yCenter, 10f, 10f)

        // Render number of sources
        engine.gfx.mainSurface.drawText("Active sound sources:  ${engine.audio.getSources().size}", 20f, 30f)
    }

    override fun onDestroy() { }
}