package media.vn.module_poster.service

import media.vn.module_poster.domain.dto.user.UserDTO
import media.vn.module_poster.domain.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UserService {
    fun handleGetUserByUsername(username: String): User

    fun validateUserLogin(username: String, password: String): User
    fun updateUserToken(userId: Long, refreshToken: String?)
    fun getUserByUsernameAndRefreshToken(username: String, refreshToken: String): User?

    fun getUserPage(search: String?): List<UserDTO>
}