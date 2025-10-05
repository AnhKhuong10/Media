import { PosterCreateDTO } from './../../model/poster/index';
import { gql } from "@apollo/client/core";
import apolloClient from "../../common/apollo-client";
import type { Poster, PosterDTO, PosterPage } from "../../model/poster";



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