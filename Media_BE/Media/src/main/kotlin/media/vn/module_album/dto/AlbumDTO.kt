package media.vn.module_album.dto

data class AlbumDTO(
    val id: Long,
    val name: String?,
    val createdAt: String?,
    val photos: List<PhotoDTO>,
    val sharedWith: List<SharedAlbumDTO>
)

data class SharedAlbumDTO(
    val id: Long,
    val sharedWith: Long,
    val permission: String?
)