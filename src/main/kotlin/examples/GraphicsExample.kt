package examples

import no.njoh.pulseengine.core.PulseEngine
import no.njoh.pulseengine.core.PulseEngineGame
import no.njoh.pulseengine.core.asset.types.Texture
import no.njoh.pulseengine.core.graphics.api.BlendFunction
import no.njoh.pulseengine.core.graphics.api.Multisampling
import no.njoh.pulseengine.core.graphics.postprocessing.effects.VignetteEffect
import no.njoh.pulseengine.core.input.MouseButton
import no.njoh.pulseengine.core.shared.utils.Camera2DController

fun main() = PulseEngine.run(GraphicsExample::class)

class GraphicsExample : PulseEngineGame()
{
    private val cameraController = Camera2DController(MouseButton.LEFT)

    override fun onCreate()
    {
        // Load texture from disk
        engine.asset.loadTexture("examples/assets/textureAsset.png", "texture")

        // Set background color of default surface and add post-processing effects
        engine.gfx.mainSurface
            .setMultisampling(Multisampling.NONE)
            .setBackgroundColor(0.8f, 0.8f, 0.8f, 1f)
            .addPostProcessingEffect(VignetteEffect("vignette", strength = 0.1f))

        // Create a separate surface to use for UI
        engine.gfx
            .createSurface("uiSurface")
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
        val surface = engine.gfx.mainSurface
        val texture = engine.asset.getOrNull("texture") ?: Texture.BLANK

        // Set the draw color to be used for this surface
        surface.setDrawColor(1f, 1f, 1f)

        // Draw an untextured quad with the set color
        surface.drawQuad(300f, 300f, 300f, 300f)

        // Draw the given texture
        surface.drawTexture(texture, 400f, 400f, 100f, 100f)

        // Draw lines
        surface.setDrawColor(1f, 1f, 1f)
        surface.drawLine(200f, 300f, 200f, 600f)
        surface.drawLine(700f, 300f, 700f, 600f)
        surface.drawLine(300f, 200f, 600f, 200f)
        surface.drawLine(300f, 700f, 600f, 700f)

        // Draw text to separate UI surface
        val uiSurface = engine.gfx.getSurfaceOrDefault("uiSurface")
        uiSurface.setDrawColor(0f, 0f, 0f)
        uiSurface.drawText("UI", x = engine.window.width * 0.5f, y = 40f, xOrigin = 0.5f, fontSize = 64f)

        // Draw within an area
        uiSurface.drawWithin(100f, 100f, 200f, 200f)
        {
            uiSurface.setDrawColor(1f, 0f, 0f)
            uiSurface.drawQuad(engine.input.xMouse - 10f, engine.input.yMouse - 10f, 20f, 20f)
        }
    }

    override fun onDestroy() { }
}