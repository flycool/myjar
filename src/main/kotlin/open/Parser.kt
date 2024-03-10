package open

import com.google.gson.Gson
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStreamWriter


fun main(args: Array<String>) {

    writeToFile(BookmarkHtml())
//    writeToFile(BookmarkMD())

}

const val bookmarkFilePath =
    "C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Bookmarks"

fun writeToFile(
    parser: BookmarkJson,
) {
    val bookmark = parserBookmark()
    val folder = bookmark.roots.bookmark_bar

    parser.parse(folder) { html, filePath ->
        val file = File(filePath)
        if (!file.exists()) {
            file.createNewFile()
        }
        val bufferWriter = OutputStreamWriter(FileOutputStream(file), "utf-8")

        bufferWriter.write(html)
        bufferWriter.close()
    }
}

private fun parserBookmark(): Bookmark {
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

