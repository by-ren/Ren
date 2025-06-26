export interface User {
	userId: number; // 对应 Java 的 Long 类型（后端返回数值）
	username: string;
	roles: string[];
	enabled: boolean;
	accountNonExpired: boolean;
	accountNonLocked: boolean;
	credentialsNonExpired: boolean;
}
