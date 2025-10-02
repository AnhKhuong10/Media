import apiClient from "../../common/axios-client";
import type { Poster } from "../../model/poster";

export async function createPoster(poster: Partial<Poster>): Promise<Poster> {
  const res = await apiClient.post<Poster>("/create-poster", poster);
  return res.data;
}