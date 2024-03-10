package open

interface BookmarkJson {
    fun parse(): Pair<String, String>
}