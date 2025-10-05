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
  userId: String;
};

export type PosterCreateDTO = {
  title: string;
  content: string;
  posterType: string;
  companyName: string;
  userId: String;
};

export type PosterDTO = {
  posterId: number
  title: String
  content: String
  companyName: String
  posterType: PosterType
  createDate: String
  updateDate: String
  createdBy: String
  updatedBy: String
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
