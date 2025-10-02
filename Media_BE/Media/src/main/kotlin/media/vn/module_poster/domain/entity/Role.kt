package media.vn.module_poster.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "role")
class Role(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val roleId: Long = 0,

    @Column(nullable = false)
    val roleName: String = "",

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @JsonIgnore
    val users: List<User> = mutableListOf(),

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val rolePermissions: List<RolePermission> = mutableListOf()
)
