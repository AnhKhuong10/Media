package media.vn.module_poster.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "role_permission")
data class RolePermission(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_permission_id")
    val rolePermissionMId: Long = 0,

    @ManyToOne @JoinColumn(name = "role_id")
    val role: Role? = null,

    @ManyToOne @JoinColumn(name = "permission_id")
    val permission: Permission? = null
)
