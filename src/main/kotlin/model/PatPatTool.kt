package org.laolittle.plugin.model

import org.laolittle.plugin.PatPat.dataFolder
import util.GifEncoder
import java.awt.AlphaComposite
import java.awt.Color
import java.awt.RenderingHints.KEY_ANTIALIASING
import java.awt.RenderingHints.VALUE_ANTIALIAS_ON
import java.awt.geom.RoundRectangle2D
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import javax.imageio.ImageIO


//获取头像并暂存在本地的tmp内
    fun getavatar(qqid: Long) {
        val tmp = File("$dataFolder/tmp")
        if(!tmp.exists()) tmp.mkdir()
        val avatarurl = "http://q1.qlogo.cn/g?b=qq&nk=$qqid&s=640"
        var connection : HttpURLConnection? = null
        try {
            connection = URL(avatarurl).openConnection() as HttpURLConnection //建立链接

            connection.connect() //打开输入流

            connection.inputStream.use { input ->
                BufferedOutputStream(FileOutputStream(tmp.resolve("${qqid}_avatar.jpg"))).use { output ->
                    input.copyTo(output)  //将文件复制到本地
                }
            }
        }catch (e : Exception){
            e.printStackTrace()
        }finally {
            connection?.disconnect()
        }
    mkimg(tmp.resolve("${qqid}_avatar.jpg"), tmp.resolve("${qqid}_pat.gif"), qqid)
        return
    }


    @Throws(IOException::class)
    fun mkimg(filePath: File?, savePath: File?, qqid: Long) {
        val bufferedImage = ImageIO.read(filePath)
        val circularBufferImage = roundImage(bufferedImage, 112, 112)
        //ImageIO.write(circularBufferImage, "png", savePath)
        // y:头像y轴偏移量   hy:手的y轴偏移量　w:横向挤压 标准为112 h:纵向挤压 标准为112
        val p1 = processImage(circularBufferImage, qqid, 0, 90, 90, 5, 0)
        val p2 = processImage(circularBufferImage, qqid, 1, 92, 85, 28, 3)
        val p3 = processImage(circularBufferImage, qqid, 2, 95, 79, 36, 6)
        val p4 = processImage(circularBufferImage, qqid, 3, 97, 74, 29, 9)
        val p5 = processImage(circularBufferImage, qqid, 4, 100, 68, 37, 12)
        val images: Array<String?> = arrayOf(p1, p2, p3, p4, p5, p4, p3, p2)
        GifEncoder.convert(images, "$savePath", 60, true)
        //toGif(p1, p2, p3, p4, p5, savePath)
        return
    }



fun processImage(image: BufferedImage, qqid: Long, i: Int, w: Int, h: Int, y: Int, hy: Int): String {
    val tmp = File("$dataFolder/tmp")
    val handImage = ImageIO.read(dataFolder.resolve("img${i}.png"))
    val processingImage = resizeImage(image, w, h)
    val processedImage = compoundImage(handImage, processingImage, y, hy)
    ImageIO.write(processedImage, "jpg", tmp.resolve("${qqid}_g${i}.jpg"))
    return "$dataFolder/tmp/${qqid}_g${i}.jpg"
    //return compoundImage(handImage, processingImage, y)
}

    //将图片绘制到圆形图片上，并改变头像尺寸
    private fun roundImage(image: BufferedImage, targetSize: Int, cornerRadius: Int): BufferedImage {
        val outputImage = BufferedImage(targetSize, targetSize, TYPE_INT_ARGB)
        val g2 = outputImage.createGraphics()
        g2.composite = AlphaComposite.Src
        g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON)
        g2.color = Color.WHITE
        g2.fill(
            RoundRectangle2D.Float(
                0F, 0F, targetSize.toFloat(), targetSize.toFloat(),
                cornerRadius.toFloat(), cornerRadius.toFloat()
            )
        )
        g2.composite = AlphaComposite.SrcAtop
        g2.drawImage(image, 0, 0, targetSize, targetSize, null)
        g2.dispose()
        return outputImage
    }

    fun resizeImage(roundImage: BufferedImage, targetWidth: Int, targetHeight: Int): BufferedImage {
        val resizedImage = BufferedImage(targetWidth, targetHeight, TYPE_INT_ARGB)
        val g2 = resizedImage.createGraphics()
        g2.drawImage(roundImage, 0, 0, targetWidth, targetHeight, null)
        g2.dispose()
        return resizedImage
    }

    //合成图片 手手和头像
    fun compoundImage(handImage: BufferedImage, avatarImage: BufferedImage, y: Int, hy: Int): BufferedImage {
        val backImage = BufferedImage(112, 112, TYPE_INT_RGB)
        val g2 = backImage.createGraphics()
        g2.color = Color.WHITE
        g2.fillRect(0, 0, 112, 112)
        g2.drawImage(avatarImage, 0, y,  null) //绘制头像
        g2.drawImage(handImage, 0, hy, null) //绘制手
        g2.dispose()
        return backImage
    }