import type { Poster } from "../poster"
import type { User } from "../user"
export enum ReactionEnum {
    like,
    love,
    haha,
    wow,
    sad,
    angry
}

export type PosterReaction = {
    posterReactionId: number,
    poster: Poster,
    posterReaction: ReactionEnum,
    createDate: string,
    updateDate: string,
    user: User
}

export type PosterReactionDTO = {
    posterReactionId: number,
    
}