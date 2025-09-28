package media.vn.module_album.service

import media.vn.module_album.dto.PhotoDTO
import org.springframework.graphql.data.method.annotation.Argument

interface PhotoService {
    fun getPhoto(@Argument photoId: Long): PhotoDTO?
    fun getUserPhotos(@Argument userId: Long): List<PhotoDTO>
}