package media.vn.module_poster.repository

import media.vn.module_poster.domain.entity.Poster
import media.vn.module_poster.domain.entity.PosterReaction
import media.vn.module_poster.domain.entity.User
import media.vn.utils.constants.ReactionEnum
import org.springframework.data.jpa.repository.JpaRepository

interface PosterReactionRepository : JpaRepository<PosterReaction, Long> {

    fun findByPosterAndUser(poster: Poster, user: User): PosterReaction?
    fun countByPosterAndPosterReaction(poster: Poster, reaction: ReactionEnum): Long
    fun findByPoster(poster: Poster): List<PosterReaction>
}