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
import no.njoh.pulseengine.modules.lighting.shared.NormalMapRenderer.Orientation.*
import no.njoh.pulseengine.modules.scene.entities.Backdrop
import no.njoh.pulseengine.modules.scene.entities.Camera
import no.njoh.pulseengine.modules.scene.entities.Lamp
import no.njoh.pulseengine.modules.scene.entities.Wall
import no.njoh.pulseengine.modules.scene.systems.EntityRendererImpl
import no.njoh.pulseengine.modules.scene.systems.EntityUpdater
import kotlin.math.cos
import kotlin.math.sin

fun main() = PulseEngine.run<DirectLightingExample>()

class DirectLightingExample : PulseEngineGame()
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

        // Add systems to update and render entities
        engine.scene.addSystem(EntityUpdater())
        engine.scene.addSystem(EntityRendererImpl())

        // Create a backdrop for the lights to shine on
        val backdrop = Backdrop()
        backdrop.z = 1f
        backdrop.width = 512f * 5f
        backdrop.height = 512f * 5f
        backdrop.xTiling = 5f
        backdrop.yTiling = 5f
        backdrop.baseTexture = "cobblestone_albedo"
        backdrop.normalMapTexture = "cobblestone_normal"
        backdrop.normalMapOrientation = NORMAL
        backdrop.normalMapIntensity = 0.4f
        engine.scene.addEntity(backdrop)

        // Create a wall to cast shadows
        val wall = Wall()
        wall.z = -0.1f
        wall.width = 100f
        wall.height = 100f
        wall.baseTexture = "crate_albedo"
        wall.normalMapTexture = "crate_normal"
        wall.normalMapOrientation = INVERT_Y
        engine.scene.addEntity(wall)

        // Create a radial light source
        val radialLamp = Lamp()
        radialLamp.y = 200f
        radialLamp.z = -0.3f
        radialLamp.lightColor = Color(1f, 0.72f, 0.55f)
        radialLamp.intensity = 3f
        radialLamp.radius = 800f
        radialLamp.size = 50f
        radialLamp.coneAngle = 360f
        radialLamp.spill = 0.95f
        radialLamp.type = RADIAL
        radialLamp.shadowType = SOFT
        engine.scene.addEntity(radialLamp)

        // Create a linear light source
        val linearLamp = Lamp()
        linearLamp.y = -230f
        linearLamp.z = -0.1f
        linearLamp.lightColor = Color(144, 172, 247)
        linearLamp.intensity = 3.7f
        linearLamp.radius = 1000f
        linearLamp.size = 110f
        linearLamp.spill = 0.95f
        linearLamp.type = LINEAR
        linearLamp.shadowType = SOFT
        engine.scene.addEntity(linearLamp)

        // Create a camera to better view the scene
        val camera = Camera()
        camera.viewPortWidth = engine.window.width.toFloat()
        camera.viewPortHeight = engine.window.height.toFloat()
        engine.scene.addEntity(camera)

        // Create a lighting system to render all light sources
        val lightingSystem = DirectLightingSystem()
        lightingSystem.ambientColor = Color(0.05f, 0.05f, 0.07f, 0.95f)
        lightingSystem.dithering = 0.7f
        lightingSystem.textureScale = 1f
        lightingSystem.textureFilter = TextureFilter.LINEAR
        lightingSystem.textureFormat = RGBA16F
        lightingSystem.multisampling = Multisampling.NONE
        lightingSystem.enableFXAA = false
        lightingSystem.useNormalMap = true
        lightingSystem.enableLightSpill = true
        engine.scene.addSystem(lightingSystem)

        // Add Color Grading and bloom effect
        engine.gfx.mainSurface.addPostProcessingEffect(ColorGradingEffect())
        engine.gfx.mainSurface.addPostProcessingEffect(BloomEffect().apply { intensity=1.2f; radius=0f; threshold=1f; })

        // Start the scene
        engine.scene.start()
    }

    override fun onFixedUpdate()
    {
        // Find the lamp entity and update its position
        engine.scene.getFirstEntityOfType<Lamp>()?.apply()
        {
            // Set xy position of the light source
            val mousePressed = engine.input.isPressed(MouseButton.LEFT)
            x = if (mousePressed) engine.input.xWorldMouse else cos(angle) * 200f
            y = if (mousePressed) engine.input.yWorldMouse else sin(angle) * 200f

            // Adjust the depth of the light source with the scroll wheel
            z += engine.input.yScroll * 0.05f

            // Increase the rotation angle
            angle += 0.01f
        }
    }
}