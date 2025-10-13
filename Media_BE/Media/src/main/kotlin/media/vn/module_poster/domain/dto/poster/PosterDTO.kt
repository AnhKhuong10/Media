package media.vn.module_poster.domain.dto.poster

import media.vn.module_poster.domain.dto.poster_reaction.ReactionSummaryDTO
import media.vn.utils.constants.GenderEnum
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
    val user: UserPoster,
    val totalReactions: Long = 0,
    val reactionSummary: List<ReactionSummaryDTO>? = null,
    val isDraft: Boolean = false,
){
    data class UserPoster(
        val userId: Long,
        val fullName: String,
        val dob: LocalDate?,
        val homeTown: String,
        val avatar: String,
        val roleName: String,
    )
}
