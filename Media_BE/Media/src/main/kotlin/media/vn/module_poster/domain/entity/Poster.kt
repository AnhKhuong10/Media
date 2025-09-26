package media.vn.module_poster.domain.entity

import jakarta.persistence.*
import media.vn.utils.constants.PosterEnum
import java.time.LocalDate

@Entity
@Table(name = "poster")
data class Poster(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poster_id")
    val posterId: Long,

    val title: String?,
    val content: String?,

    @Column(name = "file_path")
    val filePath: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "poster_type")
    val posterType: PosterEnum,
    val createDate: LocalDate = LocalDate.now(),
    val updateDate: LocalDate?,
    val createdBy: String?,
    val updatedBy: String?,

    @Column(name = "is_draft")
    val isDraft: Boolean = false,

    @Column(name = "is_deleted")
    val isDeleted: Boolean = false,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @OneToMany(mappedBy = "poster", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var posterReactions: List<PosterReaction> = mutableListOf()
)
