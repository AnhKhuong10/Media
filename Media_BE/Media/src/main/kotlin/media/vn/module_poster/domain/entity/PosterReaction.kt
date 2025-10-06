package media.vn.module_poster.domain.entity

import jakarta.persistence.*
import media.vn.utils.constants.ReactionEnum
import java.time.LocalDateTime
import java.time.OffsetDateTime

@Entity
@Table(name = "poster_reaction")
data class PosterReaction(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reaction_id")
    val posterReactionId: Long = 0,

    @ManyToOne
    @JoinColumn(name = "poster_id")
    val poster: Poster,

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction")
    val posterReaction: ReactionEnum,

    @Column(name = "created_at")
    val createDate: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "updated_at")
    var updateDate: OffsetDateTime,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
    )
