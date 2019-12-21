package com.sjm.wlkg.controller;

import com.sjm.wlkg.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class GoodsController {

    @Autowired
    private PageService pageService;

    @GetMapping("/item/{id}.html")
    public String toItemPage(Model model, @PathVariable("id")Long id){

        Map<String,Object> pageInfos = this.pageService.loadModel(id);
        model.addAllAttributes(pageInfos);

        if(!this.pageService.exists(id)){

            this.pageService.syncCreateHtml(id);
        }

        return "item";
    }

}
