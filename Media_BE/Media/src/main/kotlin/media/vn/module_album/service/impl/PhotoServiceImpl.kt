package media.vn.module_album.service.impl

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import media.vn.module_album.dto.PhotoDTO
import media.vn.module_album.entity.PhotoEntity
import media.vn.module_album.repository.PhotoRepository
import media.vn.module_album.service.PhotoService
import media.vn.module_album.service.UploadService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.OffsetDateTime
import kotlin.toString

@Service
class PhotoServiceImpl(val photoRepository: PhotoRepository, val uploadService: UploadService) : PhotoService {

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

    override fun  createPhoto(
        userId: Long,
        file: MultipartFile,
        filename: String? ,
    ): PhotoEntity {
        val url = uploadService.saveImage(file)

        // Tạo metadata JSON
//        val objectMapper = jacksonObjectMapper()
//        val autoMetadata = mapOf(
//            "size" to file.size,
//            "contentType" to file.contentType,
//            "originalName" to file.originalFilename
//        )
//        val merged = extraMetadata?.let { autoMetadata + it } ?: autoMetadata
//        val metadataJson = objectMapper.writeValueAsString(merged)

        val photo = PhotoEntity(
            userId = userId,
            url = url,
            filename = filename ?: file.originalFilename,
//            metadata = metadataJson,
            createdAt = OffsetDateTime.now(),
            isDeleted = false,
            liked = false
        )
        return photoRepository.save(photo)
    }
//    override fun createPhoto(userId: Long, file: MultipartFile, name: String?): PhotoEntity {
//        val url = uploadService.saveImage(file)
//
//        val photo = PhotoEntity(
//            userId = userId,
//            url = url,
//            filename = name ?: file.originalFilename,
//            createdAt = OffsetDateTime.now()
//        )
//
//        return photoRepository.save(photo)
//    }



}//////////////