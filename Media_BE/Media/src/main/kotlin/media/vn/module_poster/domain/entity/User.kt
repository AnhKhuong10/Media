package media.vn.module_poster.domain.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.nimbusds.openid.connect.sdk.claims.Gender
import jakarta.persistence.*
import media.vn.utils.constants.GenderEnum
import media.vn.utils.constants.StatusUser
import java.time.LocalDate

@Entity
@Table(name = "\"user\"")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    val userId: Long = 0,

    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false, unique = true)
    var phone: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(name ="full_name", nullable = false)
    var fullName: String,

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    var refreshToken: String? = null,

    @Column(name = "home_town")
    var homeTown: String? = null,

    @Column(name = "create_date")
    var createDate: LocalDate? = null,

    @Column(name = "avatar")
    var avatar: String? = null,

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    var gender: GenderEnum,

    @Column(name = "status_user")
    @Enumerated(EnumType.STRING)
    var statusUser: StatusUser,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    var dob: LocalDate,

    @ManyToOne @JoinColumn(name = "role_id")
    var role: Role,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var posters: List<Poster> = mutableListOf()
)