import apiClient from "../../common/axios-client";
import type { PosterCreateDTO } from "../../model/poster";

export async function createPoster(poster: Partial<PosterCreateDTO>, file?: File): Promise<PosterCreateDTO | null> {
  try {
    const formData = new FormData();

    // Append các field text
    if (poster.title) formData.append("title", poster.title);
    if (poster.content) formData.append("content", poster.content);
    if (poster.posterType) formData.append("posterType", poster.posterType);
    if (poster.companyName) formData.append("companyName", poster.companyName);
    if (poster.userId) formData.append("userId", String(poster.userId));

    // Append file nếu có
    if (file) {
      formData.append("file", file);  // 👈 file upload từ input
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

