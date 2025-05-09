package com.ren.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.ren.common.core.vo.TreeSelectVO;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 格式化树结构方法
 * 
 * @author admin
 */
public class TreeUtils {

    /**
     * 通用树形结构构建方法
     * @param list 原始扁平列表（必传）
     * @param isRoot 判断是否为根节点的条件函数（非必传，不传则将第一层无父节点的节点升级为根节点）
     * @param idFieldName 主键Id字段名 默认为id（如果主键字段名正好为id，则可不传，否则必传）
     * @param parentIdFieldName 父节点Id字段名 默认为parentId（如果父节点Id字段名正好为parentId，则可不传，否则必传）
     * @param childrenFieldName 子节点列表字段名 默认为children（如果子节点列表字段名正好为children，则可不传，否则必传）
     * @param orderNumFieldName 排序字段名（非必传，不传则不进行排序）
     * @return 树形结构列表
     */
    public static <T> List<T> formatTree(
            List<T> list,
            Predicate<T> isRoot,
            String idFieldName,String parentIdFieldName,String childrenFieldName,String orderNumFieldName) {

        if(list == null){
            throw new RuntimeException("需格式化列表不可为空");
        }

        idFieldName = StrUtil.isBlank(idFieldName) ? "id" : idFieldName;
        parentIdFieldName = StrUtil.isBlank(parentIdFieldName) ? "parentId" : parentIdFieldName;
        childrenFieldName = StrUtil.isBlank(childrenFieldName) ? "children" : childrenFieldName;

        // 1. 创建节点映射表：以节点ID为key，节点对象为value
        Map<Object, T> nodeMap = new HashMap<>();
        for (T node : list) {
            // 通过 BeanUtil 动态根据属性名获取属性值（获取主键Id值）
            Object id = BeanUtil.getProperty(node, idFieldName);
            if(id == null){
                throw new RuntimeException("所传入的idFieldName字段名为无效值");
            }
            nodeMap.put(id, node);
        }

        // 2.1 初始化根节点列表
        List<T> roots = new ArrayList<>();
        // 2.2 记录节点是否有父节点(用于处理不存在根节点的情况)
        Map<Object, Boolean> hasParentMap = new HashMap<>();

        // 3. 遍历所有节点，添加空的根节点列表，同时构建树形结构，并将数据存储到节点映射表中
        for (T node : list) {
            // 3.1 判断当前节点是否为根节点，如果是根节点，直接插入根节点列表，并结束本次循环，继续下一次循环
            if (isRoot != null && isRoot.test(node)) {
                roots.add(node);
                continue;
            }

            // 3.2 如果不是父节点，通过 BeanUtil 动态根据属性名获取属性值（获取父节点ID值）
            Object parentId = BeanUtil.getProperty(node, parentIdFieldName);
            if(parentId == null){
                throw new RuntimeException("所传入的parentIdFieldName字段名为无效值");
            }

            // 3.3 判断父节点是否存在于节点映射表中，如果不存在，则将当前节点标记为没有父节点
            if (!nodeMap.containsKey(parentId)) {
                hasParentMap.put(BeanUtil.getProperty(node, idFieldName), false);
            } else {
                hasParentMap.put(BeanUtil.getProperty(node, idFieldName), true);
            }

            // 3.4 根据上方获取到的父节点值在节点映射表中查找出对应的对象
            T parent = nodeMap.get(parentId);

            // 3.5 若存在父节点，将当前节点加入父节点的子列表
            if (parent != null) {
                // 通过 BeanUtil 动态根据属性名获取属性值（获取父节点的子节点列表内容）
                List<T> children = BeanUtil.getProperty(parent, childrenFieldName);
                // 如果不存在，则重新创建一个空列表
                if (children == null) {
                    throw new RuntimeException("所传入的childrenFieldName字段名为无效值");
                }
                // 将该子节点添加到父节点的子列表中
                children.add(node);
                // 通过函数式接口将子列表设置回父节点
                BeanUtil.setProperty(parent, childrenFieldName, children);
                // 3.6 将父节点重新放入节点映射表中，以便后续递归使用
                nodeMap.put(parentId, parent);
            }
        }

        // 将所有没有父节点的节点都升级为根节点
        for (T node : list) {
            Object nodeId = BeanUtil.getProperty(node, idFieldName);
            // getOrDefault表示从Map中安全取值，如果存在，则返回值，不存在返回默认值true
            if (!hasParentMap.getOrDefault(nodeId, true)) {
                roots.add(node);
            }
        }

        // 4. 遍历所以根节点，然后从节点映射表中取出对应的节点，进行替换
        for(int i=0;i<roots.size();i++){
            Object parentId = BeanUtil.getProperty(roots.get(i), idFieldName);
            if (parentId == null) {
                throw new RuntimeException("所传入的idFieldName字段名为无效值");
            }
            T parent = nodeMap.get(parentId);
            if (parent != null) {
                roots.set(i, parent);
            }
        }

        // 5. 递归排序所有子节点（按 orderNum 排序）
        // 只有排序字段不为空才排序，否则不排序
        if(StrUtil.isNotBlank(orderNumFieldName)){
            sortChildren(roots,childrenFieldName,orderNumFieldName);
        }
        return roots;
    }

