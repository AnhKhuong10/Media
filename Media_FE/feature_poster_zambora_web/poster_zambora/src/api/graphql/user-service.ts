import { gql } from "@apollo/client/core";
import apolloClient from "../../common/apollo-client";
import type { User } from "../../model/user";
import type { Poster } from "../../model/poster";

export async function getAllUser(
  search: string = ""
): Promise<User[]> {
  const GET_USER_PAGE = gql`
    query ($search: String) {
      getUserPage(search: $search) {
        content {
          userId
          username
          fullName
          gender
          email
          phone
          dob
          roleName
          homeTown
          createDate
          avatar
        }
      }
    }
  `;

  try {
    const { data } = await apolloClient.query({
      query: GET_USER_PAGE,
      variables: { search }, // truyền biến search vào query
      fetchPolicy: "no-cache",
    });

    console.log("Data nhận được từ getAllUser:", data);

    // data.getUserPage.content mới là User[]
    return data.getUserPage.content as User[];
  } catch (error) {
    console.error("Lỗi khi gọi getAllUser:", error);
    return [];
  }
}

export async function getAllPoster(
  page: number,
  size: number,
  search: string
): Promise<{ content: Poster[]; totalElements: number; totalPages: number }> {
  const GET_ALL_POSTER_PAGE = gql`
    query ($page: Int!, $size: Int!, $search: String) {
      postersForHR(page: $page, size: $size, search: $search) {
        content {
          posterId
          title
          content
          filePath
          posterType
          companyName
          createDate
          updateDate
          createdBy
          updatedBy
          isDraft
          isDeleted
          user {
            userId
            fullName
            email
            roleName
            avatar
          }
        }
        totalElements
        totalPages
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