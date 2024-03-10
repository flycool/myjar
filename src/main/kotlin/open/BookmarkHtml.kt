package open

import kotlinx.html.*
import kotlinx.html.stream.createHTML

class BookmarkHtml : BookmarkJson {

    private val htmlFilePath = "F:\\obsidianwork\\all\\android\\bookmarks.html"

    override fun parse(folder: Folder, result: (html: String, filePath: String) -> Unit) {
        val html = createHtml(folder)
        result(html, htmlFilePath)
    }

    private fun createHtml(folder: Folder): String {
        val html = createHTML().html {
            createHead()
            createBody(folder)
        }
        val htmlResult = html.replace("h3", "H3")
        return htmlResult
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
