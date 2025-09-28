package media.vn.module_album.repository

import media.vn.module_album.entity.ShareAlbumEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ShareAlbumRepository : JpaRepository<ShareAlbumEntity, Long> {
    fun findAllByAlbumId(albumId: Long): List<ShareAlbumEntity>
    fun findByAlbumIdAndSharedWith(albumId: Long, sharedWithUserId: Long): List<ShareAlbumEntity>
    fun findBySharedWith(sharedWithUserId: Long): List<ShareAlbumEntity>
}