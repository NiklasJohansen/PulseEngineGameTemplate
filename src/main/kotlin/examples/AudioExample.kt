package examples

import no.njoh.pulseengine.core.PulseEngine
import no.njoh.pulseengine.core.PulseEngineGame
import no.njoh.pulseengine.core.asset.types.Sound
import no.njoh.pulseengine.core.input.Key
import no.njoh.pulseengine.core.input.Mouse
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
        engine.audio.getOutputDevices().forEach { Logger.info("Sound device: $it") }

        // Get name of default output device
        val defaultOutputDevice = engine.audio.getDefaultOutputDevice()
        Logger.info("Default output device: $defaultOutputDevice")

        // Set output device
        engine.audio.setOutputDevice(defaultOutputDevice)

        // Load sound assets
        engine.asset.loadSound("examples/assets/hollow.ogg", "hollow")
        engine.asset.loadSound("examples/assets/soundAsset.ogg", "heartBeat")
    }

    override fun onUpdate()
    {
        if (loopingSource == -1)
        {
            // Get sound asset
            val heartBeat = engine.asset.get<Sound>("heartBeat")

            // Create new looping source
            loopingSource = engine.audio.createSource(heartBeat, 2f, true)

            // Play looping source
            engine.audio.play(loopingSource)
        }

        // Create new sound source
        if (engine.input.wasClicked(Mouse.LEFT))
        {
            val soundAsset = engine.asset.get<Sound>("hollow")
            val sourceId = engine.audio.createSource(soundAsset)
            engine.audio.setPitch(sourceId,1f)
            engine.audio.setVolume(sourceId, 0.8f)
            engine.audio.setLooping(sourceId, false)
            engine.audio.setPosition(sourceId, 0f, 0f)
            engine.audio.play(sourceId)
        }

        // Play looping sound
        if (engine.input.wasClicked(Key.S))
            engine.audio.play(loopingSource)

        // Pause looping sound
        if (engine.input.wasClicked(Key.P))
            engine.audio.pause(loopingSource)

        // Stop all audio sources
        if (engine.input.wasClicked(Key.BACKSPACE))
            engine.audio.stopAll()

        // Loop through sources and update position
        engine.audio.getSources().forEach { sourceId ->
            engine.audio.setPosition(sourceId, cos(angle) * 10,sin(angle) * 10)
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
        engine.gfx.mainSurface.setDrawColor(1f,1f, 1f)
        engine.gfx.mainSurface.drawQuad(xCenter + cos(angle) * xCenter, yCenter, 10f, 10f)

        // Render number of sources
        engine.gfx.mainSurface.drawText("Active sound sources:  ${engine.audio.getSources().size}", 20f, 30f)
    }

    override fun onDestroy() { }
}