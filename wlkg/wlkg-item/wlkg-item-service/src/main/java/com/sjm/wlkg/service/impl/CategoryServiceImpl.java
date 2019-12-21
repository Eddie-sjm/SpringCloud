package com.sjm.wlkg.service.impl;

import com.sjm.common.enums.ExceptionEnums;
import com.sjm.common.exception.WlkgException;
import com.sjm.wlkg.mapper.CategoryMapper;
import com.sjm.wlkg.pojo.Category;
import com.sjm.wlkg.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> selectCategoryById(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }

    @Override
    public int addCategory(Category category) {
        Category cate = new Category();
        cate.setId(category.getParentId());
        Category category1 = categoryMapper.selectByPrimaryKey(cate);
        if(category1 != null){
            System.out.println(category1.getId());
            if (category1.getIsParent() == true){
                return categoryMapper.insertSelective(category);
            }else {
                category1.setIsParent(true);
//                System.out.println(category1);
                categoryMapper.updateByPrimaryKeySelective(category1);
//                System.out.println(category1);
//                System.out.println(category);
                return categoryMapper.insertSelective(category);
            }
        }else {
//            System.out.println(category);
            return categoryMapper.insertSelective(category);
        }
    }

    @Override
    public int updateCategory(Long id, String name) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        System.out.println(category);
        category.setName(name);
        return categoryMapper.updateByPrimaryKeySelective(category);
    }

    @Override
    public int deleteCategory(Long id) {

        Long parentId = categoryMapper.selectByPrimaryKey(id).getParentId();

        Set<Long> set = sonId(id);
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id",set);
        int line = categoryMapper.deleteByExample(example);
        example = new Example(Category.class);
        criteria = example.createCriteria();
        criteria.andEqualTo("parentId",parentId);
        List<Category> sonCategories = categoryMapper.selectByExample(example);
        if(sonCategories == null || sonCategories.size() == 0){
            for (Category sonCategory:sonCategories)
                categoryMapper.updateParentById(parentId, 0);
        }
        return line;
    }

    public Set<Long> sonId(Long id) {
        Set<Long> ids = new HashSet<>();
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId",id);
        List<Category> categories = categoryMapper.selectByExample(example);
        ids.add(id);
        if(categories != null&&categories.size()>0){
            for (Category category:categories){
                Long sid = category.getId();
                if(category.getIsParent()){
                    Set<Long> des = sonId(sid);
                    ids.addAll(des);
                }else{
                    ids.add(sid);
                }
            }
        }

        return ids;
    }

    @Override
    public List<Category> queryByBrandId(Long bid) {
        return this.categoryMapper.queryByBrandId(bid);
    }

    @Override
    public List<String> queryNameByIds(List<Long> asList) {
        return this.categoryMapper.selectByIdList(asList).stream().map(Category::getName).collect(Collectors.toList());
    }

    @Override
    public List<Category> queryByIds(List<Long> ids) {
        List<Category> list = this.categoryMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(list)){
            throw new WlkgException(ExceptionEnums.LIST_NULL);
        }
        return list;
    }
}
