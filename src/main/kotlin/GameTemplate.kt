import no.njoh.pulseengine.core.PulseEngine
import no.njoh.pulseengine.core.PulseEngineGame
import no.njoh.pulseengine.core.scene.SceneState.STOPPED
import no.njoh.pulseengine.widgets.cli.CommandLine
import no.njoh.pulseengine.widgets.editor.SceneEditor
import no.njoh.pulseengine.widgets.metrics.GpuMonitor
import no.njoh.pulseengine.widgets.metrics.MetricViewer

fun main() = PulseEngine.run(GameTemplate::class)

class GameTemplate : PulseEngineGame()
{
    override fun onCreate()
    {
        engine.widget.add(CommandLine(), SceneEditor(), MetricViewer(), GpuMonitor())
        engine.console.runScript("init-dev.pes")
        engine.scene.reload() // Load default.scn from disk
        engine.scene.start()
    }

    override fun onFixedUpdate() { }

    override fun onUpdate() { }

    override fun onRender()
    {
        engine.gfx.mainSurface.drawText(
            text = "Pulse Engine Game Template",
            x = engine.window.width / 2f,
            y = engine.window.height / 2f,
            fontSize = 72f,
            xOrigin = 0.5f,
            yOrigin = 0.5f
        )
    }

    override fun onDestroy()
    {
        if (engine.scene.state == STOPPED)
            engine.scene.save() // Save default.scn to disk
    }
}