import type { User } from "./User";
export interface LoginResponse {
    code: number;
    message: string;
    accessToken: string;
    refreshToken: string;
}