package com.sjm.wlkg.service;

import com.sjm.wlkg.client.BrandClient;
import com.sjm.wlkg.client.CategoryClient;
import com.sjm.wlkg.client.GoodsClient;
import com.sjm.wlkg.client.SpecificationClient;
import com.sjm.wlkg.pojo.Brand;
import com.sjm.wlkg.pojo.Category;
import com.sjm.wlkg.pojo.Sku;
import com.sjm.wlkg.pojo.SpecGroup;
import com.sjm.wlkg.pojo.Spu;
import com.sjm.wlkg.pojo.SpuDetail;
import com.sjm.wlkg.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class PageService {
    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${wlkg.thymeleaf.destPath}")
    private String destPath;

    private static final Logger logger = LoggerFactory.getLogger(PageService.class);

    public Map<String, Object> loadModel(Long id) {
        Map<String, Object> pageInfos = new HashMap<>();

        Spu spu = goodsClient.querySpuById(id);

        SpuDetail spuDetail = spu.getSpuDetail();

        List<Sku> skus = spu.getSkus();

        Brand brand = brandClient.queryBrandById(spu.getBrandId());

        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

        List<SpecGroup> specs  = specificationClient.querySpecsByCid(spu.getCid3());

        pageInfos.put("spu", spu);
        pageInfos.put("title", spu.getTitle());
        pageInfos.put("subTitle", spu.getSubTitle());
        pageInfos.put("detail", spuDetail);
        pageInfos.put("skus", skus);
        pageInfos.put("brand", brand);

        pageInfos.put("categories", categories);
        pageInfos.put("specs", specs );

        return pageInfos;
    }

    /**
     * 判断某个商品的页面是否存在
     * @param id
     * @return
     */
    public boolean exists(Long id){
        System.out.println(id);

        return this.createPath(id).exists();
    }

    private File createPath(Long id) {
        System.out.println(id);
        if (id == null) {
            System.out.println(id);
            return null;
        }
        File dest = new File(this.destPath);
        if (!dest.exists()) {
            dest.mkdirs();
        }
        return new File(dest, id + ".html");
    }

    /**
     * 创建html页面
     * @param spuId
     * @throws Exception
     */
    public void createHtml(Long spuId) throws Exception {
        System.out.println(spuId);
        // 创建上下文，
        Context context = new Context();
        // 把数据加入上下文
        context.setVariables(loadModel(spuId));

        // 创建输出流，关联到一个临时文件
        File temp = new File(spuId + ".html");

        // 目标页面文件
        File dest = createPath(spuId);
        // 备份原页面文件
        File bak = new File(spuId + "_bak.html");

        try (PrintWriter writer = new PrintWriter(temp, "UTF-8")) {

            // 利用thymeleaf模板引擎生成 静态页面
            templateEngine.process("item", context, writer);

            if (dest.exists()) {
                // 如果目标文件已经存在，先备份
                dest.renameTo(bak);
            }

            // 将新页面覆盖旧页面
            FileCopyUtils.copy(temp,dest);
            // 成功后将备份页面删除
            bak.delete();
        } catch (IOException e) {
            // 失败后，将备份页面恢复
            bak.renameTo(dest);
            // 重新抛出异常，声明页面生成失败
            throw new Exception(e);
        } finally {
            // 删除临时页面
            if (temp.exists()) {
                temp.delete();
            }
        }
    }
    /**
     * 异步创建html页面
     * @param id
     */
    public void syncCreateHtml(Long id){
        ThreadUtils.execute(() -> {
            try {
                createHtml(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteHtml(Long id) {
        File file = new File(this.destPath, id + ".html");
        file.deleteOnExit();
    }
}
