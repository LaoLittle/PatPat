package org.laolittle.plugin.util

import net.mamoe.mirai.contact.User
import net.mamoe.mirai.utils.verbose
import org.laolittle.plugin.PatPat
import java.util.*

val inActive = mutableSetOf<User>()

class AutoClear(private val user: User) : TimerTask() {
    override fun run() {
        val cache = PatPat.dataFolder.resolve("tmp").resolve("${user.id}_pat.gif")
        runCatching {
            if (!cache.delete()) PatPat.logger.verbose { "缓存清理失败，原因: 无法找到用户$user 的缓存" }
        }
        inActive.remove(user)
    }
}