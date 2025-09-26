package media.vn.module_poster.service

import media.vn.module_poster.domain.entity.User

interface UserService {
    fun handleGetUserByUsername(username: String): User

    fun validateUserLogin(username: String, password: String): User
    fun updateUserToken(userId: Long, refreshToken: String?)
    fun getUserByUsernameAndRefreshToken(username: String, refreshToken: String): User?

}