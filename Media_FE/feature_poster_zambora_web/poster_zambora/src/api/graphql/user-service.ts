import { gql } from "@apollo/client/core";
import apolloClient from "../../common/apollo-client";
import type { User } from "../../model/user";

export async function getAllUser(search: string = ""): Promise<User[]> {
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