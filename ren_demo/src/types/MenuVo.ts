export interface MenuVO {
  index: string
  name: string
  icon: string
  children?: MenuVO[]
}