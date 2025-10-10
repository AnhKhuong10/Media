import apiClient from "../../common/axios-client";
import type { PosterCreateDTO, PosterUpdateDTO, PosterDTO } from "../../model/poster";

export async function createPoster(poster: Partial<PosterCreateDTO>, file?: File, isDraft?: boolean): Promise<PosterCreateDTO | null> {
  try {
    const formData = new FormData();

    // Append các field text
    if (poster.title) formData.append("title", poster.title);
    if (poster.content) formData.append("content", poster.content);
    if (poster.posterType) formData.append("posterType", poster.posterType);
    if (poster.companyName) formData.append("companyName", poster.companyName);
    if (poster.userId) formData.append("userId", String(poster.userId));
    formData.append("ownerId", "1")
    formData.append("isDraft", String(isDraft ?? false));

    // Append file nếu có
    if (file) {
      formData.append("file", file);
    }

    const res = await apiClient.post<PosterCreateDTO>("/poster", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });

    return res.data;
  } catch (error: any) {
    console.error("Lỗi khi tạo Poster:", error.message || error);
    return null;
  }
}

export async function updatePoster(
  poster: Partial<PosterDTO>,
  file?: File
): Promise<PosterDTO | null> {
  try {
    const formData = new FormData();

    // Append các field text
    if (poster.posterId) formData.append("posterId", String(poster.posterId));
    if (poster.title) formData.append("title", poster.title);
    if (poster.content) formData.append("content", poster.content);
    if (poster.posterType) formData.append("posterType", String(poster.posterType));
    if (poster.companyName) formData.append("companyName", poster.companyName);
    if (poster.user) formData.append("userId", String(poster.user.userId));
    formData.append("isDraft", String(poster.isDraft))
    formData.append("ownerId", "1")

    // Append file nếu có
    if (file) {
      formData.append("file", file);
    }

    const res = await apiClient.put<PosterDTO>("/poster", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    return res.data;
  } catch (error: any) {
    console.error("Lỗi khi tạo Poster:", error.message || error);
    return null;
  }
}



