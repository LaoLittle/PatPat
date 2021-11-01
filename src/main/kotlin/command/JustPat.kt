package org.laolittle.plugin.command

import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.getGroupOrNull
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.User
import org.laolittle.plugin.PatPat
import org.laolittle.plugin.model.getavatar
import java.io.File

object JustPat : SimpleCommand(
    PatPat, "摸爆",
    description = "摸爆"
){

    override val prefixOptional: Boolean = true

    @Handler
    suspend fun CommandSenderOnMessage<*>.handle(target: User){
        val qqid = target.id
        val image = File("${PatPat.dataFolder}/tmp").resolve("${qqid}_pat.gif")
        if(image.exists()) image.delete()
        getavatar(qqid, 20)
        getGroupOrNull()?.sendImage(image)
        image.delete()
    }
}