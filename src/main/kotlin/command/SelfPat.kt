package org.laolittle.plugin.command

import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.command.getGroupOrNull
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import org.laolittle.plugin.PatPat
import org.laolittle.plugin.PatPat.dataFolder
import org.laolittle.plugin.model.getPat
import java.io.File

@ConsoleExperimentalApi
@ExperimentalCommandDescriptors
object SelfPat : SimpleCommand(
    PatPat, "摸我", "自摸", "探头", "selfpat",
    description = "给自己摸摸头"
){

    override val prefixOptional: Boolean = true

    @Handler
    suspend fun CommandSenderOnMessage<*>.handle(){
        val qqid = user!!.id
        getPat(qqid, 80)
        getGroupOrNull()?.sendImage(File("$dataFolder/tmp").resolve("${qqid}_pat.gif"))
    }
}