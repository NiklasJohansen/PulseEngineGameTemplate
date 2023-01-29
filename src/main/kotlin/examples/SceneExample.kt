package examples

import no.njoh.pulseengine.core.PulseEngine
import no.njoh.pulseengine.core.PulseEngineGame
import no.njoh.pulseengine.core.asset.types.Texture
import no.njoh.pulseengine.core.graphics.Surface2D
import no.njoh.pulseengine.core.input.Key
import no.njoh.pulseengine.core.scene.SceneEntity
import no.njoh.pulseengine.core.scene.SceneState.*
import no.njoh.pulseengine.core.shared.annotations.AssetRef
import no.njoh.pulseengine.core.shared.primitives.Color
import no.njoh.pulseengine.modules.scene.entities.Camera
import no.njoh.pulseengine.modules.scene.entities.StandardSceneEntity
import no.njoh.pulseengine.widgets.editor.SceneEditor


fun main() = PulseEngine.run(SceneExample::class)

class SceneExample : PulseEngineGame()
{
    override fun onCreate()
    {
        // Adds the SceneEditor widget (default open key: F2)
        engine.widget.add(SceneEditor())

        // Loads a scene asset to use from the editor
        engine.asset.loadTexture("examples/assets/textureAsset.png", "textureAsset")

        // Checks if a scene file already exist
        if (engine.data.exists("example.scn"))
        {
            // Loads the scene and sets it as the currently active scene
            engine.scene.loadAndSetActive("example.scn")
        }
        else
        {
            // If no scene was found, create a new one and set it active
            engine.scene.createEmptyAndSetActive("example.scn")

            // Creates a new scene entity
            val entity = ExampleEntity()
            entity.x = 100f
            entity.y = engine.window.height / 2f
            entity.width = 100f
            entity.height = 100f

            // Creates a camera entity (provided by engine)
            val camera = Camera()
            camera.x = engine.window.width / 2f
            camera.y = engine.window.height / 2f
            camera.viewPortWidth = 1200f
            camera.viewPortHeight = 800f

            // Adds the entities to the scene
            engine.scene.addEntity(entity)
            engine.scene.addEntity(camera)
        }

        // Saves the active scene to disk
        engine.scene.save()

        // Starts the active scene
        engine.scene.start()
    }

    override fun onUpdate()
    {
        if (engine.input.wasClicked(Key.SPACE))
        {
            // Change scene state when SPACE key is pressed
            when (engine.scene.state)
            {
                RUNNING ->
                {
                    // Pause scene if it is running
                    engine.scene.pause()
                }
                PAUSED ->
                {
                    // Stop and reload scene from file
                    engine.scene.stop()
                    engine.scene.reload()
                }
                STOPPED ->
                {
                    // Save scene to file and start it
                    engine.scene.save()
                    engine.scene.start()
                }
            }
        }
    }

    override fun onRender()
    {
        // Render scene state text
        engine.gfx.mainSurface.setDrawColor(1f, 1f, 1f)
        engine.gfx.mainSurface.drawText(
            text = "${engine.scene.state}",
            x = 10f,
            y = 20f,
            fontSize = 48f
        )
    }

    override fun onDestroy()
    {
        // Save the active scene if it is not running
        if (engine.scene.state == STOPPED)
            engine.scene.save()
    }
}

/**
 * All scene entities inherits from the [SceneEntity] class
 */
class ExampleEntity : StandardSceneEntity()
{
    // Public mutable properties will be adjustable from the SceneEditor UI

    @AssetRef(Texture::class)
    var textureName = "textureAsset"

    var color = Color(1f, 1f, 1f)

    var speed = 1f

    override fun onFixedUpdate(engine: PulseEngine)
    {
        x += speed
    }

    override fun onRender(engine: PulseEngine, surface: Surface2D)
    {
        // Gets the texture asses or uses the blank texture if it's not found
        val texture = engine.asset.getOrNull(textureName) ?: Texture.BLANK

        // Sets the color and draws the texture
        surface.setDrawColor(color)
        surface.drawTexture(texture, x, y, width, height, rotation, 0.5f, 0.5f)
    }
}