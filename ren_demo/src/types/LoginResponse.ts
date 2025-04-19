import type { User } from "./User";
export interface LoginResponse {
    code: number;
    message: string;
    token: string;
    refreshToken: string;
    user: User;
}