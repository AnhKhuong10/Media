package media.vn.module_album.service.impl

import media.vn.module_album.dto.PhotoDTO
import media.vn.module_album.repository.PhotoRepository
import media.vn.module_album.service.PhotoService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.stereotype.Service
import kotlin.toString

@Service
class PhotoServiceImpl(val photoRepository: PhotoRepository) : PhotoService {

    override fun getPhoto( photoId: Long): PhotoDTO? {
        val photo = photoRepository.findById(photoId).orElse(null) ?: return null
        return PhotoDTO(
            id = photo.id,
            url = photo.url,
            filename = photo.filename,
            liked = photo.liked,
            createdAt = photo.createdAt?.toString(),
            isDeleted = photo.isDeleted
        )
    }

    override fun getUserPhotos(userId: Long): List<PhotoDTO> {
        return photoRepository.findAllByUserId(userId).map {
            PhotoDTO(
                id = it.id,
                url = it.url,
                filename = it.filename,
                liked = it.liked,
                createdAt = it.createdAt?.toString(),
                isDeleted = it.isDeleted
            )
        }
    }





}//////////////