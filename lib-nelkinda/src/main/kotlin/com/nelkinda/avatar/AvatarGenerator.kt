package com.nelkinda.avatar

import java.awt.Color
import java.awt.Font
import java.awt.Font.BOLD
import java.awt.Font.SANS_SERIF
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

/** Generates an Avatar, that is, an image with a two-letter monogram. */
class AvatarGenerator {
    fun createAvatar(text: String): ByteArray {
        val hue = (text[0].uppercaseChar().code % 26 * 26 + text[1].uppercaseChar().code % 26) / (26 * 26).toFloat()
        val avatarImage = BufferedImage(WIDTH, HEIGHT, TYPE_INT_ARGB)
        with(avatarImage.createGraphics()) {
            color = Color.getHSBColor(hue, 0.1f, 0.95f)
            fillRect(0, 0, WIDTH, HEIGHT)
            font = Companion.font
            val box = font.getStringBounds(text, fontRenderContext)
            val ascent = font.getLineMetrics(text, fontRenderContext).ascent
            color = Color.getHSBColor(hue, 1.0f, 0.5f)
            drawString(text, ((WIDTH - box.width) / 2).toFloat(), ((HEIGHT - box.height) / 2 + ascent).toFloat())
        }
        val pngBytes = ByteArrayOutputStream()
        ImageIO.write(avatarImage, "png", pngBytes)
        return pngBytes.toByteArray()
    }

    companion object {
        private const val WIDTH = 400
        private const val HEIGHT = 400
        private val font = Font(SANS_SERIF, BOLD, HEIGHT / 2)
    }
}
