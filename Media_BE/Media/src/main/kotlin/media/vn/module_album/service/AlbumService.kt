package media.vn.module_album.service

import media.vn.module_album.dto.AlbumDTO
import media.vn.module_album.dto.PhotoDTO
import media.vn.module_album.entity.AlbumEntity
import media.vn.module_album.entity.AlbumPhotoEntity
import media.vn.module_album.entity.ShareAlbumEntity
import org.springframework.graphql.data.method.annotation.Argument

interface AlbumService {
    fun getAlbum(albumId: Long): AlbumDTO?
    fun getUserAlbums(userId: Long): List<AlbumDTO>

    fun createAlbum(userId: Long, name: String): AlbumEntity
    fun updateAlbumName(albumId: Long, userId: Long, newName: String): AlbumEntity
    fun deleteAlbum(albumId: Long, userId: Long)

    /// ảnh và album
    fun addPhotoToAlbum(albumId: Long, photoId: Long, userId: Long, position: Int? = null): AlbumPhotoEntity
    fun removePhotoFromAlbum(albumPhotoId: Long, userId: Long)

    // share và album
    fun shareAlbum(albumId: Long, ownerId: Long, sharedWithUserId: Long, permission: String): ShareAlbumEntity
    fun revokeShare(albumId: Long, ownerId: Long, sharedWithUserId: Long)
    fun listSharedAlbumsOfUser(userId: Long): List<ShareAlbumEntity>

    // check queenf
    fun checkPermission(albumId: Long, userId: Long, required: String): Boolean

}