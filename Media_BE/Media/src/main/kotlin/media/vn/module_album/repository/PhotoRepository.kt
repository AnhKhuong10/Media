package media.vn.module_album.repository

import media.vn.module_album.entity.PhotoEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PhotoRepository : JpaRepository<PhotoEntity, Long> {
    fun findAllByUserId(userId: Long): List<PhotoEntity>
}