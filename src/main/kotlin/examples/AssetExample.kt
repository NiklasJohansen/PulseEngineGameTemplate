package examples

import no.njoh.pulseengine.core.PulseEngine
import no.njoh.pulseengine.core.PulseEngineGame
import no.njoh.pulseengine.core.asset.types.*

fun main() = PulseEngine.run<AssetExample>()

class AssetExample : PulseEngineGame()
{
    private var frameIndex = 0

    override fun onCreate()
    {
        // Load plane text
        engine.asset.load(Text(filePath = "examples/assets/text_asset.txt", name = "text"))

        // Load texture
        engine.asset.load(Texture(filePath = "examples/assets/texture_asset.png", name = "texture"))

        // Load all assets in the folder
        engine.asset.loadAll(directory = "examples/assets/")

        // Load the sprite sheet and define the cell count
        engine.asset.load(SpriteSheet(
            filePath = "examples/assets/sprite_sheet_asset.png",
            name = "sprite_sheet",
            horizontalCells = 6,
            verticalCells = 1
        ))

        // Load sound
        engine.asset.load(Sound(filePath = "examples/assets/sound_asset.ogg", name = "sound"))

        // Load font
        engine.asset.load(Font(filePath = "examples/assets/font_asset.ttf", name = "font"))

        // Set tick rate to 1 for this example
        engine.config.fixedTickRate = 1f
    }

    override fun onFixedUpdate()
    {
        // Get loaded sound asset
        val soundAsset = engine.asset.getOrNull<Sound>("sound")

        // Create the sound source and play it if the asset was found
        if (soundAsset != null)
            engine.audio.playSound(soundAsset)

        // Increase the current frame index
        val frameCount = engine.asset.getOrNull<SpriteSheet>("sprite_sheet")?.size ?: 1
        frameIndex = (frameIndex + 1) % frameCount
    }

    override fun onRender()
    {
        // Get loaded assets
        val text = engine.asset.getOrNull<Text>("text") ?: return
        val font = engine.asset.getOrNull<Font>("font") ?: return
        val texture = engine.asset.getOrNull<Texture>("texture") ?: return
        val spriteSheet = engine.asset.getOrNull<SpriteSheet>("sprite_sheet") ?: return

        // Draw text with the given font
        engine.gfx.mainSurface.drawText(text.text, 200f, 130f, font, 72f)

        // Draw texture
        engine.gfx.mainSurface.drawTexture(texture, 10f, 20f, 250f, 250f)

        // Draw texture from the sprite sheet
        engine.gfx.mainSurface.drawTexture(spriteSheet.getTexture(frameIndex), 880f, 110f, 60f, 60f)
    }
}