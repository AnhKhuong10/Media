import type { Role } from '../role'
export type User = {
    userId: number; // BIGINT --> number
    username: string; // VARCHAR(255)
    password: string; // VARCHAR(255)
    phone?: string; // VARCHAR(50) - optional
    email?: string; // VARCHAR(255) - optional
    fullName?: string; // VARCHAR(255) - optional
    refreshToken?: string; // TEXT - optional
    gender?: string; // VARCHAR(255) - optional
    dob?: string; // DATE - optional, use ISO string
    statusUser?: string; // VARCHAR(255) - optional
    createDate?: string; // TIMESTAMP - optional, use ISO string
    updateDate?: string; // TIMESTAMP - optional, use ISO string
    roleName: string; // BIGINT - optional
    avatar: string;
    //*** */
    homeTown: string; // VARCHAR(255) - optional
    photo?: string; // TEXT - optional
};

export enum GenderEnum {
    male,
    female,
    other
}
