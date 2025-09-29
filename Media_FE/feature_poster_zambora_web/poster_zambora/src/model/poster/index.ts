import type { User } from "../model/user";
export type Poster = {
    posterId: string;
    title: string;
    content: string;
    filePath: string;
    createdAt: string;
    updatedAt: string;
    createdBy: string;
    updatedBy: string;
    isDraft: boolean;
    isDeleted: boolean;
    user: User;
    postStyleId: string;
    //photoId: string;
    //*****/
    companyName: string;
    month: string;
    year: string;
}
