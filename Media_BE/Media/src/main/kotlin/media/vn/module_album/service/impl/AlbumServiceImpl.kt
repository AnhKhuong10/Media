package media.vn.module_album.service.impl

import jakarta.transaction.Transactional
import media.vn.module_album.dto.AlbumDTO
import media.vn.module_album.dto.PhotoDTO
import media.vn.module_album.dto.SharedAlbumDTO
import media.vn.module_album.entity.AlbumEntity
import media.vn.module_album.entity.AlbumPhotoEntity
import media.vn.module_album.entity.ShareAlbumEntity
import media.vn.module_album.repository.AlbumPhotoRepository
import media.vn.module_album.repository.AlbumRepository
import media.vn.module_album.repository.PhotoRepository
import media.vn.module_album.repository.ShareAlbumRepository
import media.vn.module_album.service.AlbumService
import media.vn.utils.exception.BusinessException
import media.vn.utils.exception.ErrorCode
import org.springframework.stereotype.Service

@Service
class AlbumServiceImpl(
    val albumRepository: AlbumRepository,
    val photoRepository: PhotoRepository,
    val shareAlbumRepository: ShareAlbumRepository,
    val albumPhotoRepository: AlbumPhotoRepository


) : AlbumService {
    override fun getAlbum(albumId: Long): AlbumDTO? {
        val album = albumRepository.findById(albumId).orElse(null) ?: return null

        val photos = albumPhotoRepository.findPhotosByAlbumId(album.id).map {
            PhotoDTO(
                id = it.id,
                url = it.url,
                filename = it.filename,
                liked = it.liked,
                createdAt = it.createdAt?.toString(),
                isDeleted = it.isDeleted
            )
        }

        val shared = shareAlbumRepository.findAllByAlbumId(album.id).map {
            SharedAlbumDTO(
                id = it.id,
                sharedWith = it.sharedWith,
                permission = it.permission
            )
        }

        return AlbumDTO(
            id = album.id,
            name = album.name,
            createdAt = album.createdAt?.toString(),
            photos = photos,
            sharedWith = shared
        )
    }

    override fun getUserAlbums(userId: Long): List<AlbumDTO> {
        return albumRepository.findAllByUserId(userId).map { album ->
            val photos = albumPhotoRepository.findPhotosByAlbumId(album.id).map {
                PhotoDTO(
                    id = it.id,
                    url = it.url,
                    filename = it.filename,
                    liked = it.liked,
                    createdAt = it.createdAt?.toString(),
                    isDeleted = it.isDeleted
                )
            }

            val shared = shareAlbumRepository.findAllByAlbumId(album.id).map {
                SharedAlbumDTO(
                    id = it.id,
                    sharedWith = it.sharedWith,
                    permission = it.permission
                )
            }

            AlbumDTO(
                id = album.id,
                name = album.name,
                createdAt = album.createdAt?.toString(),
                photos = photos,
                sharedWith = shared
            )
        }
    }

    override fun createAlbum(userId: Long, name: String): AlbumEntity {
        val album = AlbumEntity(userId = userId, name = name)
        return albumRepository.save(album)
    }

    override fun updateAlbumName(albumId: Long, userId: Long, newName: String): AlbumEntity {
        val album = albumRepository.findById(albumId)
            .orElseThrow { IllegalArgumentException("Album not found") }
        if (album.userId != userId) throw IllegalAccessException("Không có quyền sửa album này")
        val updated = album.copy(name = newName)
        return albumRepository.save(updated)
    }

    override fun deleteAlbum(albumId: Long, userId: Long) {
        val album = albumRepository.findById(albumId)
            .orElseThrow { BusinessException(ErrorCode.NOT_FOUND, "Album not found","không tìm thấy album") }
        if (album.userId != userId) throw BusinessException(ErrorCode.FORBIDDEN, "Invalid share user","Không có quyền xoá album này")
        albumRepository.delete(album)
    }

    @Transactional
    override fun addPhotoToAlbum(albumId: Long, photoId: Long, userId: Long, position: Int?): AlbumPhotoEntity {
        val album = albumRepository.findById(albumId)
            .orElseThrow { BusinessException(ErrorCode.NOT_FOUND, "Album not found","Không tìm thấy Album") }
        if (album.userId != userId) throw BusinessException(ErrorCode.FORBIDDEN,"Invalid share user","Không có quyền thêm ảnh vào album này")

        val photo = photoRepository.findById(photoId)
            .orElseThrow { BusinessException(ErrorCode.NOT_FOUND,"Photo not found","Không tìm thấy ảnh") }
        if (photo.userId != userId) throw IllegalAccessException("Không có quyền dùng photo này")

        val albumPhoto = AlbumPhotoEntity(album = album, photo = photo, position = position)
        return albumPhotoRepository.save(albumPhoto)
    }

    @Transactional
    override fun removePhotoFromAlbum(albumPhotoId: Long, userId: Long) {
        val albumPhoto = albumPhotoRepository.findById(albumPhotoId)
            .orElseThrow { IllegalArgumentException("AlbumPhoto not found") }

        if (albumPhoto.album.userId != userId)
            throw IllegalAccessException("Không có quyền xoá ảnh khỏi album này")

        albumPhotoRepository.delete(albumPhoto)
    }

    override fun shareAlbum(
        albumId: Long,
        ownerId: Long,
        sharedWithUserId: Long,
        permission: String
    ): ShareAlbumEntity {
        val album = albumRepository.findById(albumId)
            .orElseThrow { IllegalArgumentException("Album not found") }

        if (album.userId != ownerId) throw IllegalAccessException("Không có quyền share album này")

        val existing = shareAlbumRepository.findByAlbumIdAndSharedWith(albumId, sharedWithUserId)
// /       if (existing != null) {  nếu đã có dữ liệu trong bản chia sẻ (đã chia sẻ) thì cập nhật lại quyền nếu như có thay đổi rồi update
        if (existing.isNotEmpty()){
            val updated = existing[0].copy(permission = permission)
            return shareAlbumRepository.save(updated)
        }

        val share = ShareAlbumEntity(album = album, sharedWith = sharedWithUserId, permission = permission)
        return shareAlbumRepository.save(share)
    }

    override fun revokeShare(albumId: Long, ownerId: Long, sharedWithUserId: Long) {
        val album = albumRepository.findById(albumId)
            .orElseThrow { IllegalArgumentException("Album not found") }

        if (album.userId != ownerId) throw IllegalAccessException("Không có quyền")

        val share = shareAlbumRepository.findByAlbumIdAndSharedWith(albumId, sharedWithUserId)
            ?: throw IllegalArgumentException("Không tồn tại share")

        shareAlbumRepository.delete(share[0])
    }

    override fun listSharedAlbumsOfUser(userId: Long): List<ShareAlbumEntity> {
        return shareAlbumRepository.findBySharedWith(userId)
    }

    //    cái này dùng để lọc hoặc chặn người dùng theo quyền truy cập album
    override fun checkPermission(albumId: Long, userId: Long, required: String): Boolean {
        val album = albumRepository.findById(albumId).orElse(null)
        if (album == null) return false

        // chủ album
        if (album.userId == userId) return true

        // share
        val share = shareAlbumRepository.findByAlbumIdAndSharedWith(albumId, userId)
//        if (share != null) {
        if (share.isNotEmpty()) {

            // ví dụ: "VIEW", "EDIT"
            return share[0].permission?.equals(required, ignoreCase = true) ?: false
        }
        return false
    }


}////////////