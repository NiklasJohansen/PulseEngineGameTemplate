package examples

import no.njoh.pulseengine.core.PulseEngine
import no.njoh.pulseengine.core.PulseEngineGame
import no.njoh.pulseengine.core.graphics.surface.Surface
import no.njoh.pulseengine.core.input.*
import no.njoh.pulseengine.core.shared.primitives.Color

fun main() = PulseEngine.run<InputExample>()

class InputExample : PulseEngineGame()
{
    // Create two focus areas
    private val focusAreaOne = FocusArea(200f, 200f, 400f, 400f)
    private val focusAreaTwo = FocusArea(300f, 300f, 500f, 500f)

    override fun onCreate()
    {
        // Create a new surface for UI
        engine.gfx.createSurface("ui_surface")
    }

    override fun onUpdate()
    {
        // Checks if the SPACE key was clicked once
        if (engine.input.wasClicked(Key.SPACE))
            println("SPACE clicked")

        // Checks if the SPACE key was released once
        if (engine.input.wasReleased(Key.SPACE))
            println("SPACE released")

        // Checks if the RIGHT key is down
        if (engine.input.isPressed(Key.RIGHT))
            engine.gfx.mainCamera.position.x += 100f * engine.data.deltaTime

        // Read clipboard
        if (engine.input.isPressed(Key.LEFT_CONTROL) && engine.input.wasClicked(Key.V))
            engine.input.getClipboard { content -> println(content) }

        // Read text input
        if (engine.input.textInput.isNotBlank())
            println("Text input: ${engine.input.textInput}")

        // Read vertical mouse scroll
        if (engine.input.yScroll != 0f)
            println("Scroll: ${engine.input.yScroll}")

        // Set default cursor
        engine.input.setCursorType(CursorType.ARROW)

        // Update focus areas
        requestFocusAndUpdateArea(focusAreaOne)
        requestFocusAndUpdateArea(focusAreaTwo)
    }

    private fun requestFocusAndUpdateArea(focusArea: FocusArea)
    {
        // Request focus for this area
        engine.input.requestFocus(focusArea)

        // If the mouse is inside both area one and two, only area two will get focus as it is last to request it
        if (focusArea.isInside(engine.input.xMouse, engine.input.yMouse))
        {
            if (engine.input.isPressed(MouseButton.LEFT))
            {
                // Move area by adding delta value of mouse position
                focusArea.x0 += engine.input.xdMouse
                focusArea.x1 += engine.input.xdMouse
                focusArea.y0 += engine.input.ydMouse
                focusArea.y1 += engine.input.ydMouse

                // Change cursor when mouse is inside and pressed
                engine.input.setCursorType(CursorType.CROSSHAIR)
            }
            else engine.input.setCursorType(CursorType.HAND)
        }
    }

    override fun onRender()
    {
        // Draw rect on the main surface (world space)
        engine.gfx.mainSurface.setDrawColor(0.08f, 0.08f, 0.08f, 1f)
        engine.gfx.mainSurface.drawQuad(500f, 500f, 200f, 200f)
        engine.gfx.mainSurface.setDrawColor(1f, 1f, 1f)
        engine.gfx.mainSurface.drawText("Pos: (500, 500)", 505f, 520f)

        // Get the on-screen mouse position
        val x = engine.input.xMouse
        val y = engine.input.yMouse

        // Get the mouse position relative to the main camera (world space)
        val xw = engine.input.xWorldMouse
        val yw = engine.input.yWorldMouse

        // Draw mouse position text to screen
        val uiSurface = engine.gfx.getSurfaceOrDefault("ui_surface")
        uiSurface.setDrawColor(Color.WHITE)
        uiSurface.drawText("Mouse position on screen: ($x, $y)", 10f, 30f)
        uiSurface.drawText("Mouse position in world:  ($xw, $yw)", 10f, 60f)

        // Gamepad input
        engine.input.gamepads.forEachIndexed { i, gamepad ->
            val xLeft = gamepad.getAxis(GamepadAxis.LEFT_X)
            val yLeft = gamepad.getAxis(GamepadAxis.LEFT_Y)
            uiSurface.drawText("Gamepad (${gamepad.id}) left joystick: ($xLeft, $yLeft)", 10f, 90f + i * 30)
        }

        // Draw focus area one
        val alphaOne = if (engine.input.hasFocus(focusAreaOne)) 0.8f else 0.5f
        uiSurface.setDrawColor(0f, 0.4f, 0.8f, alphaOne)
        focusAreaOne.draw(uiSurface)

        // Draw focus area two
        val alphaTwo = if (engine.input.hasFocus(focusAreaTwo)) 0.8f else 0.5f
        uiSurface.setDrawColor(0f, 0.8f, 0.4f, alphaTwo)
        focusAreaTwo.draw(uiSurface)
    }

    private fun FocusArea.draw(surface: Surface) = surface.drawQuad(x0, y0, x1 - x0, y1 - y0)
}