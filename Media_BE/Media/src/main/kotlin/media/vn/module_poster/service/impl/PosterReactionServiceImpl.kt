package media.vn.module_poster.service.impl

import media.vn.module_poster.domain.dto.poster_reaction.PosterReactionDTO
import media.vn.module_poster.domain.dto.poster_reaction.ReactionSummaryDTO
import media.vn.module_poster.domain.entity.PosterReaction
import media.vn.module_poster.repository.PosterReactionRepository
import media.vn.module_poster.repository.PosterRepository
import media.vn.module_poster.repository.UserRepository
import media.vn.module_poster.service.PosterReactionService
import media.vn.utils.constants.ReactionEnum
import media.vn.utils.exception.BusinessException
import media.vn.utils.exception.ErrorCode
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PosterReactionServiceImpl(
    private val posterReactionRepository: PosterReactionRepository,
    private val userRepository: UserRepository,
    private val posterRepository: PosterRepository
): PosterReactionService {
    override fun reactPoster(userId: Long, posterId: Long, reaction: ReactionEnum): PosterReaction {
        val user = userRepository.findById(userId)
            .orElseThrow { BusinessException(ErrorCode.NOT_FOUND, "User not found") }

        val poster = posterRepository.findById(posterId)
            .orElseThrow{ BusinessException(ErrorCode.NOT_FOUND, "Post not found") }

        val existingsReaction = posterReactionRepository.findByPosterAndUser(poster, user)

        return if(existingsReaction != null){
            val updated = existingsReaction.copy(posterReaction = reaction)
            posterReactionRepository.save(updated)
        }else{
            val newReaction = PosterReaction(
                poster = poster,
                posterReaction = reaction,
                createDate = LocalDateTime.now(),
                user = user
            )
            posterReactionRepository.save(newReaction)
        }

    }

    override fun countReactions(posterId: Long, reaction: ReactionEnum): Long {
        val poster = posterRepository.findById(posterId)
            .orElseThrow { BusinessException(ErrorCode.NOT_FOUND, "Post not found") }
        return posterReactionRepository.countByPosterAndPosterReaction(poster, reaction)
    }

    override fun getReactionsForPoster(posterId: Long): List<PosterReactionDTO> {
        val poster = posterRepository.findById(posterId)
            .orElseThrow { BusinessException(ErrorCode.NOT_FOUND, "Post not found") }

        val reactions = posterReactionRepository.findByPoster(poster)

        return reactions.map { it.toDTO() }
    }

    override fun getReactionSummary(posterId: Long): List<ReactionSummaryDTO> {
        val poster = posterRepository.findById(posterId)
            .orElseThrow{ BusinessException(ErrorCode.NOT_FOUND, "Post not found") }

        val reactions = posterReactionRepository.findByPoster(poster)

        return reactions.groupingBy { it.posterReaction }
            .eachCount()
            .map { (reaction, count) ->
                ReactionSummaryDTO(reaction, count.toLong())
            }
    }


    private fun PosterReaction.toDTO(): PosterReactionDTO {
        return PosterReactionDTO(
            posterReactionId = this.posterReactionId,
            posterId = this.poster.posterId,
            username = this.user.username,
            avatar = this.user.avatar,
            reaction = this.posterReaction,
            createdAt = this.createDate
        )
    }


}
