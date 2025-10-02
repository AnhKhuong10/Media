package media.vn.module_poster.domain.dto.poster

import media.vn.utils.constants.PosterEnum
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

data class PosterCreateInput(
    val userId: Long,
    val file: MultipartFile,
    val title: String,
    val content: String,
    val posterType: PosterEnum,
    val companyName: String,
)
