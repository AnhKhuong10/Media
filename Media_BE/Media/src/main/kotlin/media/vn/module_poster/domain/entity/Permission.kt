package media.vn.module_poster.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "permission")
data class Permission (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    val permissionId: Long = 0,

    @Column(nullable = false)
    val permissionName: String,

    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val rolePermissions: List<RolePermission> = mutableListOf()
)