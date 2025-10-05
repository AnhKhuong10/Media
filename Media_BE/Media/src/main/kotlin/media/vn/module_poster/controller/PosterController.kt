package media.vn.module_poster.controller

import jakarta.validation.Valid
import media.vn.module_poster.domain.dto.poster.PosterCreateInput
import media.vn.module_poster.domain.dto.poster.PosterDTO
import media.vn.module_poster.service.PosterService
import media.vn.utils.annotation.ApiMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/v1/poster")
class PosterController(
    private val posterService: PosterService
) {

    @PostMapping()
    @ApiMessage("Create a new Poster")
    fun createPoster(@ModelAttribute @Valid input: PosterCreateInput)
    : ResponseEntity<PosterDTO> {
        println("POSTER CREATED")
        val poster = posterService.createPoster(input)
        return ResponseEntity.status(HttpStatus.CREATED).body(poster)
    }
}