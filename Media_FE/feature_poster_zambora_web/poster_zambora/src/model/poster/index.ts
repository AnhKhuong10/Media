import type { User } from "../user";

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
  user: User;
};
