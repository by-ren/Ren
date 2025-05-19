export interface DynamicRoute {
  path: string
  name: string
  component: string // 对应views目录下的组件路径
  meta?: {
    requiresAuth?: boolean
    roles?: string[]
    menuShow?: string
  }
  children?: DynamicRoute[]
}