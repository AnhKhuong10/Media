package media.vn.module_poster.domain.entity

import jakarta.persistence.*
import media.vn.utils.constants.PosterEnum
import java.time.LocalDate

@Entity
@Table(name = "poster")
data class Poster(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poster_id")
    val posterId: Long = 0,

    var title: String,
    var content: String,

    @Column(name = "file_path")
    var filePath: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "poster_type")
    var posterType: PosterEnum,

    @Column(name = "company_name")
    var companyName: String,

    @Column(name = "create_date")
    var createDate: LocalDate = LocalDate.now(),

    @Column(name = "update_date")
    var updateDate: LocalDate?,

    @Column(name = "created_by")
    val createdBy: String?,

    @Column(name = "updated_by")
    var updatedBy: String?,

    @Column(name = "is_draft")
    var isDraft: Boolean = false,

    @Column(name = "is_deleted")
    val isDeleted: Boolean = false,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    var owner: User,

    @OneToMany(mappedBy = "poster", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var posterReactions: List<PosterReaction>? = null
)
