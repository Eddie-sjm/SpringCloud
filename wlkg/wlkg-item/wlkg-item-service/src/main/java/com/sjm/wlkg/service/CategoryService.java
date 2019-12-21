package com.sjm.wlkg.service;

import com.sjm.wlkg.pojo.Category;

import java.util.List;

public interface CategoryService {

    List<Category> selectCategoryById(Long pid);

    int addCategory(Category category);

    int updateCategory(Long id, String name);

    int deleteCategory(Long id);

    List<Category> queryByBrandId(Long bid);

    List<String> queryNameByIds(List<Long> asList);

    List<Category> queryByIds(List<Long> ids);
}
