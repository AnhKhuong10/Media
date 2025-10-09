import { gql } from "@apollo/client/core";
import apolloClient from "../../common/apollo-client";
import type { PosterDTO } from "../../model/poster";

export async function getAllPoster(
  page: number,
  size: number,
  search: string
): Promise<{
  content: PosterDTO[];
  totalElements: number;
  totalPages: number;
  pageNumber: number;
  pageSize: number
}> {
  const GET_ALL_POSTER_PAGE = gql`
    query ($page: Int!, $size: Int!, $search: String) {
      postersForHR(page: $page, size: $size, search: $search) {
        content {
          posterId
          title
          content
          companyName
          createDate
          posterType
          updateDate
          createdBy
          updatedBy
          isDraft
          user {
            userId
            fullName
            dob
            avatar
            roleName
          }
        }
        totalElements
        totalPages
        pageNumber
        pageSize
      }
    }
  `;
  try {
    const { data } = await apolloClient.query({
      query: GET_ALL_POSTER_PAGE,
      variables: { page, size, search },
      fetchPolicy: "no-cache",
    });
    return data.postersForHR;
  } catch (error) {
    console.error("Lỗi khi lấy danh sách Poster:", error);
    throw error;
  }
}

export async function getAllPosterDelete(
): Promise<PosterDTO[]> {
  const GET_ALL_POSTER_DELETE = gql`
    query () {
      getListPosterDelete() {
        posterId
        title
        content
        posterType
        companyName
        createDate
        updateDate
        createdBy
        updatedBy
        isDraft
        user {
            userId
            fullName
            dob
            homeTown
            avatar
            roleName
        }
      }
    }
  `;
  try {
    const { data } = await apolloClient.query({
      query: GET_ALL_POSTER_DELETE,
      fetchPolicy: "no-cache",
    });
    return data.postersForHR;
  } catch (error) {
    console.error("Lỗi khi lấy danh sách Poster:", error);
    throw error;
  }
}




