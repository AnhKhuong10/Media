package media.vn.module_album.service

import media.vn.module_album.dto.PhotoDTO
import media.vn.module_album.entity.PhotoEntity
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.web.multipart.MultipartFile

interface PhotoService {
    fun getPhoto(@Argument photoId: Long): PhotoDTO?
    fun getUserPhotos(@Argument userId: Long): List<PhotoDTO>

    //    fun createPhoto(userId: Long, file: MultipartFile, name: String?): PhotoEntity
    fun createPhoto(
        userId: Long,
        file: MultipartFile,
        filename: String? = null,
    ): PhotoEntity

    fun createPhotoOnAlbum(userId: Long, file: MultipartFile, ): PhotoEntity
}