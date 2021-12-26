package org.laolittle.plugin.model

import net.mamoe.mirai.console.internal.fuzzySearchMember
import net.mamoe.mirai.console.internal.toDecimalPlace
import net.mamoe.mirai.console.internal.truncate
import net.mamoe.mirai.contact.*

object Tools {
    /**
     * 通过消息获取联系人
     * 若[Contact]为[Group]，则可通过群员昵称获取联系人
     * 否则通过QQ号查找，查找失败返回``null``
     * @param msg 传入的消息[String]
     * @return User if only one is found null otherwise
     * */
    suspend fun Contact.getUserOrNull(msg: String): User? {
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
    private suspend fun Group.findMemberOrNull(nameCard: String): Member? {
        this.members.singleOrNull { it.nameCardOrNick.contains(nameCard) }?.let { return it }
        this.members.singleOrNull { it.nameCardOrNick.contains(nameCard, ignoreCase = true) }?.let { return it }

        val candidates = this.fuzzySearchMember(nameCard)
        candidates.singleOrNull()?.let {
            if (it.second == 1.0) return it.first // single match
        }
        if (candidates.isNotEmpty()) {
            var index = 1
            sendMessage(
                "无法找到成员 $nameCard。 多个成员满足搜索结果或匹配度不足: \n\n" +
                        candidates.joinToString("\n", limit = 6) {
                            val percentage = (it.second * 100).toDecimalPlace(0)
                            "#${index++}(${percentage}%)${it.first.nameCardOrNick.truncate(10)}(${it.first.id})" // #1 15.4%
                        }
            )
        }
        return null
    }
}