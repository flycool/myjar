package open

import kotlinx.html.*
import kotlinx.html.stream.createHTML

//fun main() {
//    BookMarkHtml.createHtml(folderData)
//}

class BookmarkHtml(private val folder: Folder):BookmarkJson {

    private val htmlFilePath = "F:\\obsidianwork\\all\\android\\bookmarks.html"

    override fun parse(): Pair<String, String> {
        return createHtml(folder)
    }

    private fun createHtml(folder: Folder): Pair<String, String> {
        val html = createHTML().html {
            createHead()
            createBody(folder)
        }
        val replace = html.replace("h3", "H3")
        //println(replace)
        return Pair(replace, htmlFilePath)
    }

    private fun HTML.createHead() {
        head {
            meta {
                httpEquiv = "Content-Type"
                content = "text/html; charset=UTF-8"
            }
            title("bookmarks")
        }
    }

    private fun HTML.createBody(folder: Folder) {
        body {
            h1 {
                +"bookmarks"
            }
            dl {
                recurseChildrenForHtml(folder)
            }
        }
    }

    private fun DL.recurseChildrenForHtml(folder: Folder) {
        dl {
            folder.children?.forEachIndexed { index, f ->
                val type = f.type
                if (type == "url") {
                    dt {
                        a(f.url) { +f.name }
                    }
                } else if (type == "folder") {
                    dt {
                        this@dl.h3 { +f.name }
                    }
                    recurseChildrenForHtml(f)
                }
            }
        }
    }

}

val folderChild = mutableListOf(
    Folder(
        children = null,
        date_modified = "",
        date_added = "",
        guid = "",
        id = "",
        name = "你好啊",
        type = "folder",
        url = "http://www.baidu.com"
    ),
    Folder(
        children = null,
        date_modified = "",
        date_added = "",
        guid = "",
        id = "",
        name = "good2",
        type = "url",
        url = "http://www.baidu.com"
    ),
    Folder(
        children = null,
        date_modified = "",
        date_added = "",
        guid = "",
        id = "",
        name = "good3",
        type = "url",
        url = "http://www.baidu.com"
    )
)

val folderData = Folder(
    children = folderChild,
    date_modified = "",
    date_added = "",
    guid = "",
    id = "",
    name = "good",
    type = "folder",
    url = "http://www.baidu.com"
)
