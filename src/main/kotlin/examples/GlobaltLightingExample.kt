package examples

import no.njoh.pulseengine.core.PulseEngine
import no.njoh.pulseengine.core.PulseEngineGame
import no.njoh.pulseengine.core.asset.types.Texture
import no.njoh.pulseengine.core.graphics.api.Multisampling
import no.njoh.pulseengine.core.graphics.api.TextureFilter
import no.njoh.pulseengine.core.graphics.api.TextureFormat.*
import no.njoh.pulseengine.core.graphics.postprocessing.effects.BloomEffect
import no.njoh.pulseengine.core.graphics.postprocessing.effects.ColorGradingEffect
import no.njoh.pulseengine.core.input.MouseButton
import no.njoh.pulseengine.core.shared.primitives.Color
import no.njoh.pulseengine.modules.lighting.direct.DirectLightType.*
import no.njoh.pulseengine.modules.lighting.direct.DirectLightingSystem
import no.njoh.pulseengine.modules.lighting.direct.DirectShadowType.*
import no.njoh.pulseengine.modules.lighting.global.GlobalIlluminationSystem
import no.njoh.pulseengine.modules.lighting.shared.NormalMapRenderer.Orientation.*
import no.njoh.pulseengine.modules.scene.entities.Backdrop
import no.njoh.pulseengine.modules.scene.entities.Camera
import no.njoh.pulseengine.modules.scene.entities.Lamp
import no.njoh.pulseengine.modules.scene.entities.Wall
import kotlin.math.cos
import kotlin.math.sin

fun main() = PulseEngine.run(GlobalLightingExample::class)

class GlobalLightingExample : PulseEngineGame()
{
    // Angle used for light positioning
    private var angle = 0f

    override fun onCreate()
    {
        // Create an empty scene
        engine.scene.createEmptyAndSetActive("lighting.scn")

        // Load textures
        engine.asset.load(Texture("/examples/assets/cobblestone_albedo.png", "cobblestone_albedo"))
        engine.asset.load(Texture("/examples/assets/cobblestone_normal.png", "cobblestone_normal", format = RGBA8))
        engine.asset.load(Texture("/examples/assets/crate_albedo.png", "crate_albedo"))
        engine.asset.load(Texture("/examples/assets/crate_normal.png", "crate_normal", format = RGBA8))

        // Create a backdrop for the lights to shine on
        val backdrop = Backdrop()
        backdrop.z = 1f
        backdrop.width = 512f * 5f
        backdrop.height = 512f * 5f
        backdrop.xTiling = 5f
        backdrop.yTiling = 5f
        backdrop.textureName = "cobblestone_albedo"
        backdrop.normalMapName = "cobblestone_normal"
        backdrop.normalMapOrientation = NORMAL
        engine.scene.addEntity(backdrop)

        // Create a wall to cast shadows
        val wall = Wall()
        wall.z = -0.1f
        wall.width = 100f
        wall.height = 100f
        wall.textureName = "crate_albedo"
        wall.normalMapName = "crate_normal"
        wall.normalMapOrientation = INVERT_Y
        engine.scene.addEntity(wall)

        // Create a radial light source
        val movingLight = Lamp()
        movingLight.y = 200f
        movingLight.width = 25f
        movingLight.height = 25f
        movingLight.lightColor = Color(1f, 0.72f, 0.55f)
        movingLight.intensity = 10f
        movingLight.coneAngle = 360f
        engine.scene.addEntity(movingLight)

        // Create a linear light source
        val staticLight = Lamp()
        staticLight.y = -230f
        staticLight.width = 200f
        staticLight.height = 5f
        staticLight.lightColor = Color(144, 172, 247)
        staticLight.intensity = 6f
        engine.scene.addEntity(staticLight)

        // Create a camera to better view the scene
        val camera = Camera()
        camera.viewPortWidth = engine.window.width.toFloat()
        camera.viewPortHeight = engine.window.height.toFloat()
        engine.scene.addEntity(camera)

        // Create a lighting system to render all light sources (with default settings)
        val lightingSystem = GlobalIlluminationSystem()
        lightingSystem.lightTexScale = 0.8f // 80% of the screen size, decrease to increase performance
        lightingSystem.localSceneTexScale = 0.8f
        lightingSystem.normalMapScale = 5f
        engine.scene.addSystem(lightingSystem)

        // Add Color Grading and bloom effect
        engine.gfx.mainSurface.addPostProcessingEffect(ColorGradingEffect())
        engine.gfx.mainSurface.addPostProcessingEffect(BloomEffect().apply { intensity=1.2f; radius=0f; threshold=2f; })

        // Start the scene
        engine.scene.start()
    }

    override fun onFixedUpdate()
    {
        // Find the lamp entity and update its position
        engine.scene.getFirstEntityOfType<Lamp>()?.apply()
        {
            // Set xy position of light source
            val mousePressed = engine.input.isPressed(MouseButton.LEFT)
            x = if (mousePressed) engine.input.xWorldMouse else cos(angle) * 200f
            y = if (mousePressed) engine.input.yWorldMouse else sin(angle) * 200f

            // Increase rotation angle
            angle += 0.01f
        }
    }

    override fun onUpdate() { }

    override fun onRender() { }

    override fun onDestroy() { }
}