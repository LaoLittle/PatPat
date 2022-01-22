package org.laolittle.plugin.command

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.message.data.PlainText
import org.laolittle.plugin.PatPat
import org.laolittle.plugin.PatPat.dataFolder

@ConsoleExperimentalApi
@ExperimentalCommandDescriptors
object ClearCache : SimpleCommand(
    PatPat, "clearcache", "cc",
    description = "清理缓存"
) {
    @Handler
    suspend fun CommandSender.handle() {
        val tmp = dataFolder.resolve("tmp")
        if (tmp.deleteRecursively()) sendMessage(PlainText("缓存清理完毕"))
        else sendMessage(PlainText("缓存清理失败！"))
    }
}