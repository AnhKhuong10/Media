package media.vn.module_poster.controller

import media.vn.module_poster.domain.dto.poster.PosterDTO
import media.vn.module_poster.domain.dto.poster_reaction.ReactionSummaryDTO
import media.vn.module_poster.service.PosterReactionService
import org.springframework.graphql.data.method.annotation.BatchMapping
import org.springframework.stereotype.Controller

@Controller
class PosterBatchController(
    private val posterReactionService: PosterReactionService,
) {

    @BatchMapping
    fun reactionSummary(posters: List<PosterDTO>): Map<PosterDTO, List<ReactionSummaryDTO>>{
        val ids = posters.map { it.posterId }
        val summaries = posterReactionService.findSummaryByPosterIds(ids)

        return posters.associateWith {
            poster -> summaries[poster.posterId]?: emptyList()
        }
    }

    @BatchMapping
    fun totalReactions(posters: List<PosterDTO>): Map<PosterDTO, Long> {
        val ids = posters.map { it.posterId }
        val totals = posterReactionService.findSummaryReactionsByPosterIds(ids)
        return posters.associateWith { totals[it.posterId] ?: 0 }
    }
}