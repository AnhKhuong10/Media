package media.vn.module_album.graphql.query

import media.vn.module_album.dto.PhotoDTO
import media.vn.module_album.service.PhotoService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class PhotoQuery (private val photoService: PhotoService) {

    @QueryMapping
    fun getPhoto(@Argument photoId: Long): PhotoDTO? {
        return photoService.getPhoto(photoId)
    }
    @QueryMapping
    fun getUserPhotos(@Argument userId: Long): List<PhotoDTO> {
        return photoService.getUserPhotos(userId)
    }
}