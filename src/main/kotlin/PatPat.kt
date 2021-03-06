package org.laolittle.plugin

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.Permission
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.firstIsInstance
import net.mamoe.mirai.utils.info
import org.laolittle.plugin.command.ClearCache
import org.laolittle.plugin.model.PatPatTool
import org.laolittle.plugin.model.Tools.getUserOrNull
import org.laolittle.plugin.util.AutoClear
import org.laolittle.plugin.util.inActive
import java.util.*

@OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
object PatPat : KotlinPlugin(
    JvmPluginDescription(
        id = "org.laolittle.plugin.PatPat",
        name = "PatPat",
        version = "2.0.1",
    )
) {

    val tmp = dataFolder.resolve("tmp")
    private lateinit var patPermission: Permission
    override fun onEnable() {
        val osName = System.getProperties().getProperty("os.name")
        if (!osName.startsWith("Windows")) {
            logger.info { "检测到当前为${osName}系统，将使用headless模式" }
            System.setProperty("java.awt.headless", "true")
        }
        patPermission = PermissionService.INSTANCE.register(permissionId("pat"), "允许使用摸头功能")
        init()
        logger.info { "摸头插件已加载" }
        globalEventChannel().subscribeAlways<MessageEvent> {
            if (message.content.startsWith("摸") && checkPatPermission(sender)) {
                if (message.contains(Image)) {
                    runCatching {
                        val image = message.firstIsInstance<Image>()
                        PatPatTool.getImagePat(image, 40)
                        subject.sendImage(tmp.resolve("${image.imageId}_pat.gif"))
                    }.onFailure { e -> logger.error(e) }
                    return@subscribeAlways
                }
                val matchResult = Regex("摸(.*)").find(message.content) ?: return@subscribeAlways
                val matchConvert = message.content.replace(Regex("摸[爆摸头]"), "摸")
                if (matchConvert.length == 1) {
                    subject.sendMessage("发送${message.content}+@来摸头哦")
                    return@subscribeAlways
                }
                val targetId =
                    if (matchConvert[1] == '我') null else (Regex("摸(.*)").find(
                        matchConvert
                    ))?.groupValues?.get(1) ?: subject.sendMessage("我不知道你要摸谁")
                        .run { return@subscribeAlways }
                val target =
                    if (targetId == null) sender else subject.getUserOrNull(targetId) ?: subject.sendMessage("我不知道你要摸谁")
                        .run { return@subscribeAlways }
                when (matchResult.groupValues[1][0]) {
                    '爆' -> {
                        val image = tmp.resolve("${target.id}_pat.gif")
                        if (image.exists()) image.delete()
                        PatPatTool.getPat(target, 20)
                        subject.sendImage(image)
                        image.delete()
                    }
                    else -> {
                        PatPatTool.getPat(target, 80)
                        subject.sendImage(tmp.resolve("${target.id}_pat.gif"))
                    }
                }
                if (inActive.add(target)) {
                    val task = AutoClear(target)
                    Timer().schedule(task, Date(System.currentTimeMillis() + 2 * 60 * 1000))
                }
            }
        }
    }

    override fun onDisable() {
        logger.info { "摸头插件已卸载" }
    }

    private fun init() {
        ClearCache.register()
        if (tmp.exists()) tmp.deleteRecursively()
        if (!tmp.exists()) tmp.mkdir()
        logger.info { "缓存已自动清理" }
    }

    private fun checkPatPermission(user: User): Boolean {
        return when(user){
            is Member -> AbstractPermitteeId.ExactMember(user.group.id, user.id)
            else -> AbstractPermitteeId.ExactUser(user.id)
        }.hasPermission(patPermission)
    }
}