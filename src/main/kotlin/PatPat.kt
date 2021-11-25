package org.laolittle.plugin

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.utils.info
import org.laolittle.plugin.command.ClearCache
import org.laolittle.plugin.command.JustPat
import org.laolittle.plugin.command.Pat
import org.laolittle.plugin.command.SelfPat
import java.io.File

@ExperimentalCommandDescriptors
@ConsoleExperimentalApi
object PatPat : KotlinPlugin(
    JvmPluginDescription(
        id = "org.laolittle.plugin.PatPat",
        name = "PatPat",
        version = "1.0.6",
    )
) {
    override fun onEnable() {
        logger.info { "摸头插件已加载" }
        val osName = System.getProperties().getProperty("os.name")
        if(osName.indexOf("Windows") < 0){
            logger.info { "检测到当前为${osName}系统，将使用headless模式" }
            System.setProperty("java.awt.headless", "true")
        }
        Pat.register()
        SelfPat.register()
        ClearCache.register()
        JustPat.register()
        val tmp = File("$dataFolder/tmp")
        if(tmp.exists()) tmp.deleteRecursively()
        logger.info { "缓存已自动清理" }
    }

    override fun onDisable() {
        logger.info { "摸头插件已卸载" }
        Pat.unregister()
        SelfPat.unregister()
        ClearCache.unregister()
        JustPat.unregister()
    }
}