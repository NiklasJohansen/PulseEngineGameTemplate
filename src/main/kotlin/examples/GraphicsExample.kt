package examples

import no.njoh.pulseengine.PulseEngine
import no.njoh.pulseengine.data.Mouse
import no.njoh.pulseengine.data.Texture
import no.njoh.pulseengine.modules.PulseEngineGame
import no.njoh.pulseengine.modules.graphics.BlendFunction
import no.njoh.pulseengine.modules.graphics.postprocessing.effects.BloomEffect
import no.njoh.pulseengine.modules.graphics.postprocessing.effects.VignetteEffect
import no.njoh.pulseengine.util.Camera2DController

fun main() = PulseEngine.run(GraphicsExample::class)

class GraphicsExample : PulseEngineGame()
{
    private val cameraController = Camera2DController(Mouse.LEFT)

    override fun onCreate()
    {
        // Load texture from disk
        engine.asset.loadTexture("/examples/assets/textureAsset.png", "texture")

        // Set background color of default surface and add post processing effects
        engine.gfx.mainSurface
            .setBackgroundColor(0.1f, 0.1f, 0.1f)
            .addPostProcessingEffect(BloomEffect(threshold = 0.8f, exposure = 2f))
            .addPostProcessingEffect(VignetteEffect())

        // Create a separate surface to use for UI
        engine.gfx
            .createSurface2D("uiSurface",1)
            .setBackgroundColor(0f, 0f, 0f, 0f)
            .setBlendFunction(BlendFunction.NORMAL)
            .setIsVisible(true)
    }

    override fun onUpdate()
    {
        // Control camera position and zoom based on mouse input
        cameraController.update(engine, engine.gfx.mainCamera)
    }

    override fun onRender()
    {
        val camSurface = engine.gfx.mainSurface
        val texture = engine.asset.get<Texture>("texture")

        // Set the draw color to be used for this surface
        camSurface.setDrawColor(1f, 1f, 0.8f)

        // Draw an untextured quad with the set color
        camSurface.drawQuad(300f, 300f, 300f, 300f)

        // Draw the given texture
        camSurface.drawTexture(texture, 400f, 400f, 100f, 100f)

        // Draw lines
        camSurface.drawLine(200f, 300f, 200f, 600f)
        camSurface.drawLine(700f, 300f, 700f, 600f)
        camSurface.drawLine(300f, 200f, 600f, 200f)
        camSurface.drawLine(300f, 700f, 600f, 700f)

        // Draw text to separate UI surface
        val uiSurface = engine.gfx.getSurface2D("uiSurface")
        uiSurface.setDrawColor(1f, 1f, 0.9f)
        uiSurface.drawText("Stationary HUD text", 10f, 60f, fontSize = 72f)
    }

    override fun onDestroy() { }
}