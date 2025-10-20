package examples

import no.njoh.pulseengine.core.PulseEngine
import no.njoh.pulseengine.core.PulseEngineGame
import no.njoh.pulseengine.core.asset.types.Texture
import no.njoh.pulseengine.core.graphics.api.BlendFunction
import no.njoh.pulseengine.core.graphics.api.Multisampling
import no.njoh.pulseengine.core.graphics.postprocessing.effects.ColorGradingEffect
import no.njoh.pulseengine.core.graphics.postprocessing.effects.ColorGradingEffect.ToneMapper.*
import no.njoh.pulseengine.core.input.MouseButton
import no.njoh.pulseengine.core.shared.primitives.Color
import no.njoh.pulseengine.core.shared.utils.Camera2DController

fun main() = PulseEngine.run<GraphicsExample>()

class GraphicsExample : PulseEngineGame()
{
    private val cameraController = Camera2DController(MouseButton.LEFT, smoothing = 0.1f)

    override fun onCreate()
    {
        // Load texture from disk
        engine.asset.load(Texture("examples/assets/texture_asset.png", "texture"))

        // Set background color of default surface and add post-processing effects
        engine.gfx.mainSurface
            .setMultisampling(Multisampling.MSAA8)
            .setBackgroundColor(0.6f, 0.6f, 0.6f, 1f)

        // Post-processing effects
        engine.gfx.mainSurface.addPostProcessingEffect(ColorGradingEffect(
            toneMapper = ACES,
            vignette = 0.05f,
            exposure = 1.2f,
            contrast = 1.5f,
            saturation = 1f
        ))

        // Create a separate surface to use for UI
        engine.gfx
            .createSurface("ui_surface")
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
        surface.setDrawColor(Color.WHITE)

        // Draw an untextured quad with the set color
        surface.drawQuad(300f, 300f, 300f, 300f)

        // Draw the texture
        surface.drawTexture(texture, 400f, 400f, 100f, 100f)

        // Draw lines
        surface.setDrawColor(0.5f, 0.5f, 0.5f)
        surface.drawLine(200f, 300f, 200f, 600f)
        surface.drawLine(700f, 300f, 700f, 600f)
        surface.drawLine(300f, 200f, 600f, 200f)
        surface.drawLine(300f, 700f, 600f, 700f)

        // Draw text to separate UI surface
        val uiSurface = engine.gfx.getSurfaceOrDefault("ui_surface")
        uiSurface.setDrawColor(Color.BLACK)
        uiSurface.drawText("Use mouse to move camera", x = engine.window.width * 0.5f, y = 40f, xOrigin = 0.5f, fontSize = 64f)

        // Draw within an area
        uiSurface.drawWithin(100f, 100f, 200f, 200f)
        {
            uiSurface.setDrawColor(Color.RED)
            uiSurface.drawQuad(engine.input.xMouse - 10f, engine.input.yMouse - 10f, 20f, 20f)
        }
    }
}