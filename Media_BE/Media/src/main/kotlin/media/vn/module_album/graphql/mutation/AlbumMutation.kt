package media.vn.module_album.graphql.mutation

import media.vn.module_album.entity.AlbumEntity
import media.vn.module_album.entity.AlbumPhotoEntity
import media.vn.module_album.entity.ShareAlbumEntity
import media.vn.module_album.service.AlbumService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class AlbumMutation(private  val albumService: AlbumService) {

//////////////////////// Album

    @MutationMapping
    fun createAlbum(@Argument userId: Long, @Argument name: String): AlbumEntity {
        return albumService.createAlbum(userId, name)
    }

    @MutationMapping
    fun updateAlbumName(
        @Argument albumId: Long,
        @Argument userId: Long,
        @Argument newName: String
    ): AlbumEntity {
        return albumService.updateAlbumName(albumId, userId, newName)
    }

    @MutationMapping
    fun deleteAlbum(@Argument albumId: Long, @Argument userId: Long): Boolean {
        albumService.deleteAlbum(albumId, userId)
        return true
    }

//////////////////////////Photo trong Album

    @MutationMapping
    fun addPhotoToAlbum(
        @Argument albumId: Long,
        @Argument photoId: Long,
        @Argument userId: Long,
        @Argument position: Int?
    ): AlbumPhotoEntity {
        return albumService.addPhotoToAlbum(albumId, photoId, userId, position)
    }

    @MutationMapping
    fun removePhotoFromAlbum(
        @Argument albumPhotoId: Long,
        @Argument userId: Long
    ): Boolean {
        albumService.removePhotoFromAlbum(albumPhotoId, userId)
        return true
    }

///////////////////////// Share Album

    @MutationMapping
    fun shareAlbum(
        @Argument albumId: Long,
        @Argument ownerId: Long,
        @Argument sharedWith: Long,
        @Argument permission: String
    ): ShareAlbumEntity {
        return albumService.shareAlbum(albumId, ownerId, sharedWith, permission)
    }

    @MutationMapping
    fun revokeShare(
        @Argument albumId: Long,
        @Argument ownerId: Long,
        @Argument sharedWith: Long
    ): Boolean {
        albumService.revokeShare(albumId, ownerId, sharedWith)
        return true
    }

 ////////////////////////// Query

    @QueryMapping
    fun listSharedAlbumsOfUser(@Argument userId: Long): List<ShareAlbumEntity> {
        return albumService.listSharedAlbumsOfUser(userId)
    }

    @QueryMapping
    fun checkPermission(
        @Argument albumId: Long,
        @Argument userId: Long,
        @Argument required: String
    ): Boolean {
        return albumService.checkPermission(albumId, userId, required)
    }
}