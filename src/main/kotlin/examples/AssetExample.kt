package examples

import no.njoh.pulseengine.PulseEngine
import no.njoh.pulseengine.data.*
import no.njoh.pulseengine.modules.PulseEngineGame

fun main() = PulseEngine.run(AssetExample::class)

class AssetExample : PulseEngineGame()
{
    private var frame = 0

    override fun onCreate()
    {
        // Set tick rate
        engine.config.fixedTickRate = 1

        // Load plane text
        engine.asset.loadText("/examples/assets/textAsset.txt", "textAsset")

        // Load texture
        engine.asset.loadTexture("/examples/assets/textureAsset.png", "textureAsset")

        // Load sprite sheet and define cell count
        engine.asset.loadSpriteSheet("/examples/assets/spriteSheetAsset.png", "spriteSheetAsset", 6, 1)

        // Load sound
        engine.asset.loadSound("/examples/assets/soundAsset.ogg", "soundAsset")

        // Load font and define available font sizes
        engine.asset.loadFont("/examples/assets/fontAsset.ttf", "fontAsset", floatArrayOf(72f))
    }

    override fun onUpdate() { }

    override fun onFixedUpdate()
    {
        // Get loaded sound asset
        val soundAsset = engine.asset.get<Sound>("soundAsset")

        // Create sound source and play it
        val sourceId = engine.audio.createSource(soundAsset)
        engine.audio.play(sourceId)

        // Increase frame counter
        frame = (frame + 1) % 6
    }

    override fun onRender()
    {
        // Get loaded assets
        val text = engine.asset.get<Text>("textAsset")
        val font = engine.asset.get<Font>("fontAsset")
        val texture = engine.asset.get<Texture>("textureAsset")
        val spriteSheet = engine.asset.get<SpriteSheet>("spriteSheetAsset")

        // Draw text with given font
        engine.gfx.mainSurface.drawText(text.text, 200f, 160f, font)

        // Draw texture
        engine.gfx.mainSurface.drawTexture(texture, 10f, 20f, 250f, 250f)

        // Draw texture from sprite sheet
        engine.gfx.mainSurface.drawTexture(spriteSheet.getTexture(frame), 880f, 110f, 60f, 60f)
    }

    override fun onDestroy() { }
}