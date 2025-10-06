package media.vn.module_poster.domain.dto.poster_reaction

import media.vn.utils.constants.ReactionEnum
import java.time.OffsetDateTime

data class PosterReactionDTO(
    val posterReactionId: Long,
    val posterId: Long,
    val username: String,
    val avatar: String?,
    val reaction: ReactionEnum,
    val createdAt: OffsetDateTime
)
