package media.vn.module_poster.controller

import media.vn.module_poster.domain.dto.poster_reaction.PosterReactionDTO
import media.vn.module_poster.domain.dto.poster_reaction.ReactionSummaryDTO
import media.vn.module_poster.domain.entity.PosterReaction
import media.vn.module_poster.service.PosterReactionService
import media.vn.utils.constants.ReactionEnum
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class PosterReactionQuery(
    private val posterReactionService: PosterReactionService
){
    @QueryMapping
    fun getReactionsForPoster(
        @Argument posterId: Long,
    ): List<PosterReactionDTO>{
        return posterReactionService.getReactionsForPoster(posterId)
    }

    @QueryMapping
    fun getReactionSummary(
        @Argument posterId: Long,
    ): List<ReactionSummaryDTO>{
        return posterReactionService.getReactionSummary(posterId)
    }
}

@Controller
class PosterReactionMutation(
    private val posterReactionService: PosterReactionService
){

    @MutationMapping
    fun reactionPoster(
        @Argument userId: Long,
        @Argument posterId: Long,
        @Argument reaction: ReactionEnum,
    ): PosterReaction{
        return posterReactionService.reactPoster(userId, posterId, reaction)
    }
}


