package media.vn.module_album.graphql.query

import media.vn.module_album.dto.AlbumDTO
import media.vn.module_album.dto.PhotoDTO
import media.vn.module_album.dto.SharedAlbumDTO
import media.vn.module_album.repository.AlbumPhotoRepository
import media.vn.module_album.repository.AlbumRepository
import media.vn.module_album.repository.PhotoRepository
import media.vn.module_album.repository.ShareAlbumRepository
import media.vn.module_album.service.AlbumService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class AlbumQuery(
    private val albumService: AlbumService,
) {


    @QueryMapping
    fun getAlbum(@Argument albumId: Long): AlbumDTO? {
       return albumService.getAlbum(albumId)
    }

    @QueryMapping
    fun getUserAlbums(@Argument userId: Long): List<AlbumDTO> {
        return albumService.getUserAlbums(userId)

    }




}

