package media.vn.module_poster.domain.dto.poster

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import media.vn.utils.constants.PosterEnum
import org.springframework.web.multipart.MultipartFile

data class PosterUpdateInput(
    val posterId: Long,
    val userId: Long,
    val ownerId: Long,
    val file: MultipartFile?,

    @field:NotBlank(message = "Title cannot be blank")
    val title: String,

    @field:NotBlank(message = "Content cannot be blank")
    val content: String,

    @field:NotNull(message = "PosterType is required")
    val posterType: PosterEnum,

    @field:NotBlank(message = "CompanyName cannot be blank")
    val companyName: String,


    val isDraft: Boolean,
)
