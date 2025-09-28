package media.vn.module_album.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.time.OffsetDateTime
import jakarta.persistence.FetchType


@Entity
@Table(name = "photo")
data class PhotoEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    val url: String? = null,
    val filename: String? = null,

    @Column(columnDefinition = "jsonb")
    val metadata: String? = null,

    @Column(name = "deleted_at")
    val deletedAt: OffsetDateTime? = null,

    @Column(name = "created_at")
    val createdAt: OffsetDateTime? = OffsetDateTime.now(),

    val liked: Boolean = false,

    @Column(name = "update_at")
    val updateAt: OffsetDateTime? = null,

    @Column(name = "is_deleted")
    val isDeleted: Boolean = false,

    @OneToMany(mappedBy = "photo", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    val albumPhotos: List<AlbumPhotoEntity> = emptyList()
)