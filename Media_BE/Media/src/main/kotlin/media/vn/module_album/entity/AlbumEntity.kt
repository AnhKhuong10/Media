package media.vn.module_album.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.time.OffsetDateTime

@Entity
@Table(name = "album")
data class AlbumEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    val name: String? = null,

    @Column(name = "created_at")
    val createdAt: OffsetDateTime? = OffsetDateTime.now(),

    @OneToMany(mappedBy = "album", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    val albumPhotos: List<AlbumPhotoEntity> = emptyList(),

    @OneToMany(mappedBy = "album", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    val sharedAlbums: List<ShareAlbumEntity> = emptyList()
)