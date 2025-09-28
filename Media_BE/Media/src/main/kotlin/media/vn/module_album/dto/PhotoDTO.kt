package media.vn.module_album.dto



data class PhotoDTO(
    val id: Long,
    val url: String?,
    val filename: String?,
    val liked: Boolean,
    val createdAt: String?,
    val isDeleted: Boolean
)


