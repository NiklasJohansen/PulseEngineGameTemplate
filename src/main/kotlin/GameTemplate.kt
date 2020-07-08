import no.njoh.pulseengine.PulseEngine
import no.njoh.pulseengine.modules.PulseEngineGame

fun main() = PulseEngine.run(GameTemplate::class)

class GameTemplate : PulseEngineGame()
{
    override fun onCreate()
    {

    }

    override fun onUpdate()
    {

    }

    override fun onRender()
    {
        engine.gfx.mainSurface.drawText(
            text = "Pulse Engine Game Template",
            x = engine.window.width / 2f,
            y = engine.window.height / 2f,
            fontSize = 72f,
            xOrigin = 0.5f
        )
    }

    override fun onDestroy()
    {

    }
}