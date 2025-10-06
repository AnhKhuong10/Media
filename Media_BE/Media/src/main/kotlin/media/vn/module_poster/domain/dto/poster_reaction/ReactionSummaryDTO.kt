package media.vn.module_poster.domain.dto.poster_reaction

import media.vn.utils.constants.ReactionEnum

data class ReactionSummaryDTO(
    val reaction: ReactionEnum,
    val count: Long
)
