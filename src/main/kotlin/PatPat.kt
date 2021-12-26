package org.laolittle.plugin

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.utils.info
import org.laolittle.plugin.command.ClearCache
import org.laolittle.plugin.model.PatPatTool
import org.laolittle.plugin.model.Tools.getUserOrNull
import java.io.File

@OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
object PatPat : KotlinPlugin(
    JvmPluginDescription(
        id = "org.laolittle.plugin.PatPat",
        name = "PatPat",
        version = "2.0",
    )
) {
    override fun onEnable() {
        val osName = System.getProperties().getProperty("os.name")
        if (!osName.startsWith("Windows")) {
            logger.info { "检测到当前为${osName}系统，将使用headless模式" }
            System.setProperty("java.awt.headless", "true")
        }
        init()
        logger.info { "摸头插件已加载" }
        GlobalEventChannel.subscribeAlways<MessageEvent> {
            val matchResult = Regex("摸(.*)").find(message.content) ?: return@subscribeAlways
            val targetId =
                if (matchResult.groupValues[1][0] == '我') null else (Regex("[${matchResult.groupValues[1][0]}|@](.*)").find(
                    message.content
                ) ?: Regex("""\d""").find(message.content))?.groupValues?.get(0) ?: subject.sendMessage("我不知道你要摸谁")
                    .run { return@subscribeAlways }
            val target =
                if (targetId == null) sender else subject.getUserOrNull(targetId) ?: subject.sendMessage("我不知道你要摸谁")
                    .run { return@subscribeAlways }
            when (matchResult.groupValues[1][0]) {
                '爆' -> {
                    val image = File("${PatPat.dataFolder}/tmp").resolve("${target.id}_pat.gif")
                    if (image.exists()) image.delete()
                    PatPatTool.getPat(target, 20)
                    subject.sendImage(image)
                    image.delete()
                }
                else -> {
                    PatPatTool.getPat(target, 80)
                    subject.sendImage(File("$dataFolder/tmp").resolve("${target.id}_pat.gif"))
                }
            }
        }
    }

    override fun onDisable() {
        logger.info { "摸头插件已卸载" }
    }

    private fun init() {
        ClearCache.register()
        val tmp = File("$dataFolder/tmp")
        if (tmp.exists()) tmp.deleteRecursively()
        logger.info { "缓存已自动清理" }
    }
}