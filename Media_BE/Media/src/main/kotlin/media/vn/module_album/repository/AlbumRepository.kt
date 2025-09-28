package media.vn.module_album.repository

import media.vn.module_album.entity.AlbumEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AlbumRepository : JpaRepository<AlbumEntity, Long> {
    fun findAllByUserId(userId: Long): List<AlbumEntity>
}