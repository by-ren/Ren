import type { User } from "./User";
export interface LoginResponse {
    code: number;
    msg: string;
    accessToken: string;
    refreshToken: string;
}