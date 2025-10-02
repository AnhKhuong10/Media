package media.vn.module_poster.controller

import media.vn.module_poster.domain.dto.user.UserPage
import media.vn.module_poster.service.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.Query
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class MediaQuery(
    private val userService: UserService,
){

    @QueryMapping
    fun getUserPage(
        @Argument search: String?,
    ): UserPage{

        val result = userService.getUserPage(search)

        return UserPage(
            result
        )
    }
}