    /**
     * 递归排序子节点
     * @param nodes 当前层节点列表
     * @param childrenFieldName 子节点列表字段名 默认为children
     * @param orderNumFieldName 排序字段名 默认为orderNum
     */
    private static <T> void sortChildren(List<T> nodes,String childrenFieldName,String orderNumFieldName) {
        if(nodes == null || nodes.isEmpty()){
            return;
        }
        if (BeanUtil.getProperty(nodes.get(0), orderNumFieldName) == null) {
            throw new RuntimeException("所传入的orderNumFieldName字段名为无效值");
        }
        // 5.1 按 orderNum 排序当前层节点,通过 BeanUtil 动态根据属性名获取属性值（获取序号值）
        nodes.sort(Comparator.comparingInt(node -> BeanUtil.getProperty(node, orderNumFieldName)));
        //下面这种写法是上面写法的详细写法
        //nodes.sort((o1,o2) -> Integer.compare(BeanUtil.getProperty(o1, orderNumFieldName),BeanUtil.getProperty(o2, orderNumFieldName)));

        for (T node : nodes) {
            //  5.2 获取当前节点的子节点列表
            List<T> children = BeanUtil.getProperty(node, childrenFieldName);
            // 5.3 如果子节点列表不为空，则递归排序子节点
            if (children != null && !children.isEmpty()) {
                sortChildren(children,childrenFieldName,orderNumFieldName);
            }
            // 5.4 将排序后的子节点列表设置回当前节点
            BeanUtil.setProperty(node, childrenFieldName, children);
        }
    }

    /**
     * 将普通树形结构转换为下拉框树形结构
     * @param tList 需要转换的列表
     * @param idFieldName 树形下拉框值字段名 默认为id（注意：该字段名对应的字段需要是 Long类型，一般为主键）
     * @param labelFieldName 树形下拉框名称字段名 默认为label（注意：该字段名对应的字段需要是 String类型）
     * @param disabledFieldName 树形下拉框是否禁用字段名 默认为disabled（注意：该字段名对应的字段需要是 short类型，0表示正常 1表示禁用）
     * @param childrenFieldName 树形下拉框子节点列表字段名 默认为children（注意：该字段名对应的字段需要为是 与所需要转换的列表类型相同的子列表）
     * @return java.util.List<com.ren.common.core.vo.TreeSelectVO>
     * @author admin
     * @date 2025/05/09 22:35
     */
    public static <T> List<TreeSelectVO> convertTreeSelectForAll(List<T> tList,String idFieldName, String labelFieldName, String disabledFieldName, String childrenFieldName){
        //判断是否存在，不存在则设置默认字段名
        idFieldName = StrUtil.isBlank(idFieldName) ? "id" : idFieldName;
        labelFieldName = StrUtil.isBlank(labelFieldName) ? "label" : labelFieldName;
        disabledFieldName = StrUtil.isBlank(disabledFieldName) ? "isDisabled" : disabledFieldName;
        childrenFieldName = StrUtil.isBlank(childrenFieldName) ? "children" : childrenFieldName;

        //由于需要调用流式处理，所以需要将字段重新设置一遍，变为一个未曾改变的对象
        String finalDisabledFieldName = disabledFieldName;
        String finalLabelFieldName = labelFieldName;
        String finalIdFieldName = idFieldName;
        String finalChildrenFieldName = childrenFieldName;

        return tList.stream().map(children -> new TreeSelectVO(children, finalIdFieldName, finalLabelFieldName, finalDisabledFieldName, finalChildrenFieldName)).collect(Collectors.toList());
    }

}