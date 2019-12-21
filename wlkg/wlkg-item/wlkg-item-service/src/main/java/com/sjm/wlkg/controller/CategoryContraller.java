package com.sjm.wlkg.controller;

import com.sjm.common.enums.ExceptionEnums;
import com.sjm.common.exception.WlkgException;
import com.sjm.wlkg.pojo.Category;
import com.sjm.wlkg.service.CategoryService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryContraller {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category/list")
    public ResponseEntity<List<Category>> selectCategoryById(Long pid){
        List<Category> categories = categoryService.selectCategoryById(pid);
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/category/addCategory")
    public ResponseEntity<Integer> addCategory(@RequestBody Category category){
        System.out.println(category);
        int result = categoryService.addCategory(category);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/category/updateCategory")
    public ResponseEntity<Integer> updateCategory(@RequestBody Category category){
        int result = categoryService.updateCategory(category.getId(),category.getName());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/category/deleteCategory")
    public ResponseEntity<Integer> deleteCategory(@RequestParam("id")Long id){
        System.out.println(id);
        int result = categoryService.deleteCategory(id);
        return ResponseEntity.ok(result);
    }

    /**
     * 通过品牌id查询商品分类
     * @param bid
     * @return
     */
    @GetMapping("/category/bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid") Long bid) {
        List<Category> list = this.categoryService.queryByBrandId(bid);
        if (list == null || list.size() < 1) {
            throw new WlkgException(ExceptionEnums.LIST_NULL);
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/category/list/ids")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam("ids")List<Long> ids){
        return ResponseEntity.ok(categoryService.queryByIds(ids));
    }
}
