package open

data class Bookmark(
    val checksum: String,
    val roots: Root,
    //val sync_metadata:String,
    val version: Int
)

data class Root(
    val bookmark_bar: Folder,
    val other: Folder,
    val synced: Folder
)


/**
 *  children": [  ],
"   date_added": "13209386995523643",
"   date_modified": "0",
"   guid": "82b081ec-3dd3-529c-8475-ab6c344590dd",
"   id": "2",
"   name": "其他书签",
"   type": "folder"
 */
data class Folder(
    val children: MutableList<Folder>?,
    val date_added: String,
    val date_modified: String,
    val guid: String,
    val id: String,
    val name: String,
    val type: String,
    val url: String,
)

/**
 * "date_added": "13314533977559961",
"   guid": "ed9018f2-061b-4987-a9ae-aa00a684b403",
"   id": "2446",
"   name": "love n work E05.221202 韩tv中字_哔哩哔哩_bilibili",
"   type": "url",
"   url": "https://www.bilibili.com/video/BV1hG4y1f7Qu?p=5&vd_source=514b251bc38f3d7d3fccdea6771e0640"
 */
data class Link(
    val date_added: String,
    val guid: String,
    val id: String,
    val name: String,
    val type: String,
    val url: String,
)


