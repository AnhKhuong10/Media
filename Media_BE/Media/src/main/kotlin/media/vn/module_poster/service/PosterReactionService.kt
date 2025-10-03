package media.vn.module_poster.service

import media.vn.module_poster.domain.dto.poster_reaction.PosterReactionDTO
import media.vn.module_poster.domain.dto.poster_reaction.ReactionSummaryDTO
import media.vn.module_poster.domain.entity.PosterReaction
import media.vn.utils.constants.ReactionEnum

interface PosterReactionService {
    fun reactPoster(userId: Long, posterId: Long, reaction: ReactionEnum): PosterReaction
    fun countReactions(posterId: Long, reaction: ReactionEnum): Long
    fun getReactionsForPoster(posterId: Long): List<PosterReactionDTO>
    fun getReactionSummary(posterId: Long): List<ReactionSummaryDTO>
}