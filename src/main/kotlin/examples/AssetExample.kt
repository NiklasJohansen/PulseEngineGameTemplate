package examples

import no.njoh.pulseengine.core.PulseEngine
import no.njoh.pulseengine.core.PulseEngineGame
import no.njoh.pulseengine.core.asset.types.*

fun main() = PulseEngine.run(AssetExample::class)

class AssetExample : PulseEngineGame()
{
    private var frameIndex = 0

    override fun onCreate()
    {
        // Load plane text
        engine.asset.loadText(fileName = "examples/assets/textAsset.txt", assetName = "text")

        // Load texture
        engine.asset.loadTexture(fileName = "examples/assets/textureAsset.png", assetName = "texture")

        // Load all textures in folder
        engine.asset.loadAllTextures(directory = "examples/assets/")

        // Load sprite sheet and define cell count
        engine.asset.loadSpriteSheet(
            fileName = "examples/assets/spriteSheetAsset.png",
            assetName = "spriteSheet",
            horizontalCells = 6,
            verticalCells = 1
        )

        // Load sound
        engine.asset.loadSound(fileName = "examples/assets/soundAsset.ogg", assetName = "sound")

        // Load font and define available font sizes
        engine.asset.loadFont(fileName = "examples/assets/fontAsset.ttf", assetName = "font")

        // Set tick rate to 1 for this example
        engine.config.fixedTickRate = 1
    }

    override fun onUpdate() { }

    override fun onFixedUpdate()
    {
        // Get loaded sound asset
        val soundAsset = engine.asset.getOrNull<Sound>("sound")

        // Create sound source and play it if the asset was found
        if (soundAsset != null)
            engine.audio.playSound(soundAsset)

        // Increase the current frame index
        val frameCount = engine.asset.getOrNull<SpriteSheet>("spriteSheet")?.size ?: 1
        frameIndex = (frameIndex + 1) % frameCount
    }

    override fun onRender()
    {
        // Get loaded assets
        val text = engine.asset.getOrNull<Text>("text") ?: return
        val font = engine.asset.getOrNull<Font>("font") ?: return
        val texture = engine.asset.getOrNull<Texture>("texture") ?: return
        val spriteSheet = engine.asset.getOrNull<SpriteSheet>("spriteSheet") ?: return

        // Draw text with given font
        engine.gfx.mainSurface.drawText(text.text, 200f, 130f, font, 72f)

        // Draw texture
        engine.gfx.mainSurface.drawTexture(texture, 10f, 20f, 250f, 250f)

        // Draw texture from sprite sheet
        engine.gfx.mainSurface.drawTexture(spriteSheet.getTexture(frameIndex), 880f, 110f, 60f, 60f)
    }

    override fun onDestroy() { }
}