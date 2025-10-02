package media.vn.module_poster.domain.dto.poster

import media.vn.utils.constants.PosterEnum
import java.time.LocalDate

data class PosterDTO(
    val posterId: Long,
    val title: String,
    val content: String,
    val posterType: PosterEnum,
    val companyName: String,
    val createDate: LocalDate,
    val updateDate: LocalDate?,
    val createdBy: String,
    val updatedBy: String,
)
