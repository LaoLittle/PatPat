package org.laolittle.plugin.command

import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.command.getGroupOrNull
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.User
import org.laolittle.plugin.PatPat
import org.laolittle.plugin.PatPat.dataFolder
import org.laolittle.plugin.model.getPat
import java.io.File

@ConsoleExperimentalApi
@ExperimentalCommandDescriptors
object Pat : SimpleCommand(
    PatPat, "摸", "摸摸", "摸头", "patpat", "pat",
    description = "摸摸头"
){

    override val prefixOptional: Boolean = true //命令可免去斜杠

    @Handler
    suspend fun CommandSenderOnMessage<*>.handle(target: User){
        val qqid = target.id
        getPat(qqid, 80)
        getGroupOrNull()?.sendImage(File("$dataFolder/tmp").resolve("${qqid}_pat.gif"))
    }
}