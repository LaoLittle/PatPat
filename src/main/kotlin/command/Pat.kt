package org.laolittle.plugin.command

import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.data.content
import org.laolittle.plugin.PatPat
import org.laolittle.plugin.PatPat.dataFolder
import org.laolittle.plugin.model.PatPatTool.getPat
import java.io.File

@ConsoleExperimentalApi
@ExperimentalCommandDescriptors
object Pat : SimpleCommand(
    PatPat, "摸", "摸摸", "摸头", "patpat", "pat",
    description = "摸摸头"
){

    override val prefixOptional: Boolean = true //命令可免去斜杠

    @Handler
    suspend fun CommandSenderOnMessage<*>.handle(target: Member? = null){
        val group = fromEvent.subject
        if (group !is Group){
            group.sendMessage("只有在群聊里才可以摸指定的人哦")
            return
        }
        if (target == null) {
            group.sendMessage("请输入 ${fromEvent.message.content}+@ 来摸一个人")
            return
        }
        getPat(target, 80)
        group.sendImage(File("$dataFolder/tmp").resolve("${target.id}_pat.gif"))
    }
}