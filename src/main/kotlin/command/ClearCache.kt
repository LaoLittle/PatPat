package org.laolittle.plugin.command

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
import org.laolittle.plugin.PatPat
import org.laolittle.plugin.PatPat.dataFolder
import java.io.File

object ClearCache : SimpleCommand(
    PatPat, "clearcache", "cc",
    description = "清理缓存"
){
    @Handler
    suspend fun CommandSender.handle(){
        val tmp = File("$dataFolder/tmp")
        var flag = false
        if(tmp.exists()) flag = tmp.deleteRecursively()
        if(flag) {
            if(user != null) sendMessage(At(user!!) + PlainText("缓存清理完毕"))
            else sendMessage(PlainText("缓存清理完毕"))
        }
        else sendMessage(PlainText("缓存清理失败！"))
    }
}