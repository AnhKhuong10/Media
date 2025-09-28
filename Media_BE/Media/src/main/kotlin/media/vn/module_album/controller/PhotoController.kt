package media.vn.module_album.controller
import media.vn.module_album.dto.PhotoDTO
import media.vn.module_album.entity.PhotoEntity
import media.vn.module_album.service.PhotoService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/photos")
class PhotoController(
    private val photoService: PhotoService
) {

    @PostMapping("/create")
    fun createPhoto(
        @RequestParam userId: Long,
        @RequestParam(required = false) name: String?,
        @RequestParam file: MultipartFile,
    ): PhotoEntity {
        return photoService.createPhoto(userId, file, name, )
    }
}
