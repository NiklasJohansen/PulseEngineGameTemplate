import no.njoh.pulseengine.core.PulseEngine
import no.njoh.pulseengine.core.PulseEngineGame
import no.njoh.pulseengine.widgets.cli.CommandLine
import no.njoh.pulseengine.widgets.editor.SceneEditor
import no.njoh.pulseengine.widgets.profiler.Profiler

fun main() = PulseEngine.run(GameTemplate::class)

class GameTemplate : PulseEngineGame()
{
    override fun onCreate()
    {
        engine.widget.add(CommandLine(), Profiler(), SceneEditor())
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