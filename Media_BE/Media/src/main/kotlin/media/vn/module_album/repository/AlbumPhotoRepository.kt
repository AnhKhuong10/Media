package media.vn.module_album.repository

import media.vn.module_album.entity.AlbumEntity
import media.vn.module_album.entity.AlbumPhotoEntity
import media.vn.module_album.entity.PhotoEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AlbumPhotoRepository : JpaRepository<AlbumPhotoEntity, Long> {
    @Query("SELECT ap.photo FROM AlbumPhotoEntity ap WHERE ap.album.id = :albumId")
    fun findPhotosByAlbumId(albumId: Long): List<PhotoEntity>
}