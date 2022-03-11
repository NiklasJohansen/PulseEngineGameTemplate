package examples

import no.njoh.pulseengine.core.PulseEngine
import no.njoh.pulseengine.core.PulseEngineGame
import no.njoh.pulseengine.core.shared.utils.Logger

fun main() = PulseEngine.run(ConfigExample::class)

class ConfigExample : PulseEngineGame()
{
    override fun onCreate()
    {
        // Loading a configuration file from class path. Engine loads "/application.cfg" on startup.
        engine.config.load("examples/config/exampleConfig.cfg")

        // General config parameters can be set both in config file and directly in game like below
        engine.config.gameName = "ConfigExample"
        engine.config.fixedTickRate = 50
        engine.config.targetFps = 120

        // Domain specific parameters
        engine.window.title = "Config Example"

        // Accessing parameters loaded from config files
        val number = engine.config.getInt("exampleNumber")
        Logger.info("Example number loaded from config file: $number")
    }

    override fun onUpdate() { }

    override fun onRender() { }

    override fun onDestroy() { }
}