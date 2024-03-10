package open

import java.text.SimpleDateFormat
import java.util.*

class BookmarkMD : BookmarkJson {

    private val mdFilePath = "F:\\obsidianwork\\all\\android\\bookmark.md"

    override fun parse(folder: Folder, result: (html: String, filePath: String) -> Unit) {
        val sb = StringBuilder()
        recurseChildren(sb, folder)
        result(sb.toString(), mdFilePath)
    }

    private var c = 0
    private fun recurseChildren(sb: StringBuilder, folder: Folder) {
        c++
        folder.children?.forEachIndexed { index, f ->
            val type = f.type
            if (type == "url") {
                sb.link((f))
            } else if (type == "folder") {
                sb.title(f.name, c)

                recurseChildren(sb, f)

                c--
                sb.addLine()
            }
        }
    }

    private val epochMillis = 11644473600000

    private fun StringBuilder.link(c: Folder): StringBuilder =
        this.append("[").append(c.name).append("]")
            .append("(").append(c.url).append(")")
            .append(" ")
            .append(chromeTimeValueToDate(c.date_added))
            .append("\r\n")

    private fun chromeTimeValueToDate(dateFlag: String): String {
        val l: Long = dateFlag.toLong()
        val actualDataLong = l / 1000 - epochMillis
        return actualDataLong.toDate()
    }

    private fun Long.toDate(): String {
        val date = Date(this)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val result = dateFormat.format(date)
        return result
    }

    private fun StringBuilder.title(title: String, num: Int): StringBuilder {
        var s = ""
        for (i in 0 until num) {
            s += "#"
        }
        this.append("$s ").append(title).append("\r\n")
        return this
    }

    private fun StringBuilder.addLine() {
        this.append("\r\n").append("---").append("\r\n")
    }


}