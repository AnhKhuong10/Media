package media.vn.module_poster.repository

import media.vn.module_poster.domain.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String): Optional<User>

    @Query(
        value = "SELECT * FROM users u WHERE u.username = :username AND BINARY u.refreshToken = :refreshToken",
        nativeQuery = true
    )
    fun getUserByUsernameAndRefreshToken(
        @Param("refreshToken") refreshToken: String,
        @Param("username") username: String
    ): User?

    @Query(value = """
    SELECT * FROM "user" u
    WHERE (:search IS NULL 
       OR LOWER(CAST(u.username AS TEXT)) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(CAST(u.full_name AS TEXT)) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(CAST(u.phone AS TEXT)) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(CAST(u.email AS TEXT)) LIKE LOWER(CONCAT('%', :search, '%')))
""", nativeQuery = true)
    fun listUser(
        @Param("search") search: String?,
    ) : List<User>
}