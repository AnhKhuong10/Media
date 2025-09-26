package media.vn.module_poster.service.impl

import media.vn.module_poster.domain.entity.User
import media.vn.module_poster.repository.UserRepository
import media.vn.module_poster.service.UserService
import media.vn.utils.exception.BusinessException
import media.vn.utils.exception.ErrorCode
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl (
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
): UserService {
    override fun handleGetUserByUsername(username: String): User {
        return userRepository.findByUsername(username)
            .orElseThrow { BusinessException(ErrorCode.NOT_FOUND, "User $username not found") }
    }

    override fun validateUserLogin(username: String, password: String): User {
        val user = userRepository.findByUsername(username)
            .orElseThrow { BusinessException(ErrorCode.UNAUTHORIZED, "Invalid credentials", "INVALID_LOGIN") }
        if (!passwordEncoder.matches(password, user.password)) {
            throw BusinessException(ErrorCode.UNAUTHORIZED, "Invalid credentials", "INVALID_LOGIN")
        }
        return user
    }

    override fun updateUserToken(userId: Long, refreshToken: String?) {
        val user = userRepository.findById(userId)
            .orElseThrow { BusinessException(ErrorCode.NOT_FOUND, "User not found", "USER_NOT_FOUND") }
        user.refreshToken = refreshToken
        userRepository.save(user)
    }

    override fun getUserByUsernameAndRefreshToken(username: String, refreshToken: String): User? {
        return userRepository.getUserByUsernameAndRefreshToken(username, refreshToken)
    }
}