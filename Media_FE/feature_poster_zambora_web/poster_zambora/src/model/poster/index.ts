
import type { ReactionEnum } from "../poster-reaction";
export type Poster = {
  posterId: number;
  title: string;
  content: string;
  filePath: string | null;
  posterType: string;
  companyName: string;
  createDate: string;
  updateDate: string | null;
  createdBy?: string | null;
  updatedBy?: string | null;
  isDraft: boolean;
  isDeleted: boolean;
  userId: string;
};

export type PosterCreateDTO = {
  title: string;
  content: string;
  posterType: string;
  companyName: string;
  userId: string;
};

export type PosterUpdateDTO = {
  posterId: number;
  userId: number;
  title: string;
  content: string;
  posterType: string;
  companyName: string;
}

export type UserPoster = {
  userId: number
  fullName: string,
  email: string,
  dob: Date,
  avatar: string,
  roleName: string
}

export type PosterDTO = {
  posterId: number
  title: string
  content: string
  companyName: string
  posterType: PosterType
  createDate: string
  updateDate: string
  createdBy: string
  updatedBy: string
  isDraft: boolean
  reaction: ReactionEnum
  reactionCount: number
  user: UserPoster
}

export type PosterPage = {
  content: PosterDTO[],
  totalPages: number,
  totalElements: number,
  pageNumber: number,
  pageSize: number
}

export enum PosterType {
  honor,     // = 0
  new_employee,     // = 1          // = 2
}
