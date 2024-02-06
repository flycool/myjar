package open

import com.google.gson.Gson
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStreamWriter


fun main(args: Array<String>) {
    writeToMd()
}

const val bookmarkFilePath =
    "C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Bookmarks"

//const val mdFilePath = "G:\\resource\\桌面\\test\\bookmark.md"
const val mdFilePath = "F:\\obsidianwork\\all\\android\\bookmark.md"

fun writeToMd() {
    val bookmark = parserBookmark()

    val file = File(mdFilePath)
    if (!file.exists()) {
        file.createNewFile()
    }
    val bufferWriter = OutputStreamWriter(FileOutputStream(file), "utf-8")

    val folder = bookmark.roots.bookmark_bar
    val stringBuilder = StringBuilder()

    recurseChildren(stringBuilder, folder)

    bufferWriter.write(stringBuilder.toString())
    bufferWriter.close()
}

fun parserBookmark(): Bookmark {
    val content = getJsonContentFromFile(bookmarkFilePath)
    val gson = Gson()
    val bookmark = gson.fromJson(content, Bookmark::class.java)
    return bookmark
}

fun getJsonContentFromFile(filePath: String): String? {
    val file = File(filePath)
    if (!file.exists()) return null

    val inputStream = FileInputStream(file)
    val readBytes = inputStream.readBytes()
    val s = String(readBytes)
    //println(s)
    return s
}


var c = 0
fun recurseChildren(sb: StringBuilder, folder: Folder) {
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

fun StringBuilder.link(c: Folder): StringBuilder =
    this.append("[").append(c.name).append("]")
        .append("(").append(c.url).append(")")
        .append("\r\n")

fun StringBuilder.title(title: String, num: Int): StringBuilder {
    var s = ""
    for (i in 0 until num) {
        s += "#"
    }
    this.append("$s ").append(title).append("\r\n")
    return this
}

fun StringBuilder.addLine() {
    this.append("\r\n").append("---").append("\r\n")
}