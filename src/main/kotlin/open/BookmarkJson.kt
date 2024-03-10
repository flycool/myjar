package open

interface BookmarkJson {
    fun parse(
        folder: Folder,
        result: (html: String, filePath: String) -> Unit
    )
}