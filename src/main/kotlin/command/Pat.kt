package org.laolittle.plugin.command

import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.getGroupOrNull
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.User
import org.laolittle.plugin.PatPat
import org.laolittle.plugin.PatPat.dataFolder
import org.laolittle.plugin.model.getavatar
import java.io.File

object Pat : SimpleCommand(
    PatPat, "pat", "摸", "摸摸", "摸头", "patpat",
    description = "摸摸头"
){

    override val prefixOptional: Boolean = true //命令可免去斜杠

    @Handler
    suspend fun CommandSenderOnMessage<*>.handle(target: User){
        val qqid = target.id
        getavatar(qqid)
        getGroupOrNull()?.sendImage(File("$dataFolder/tmp").resolve("${qqid}_pat.gif"))
    }
}