package org.laolittle.plugin

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info
import org.laolittle.plugin.command.Pat

object PatPat : KotlinPlugin(
    JvmPluginDescription(
        id = "org.laolittle.plugin.PatPat",
        name = "PatPat",
        version = "1.0",
    )
) {
    override fun onEnable() {
        logger.info { "摸头插件已加载" }
        Pat.register()
    }

    override fun onDisable() {
        logger.info { "摸头插件已卸载" }
        Pat.unregister()
    }
}