package util

/**
 * 集合等量分割工具类
 * */
object ListSplitUtils {
    fun <T> splitList(
        messagesList: List<T>,
        groupSize: Int
    ): List<List<T>> {
        val length = messagesList.size
        // 计算可以分成多少组
        val num = (length + groupSize - 1) / groupSize
        val newList: MutableList<List<T>> = ArrayList(num)
        for (i in 0 until num) {
            // 开始位置
            val fromIndex = i * groupSize
            // 结束位置
            val toIndex = if ((i + 1) * groupSize < length) (i + 1) * groupSize else length
            newList.add(messagesList.subList(fromIndex, toIndex))
        }
        return newList
    }
}