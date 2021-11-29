package org.laolittle.plugin.command

import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.command.getGroupOrNull
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.User
import org.laolittle.plugin.PatPat
import org.laolittle.plugin.model.getPat
import java.io.File

@ConsoleExperimentalApi
@ExperimentalCommandDescriptors
object JustPat : SimpleCommand(
    PatPat, "摸爆",
    description = "摸爆"
){

    override val prefixOptional: Boolean = true

    @Handler
    suspend fun CommandSenderOnMessage<*>.handle(target: User){
        val image = File("${PatPat.dataFolder}/tmp").resolve("${target.id}_pat.gif")
        if(image.exists()) image.delete()
        getPat(target, 20)
        getGroupOrNull()?.sendImage(image)
        image.delete()
    }
}