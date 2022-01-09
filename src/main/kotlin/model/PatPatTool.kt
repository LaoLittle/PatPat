package org.laolittle.plugin.model

import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.User
import org.laolittle.plugin.PatPat
import org.laolittle.plugin.util.GifEncoder
import java.awt.AlphaComposite
import java.awt.Color
import java.awt.RenderingHints
import java.awt.geom.RoundRectangle2D
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO

@Suppress("SameParameterValue")
object PatPatTool {

    @OptIn(ExperimentalCommandDescriptors::class, ConsoleExperimentalApi::class)
    fun getPat(hippopotomonstrosesquippedaliophobia: User, delay: Int) {
        val qqId = hippopotomonstrosesquippedaliophobia.id
        // hippopotomonstrosesquippedaliophobia: 长单词恐惧症
        val tmp = File("${PatPat.dataFolder}/tmp")
        if (!tmp.exists()) tmp.mkdir()
        if (tmp.resolve("${qqId}_pat.gif").exists()) return
        val avatar = URL(hippopotomonstrosesquippedaliophobia.avatarUrl)
        mkImg(avatar, tmp.resolve("${qqId}_pat.gif"), delay)
    }

    private fun mkImg(avatar: URL, savePath: File, delay: Int) {
        val targetSize = 112
        val cornerRadius = 112
        val avatarImage = ImageIO.read(avatar)
        val roundImage = BufferedImage(targetSize, cornerRadius, BufferedImage.TYPE_INT_ARGB)
        val g2 = roundImage.createGraphics()
        g2.composite = AlphaComposite.Src
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = Color.WHITE
        g2.fill(
            RoundRectangle2D.Float(
                0F, 0F, targetSize.toFloat(), targetSize.toFloat(),
                cornerRadius.toFloat(), cornerRadius.toFloat()
            )
        )
        g2.composite = AlphaComposite.SrcAtop
        g2.drawImage(avatarImage, 0, 0, targetSize, cornerRadius, null)
        g2.dispose()
        val p1 = processImage(roundImage, 0, 100, 100, 12, 16, 0)
        val p2 = processImage(roundImage, 1, 105, 88, 12, 28, 0)
        val p3 = processImage(roundImage, 2, 110, 76, 12, 40, 6)
        val p4 = processImage(roundImage, 3, 107, 84, 12, 32, 0)
        val p5 = processImage(roundImage, 4, 100, 100, 12, 16, 0)
        val images: Array<BufferedImage> = arrayOf(p1, p2, p3, p4, p5)
        GifEncoder.convert(images, "$savePath", delay)
    }

    //w: 宽 h: 高 x,y: 头像位置 hy:手的y轴偏移
    @OptIn(ExperimentalCommandDescriptors::class, ConsoleExperimentalApi::class)
    private fun processImage(
        image: BufferedImage,
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
        val g1 = processingImage.createGraphics()
        g1.drawImage(image, 0, 0, w, h, null)
        g1.dispose()
        val g2 = processedImage.createGraphics()
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = Color.WHITE
        g2.fillRect(0, 0, 112, 112)
        g2.drawImage(processingImage, x, y, null)
        g2.drawImage(handImage, 0, hy, null)
        g2.dispose()
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