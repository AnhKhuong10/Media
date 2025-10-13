package media.vn.module_poster.repository

import media.vn.module_poster.domain.entity.Poster
import media.vn.module_poster.domain.entity.PosterReaction
import media.vn.module_poster.domain.entity.User
import media.vn.utils.constants.ReactionEnum
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PosterReactionRepository : JpaRepository<PosterReaction, Long> {

    fun findByPosterAndUser(poster: Poster, user: User): PosterReaction?
    fun countByPosterAndPosterReaction(poster: Poster, reaction: ReactionEnum): Long
    fun findByPoster(poster: Poster): List<PosterReaction>

    @Query("SELECT r FROM PosterReaction r WHERE r.poster.posterId IN :posterIds")
    fun findByPosterIds(@Param("posterIds") posterIds: List<Long>): List<PosterReaction>

}