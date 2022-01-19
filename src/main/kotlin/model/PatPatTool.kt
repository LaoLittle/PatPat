package org.laolittle.plugin.model

import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import org.laolittle.plugin.PatPat
import org.laolittle.plugin.PatPat.tmp
import org.laolittle.plugin.util.GifEncoder
import java.awt.AlphaComposite
import java.awt.Color
import java.awt.RenderingHints
import java.awt.geom.RoundRectangle2D
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO
import java.awt.Image as AwtImage

@Suppress("SameParameterValue")
object PatPatTool {
    @OptIn(ExperimentalCommandDescriptors::class, ConsoleExperimentalApi::class)
    fun getPat(hippopotomonstrosesquippedaliophobia: User, delay: Int) {
        val qqId = hippopotomonstrosesquippedaliophobia.id
        // hippopotomonstrosesquippedaliophobia: 长单词恐惧症

        if (!tmp.exists()) tmp.mkdir()
        if (tmp.resolve("${qqId}_pat.gif").exists()) return
        val avatar = URL(hippopotomonstrosesquippedaliophobia.avatarUrl)
        mkImg(avatar, tmp.resolve("${qqId}_pat.gif"), delay)
    }

    suspend fun getImagePat(image: Image, delay: Int) {
        val imageFromServer = URL(image.queryUrl())
        mkImg(imageFromServer, tmp.resolve("${image.imageId}_pat.gif"), delay)
    }

    private fun mkImg(image: URL, savePath: File, delay: Int) {
        val avatarImage = ImageIO.read(image)
        val targetSize = avatarImage.width
        val roundImage = BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB)
        roundImage.createGraphics().apply {
            composite = AlphaComposite.Src
            setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            color = Color.WHITE
            fill(
                RoundRectangle2D.Float(
                    0F, 0F, targetSize.toFloat(), targetSize.toFloat(),
                    targetSize.toFloat(), targetSize.toFloat()
                )
            )
            composite = AlphaComposite.SrcAtop
            drawImage(avatarImage, 0, 0, targetSize, targetSize, null)
            dispose()
        }
        roundImage.getScaledInstance(112, 112, BufferedImage.SCALE_SMOOTH).apply {
            val p1 = processImage(this, 0, 100, 100, 12, 16, 0)
            val p2 = processImage(this, 1, 105, 88, 12, 28, 0)
            val p3 = processImage(this, 2, 110, 76, 12, 40, 6)
            val p4 = processImage(this, 3, 107, 84, 12, 32, 0)
            val p5 = processImage(this, 4, 100, 100, 12, 16, 0)
            val images: Array<BufferedImage> = arrayOf(p1, p2, p3, p4, p5)
            GifEncoder.convert(images, "$savePath", delay)
        }
    }

    //w: 宽 h: 高 x,y: 头像位置 hy:手的y轴偏移
    @OptIn(ExperimentalCommandDescriptors::class, ConsoleExperimentalApi::class)
    private fun processImage(
        image: AwtImage,
        i: Int,
        w: Int,
        h: Int,
        x: Int,
        y: Int,
        hy: Int
    ): BufferedImage {
        val handImage = ImageIO.read(PatPat.javaClass.getResourceAsStream("/data/PatPat/img${i}.png"))
        val processingImage = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
        val processedImage = BufferedImage(112, 112, BufferedImage.TYPE_INT_RGB)
        processingImage.createGraphics().apply {
            drawImage(image, 0, 0, w, h, null)
            dispose()
        }
        processedImage.createGraphics().apply {
            color = Color.WHITE
            fillRect(0, 0, 112, 112)
            drawImage(processingImage, x, y, null)
            drawImage(handImage, 0, hy, null)
            dispose()
        }
        return processedImage
    }

/*
@ConsoleExperimentalApi
@ExperimentalCommandDescriptors
private fun selfRead(i: Int): InputStream {
    val path = PatPat.javaClass.protectionDomain.codeSource.location.path
    val jarPath = JarFile(path)
    val entry = jarPath.getJarEntry("data/PatPat/img${i}.png")
    return jarPath.getInputStream(entry)
}
 */
}