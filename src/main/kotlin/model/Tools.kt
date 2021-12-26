package org.laolittle.plugin.model

import net.mamoe.mirai.console.internal.fuzzySearchMember
import net.mamoe.mirai.contact.*

object Tools {
    /**
     * 通过消息获取联系人
     * 若[Contact]为[Group]，则可通过群员昵称获取联系人
     * 否则通过QQ号查找，查找失败返回``null``
     * @param msg 传入的消息[String]
     * @return User if only one is found null otherwise
     * */
    fun Contact.getUserOrNull(msg: String): User? {
        val noneAt = msg.replace("@", "").replace(" ", "")
        if (noneAt.isBlank()) {
            return null
        }
        return if (noneAt.contains(Regex("""\D"""))) {
            when (this) {
                is Group -> this.findMemberOrNull(noneAt)
                else -> null
            }
        } else {
            val number = noneAt.toLong()
            when (this) {
                is Group -> this[number]
                else -> bot.getFriend(number) ?: bot.getStranger(number)
            }
        }
    }

    /**
     * 从一个群中模糊搜索昵称是[nameCard]的群员
     * @param nameCard 群员昵称
     * @return Member if only one exist or null otherwise
     * @author mamoe
     * */
    private fun Group.findMemberOrNull(nameCard: String): Member? {
        this.members.singleOrNull { it.nameCardOrNick.contains(nameCard) }?.let { return it }
        this.members.singleOrNull { it.nameCardOrNick.contains(nameCard, ignoreCase = true) }?.let { return it }

        val candidates = this.fuzzySearchMember(nameCard)
        candidates.singleOrNull()?.let {
            if (it.second == 1.0) return it.first // single match
        }
        var maxPerMember: Member? = null
        if (candidates.isNotEmpty()) {
            var maxPer = 0.0
            candidates.forEach {
                if (it.second > maxPer) maxPerMember = it.first
                maxPer = it.second
            }
        }
        return maxPerMember
    }
}