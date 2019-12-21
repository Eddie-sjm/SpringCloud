<template>
    <div>
      <v-card>
        <v-card-title>
          <v-btn @click="addBrand"  color="info">新增品牌</v-btn>
          <v-spacer/>
          <v-text-field
            single-line
            hide-details
            append-icon="search"
            label="搜索"
            v-model="search"
          ></v-text-field>
        </v-card-title>

        <v-divider/>

        <v-data-table
          :headers="headers"
          :items="desserts"
          :pagination.sync="pagination"
          :total-items="totalDesserts"
          :loading="loading"
          class="elevation-1"
        >
          <template slot="items" slot-scope="props">
            <td class="text-xs-center">{{ props.item.id }}</td>
            <td class="text-xs-center">{{ props.item.name }}</td>
            <td class="text-xs-center">
              <img style="width: 200px; height: 50px" v-if="props.item.image" :src="props.item.image"/>
              <span v-else>无图片</span>
            </td>
            <td class="text-xs-center">{{ props.item.letter }}</td>
            <td class="text-xs-center">
              <v-btn icon @click="editBrand(props.item)">
<!--                <v-btn color="info" @click="editBrand(props.item)">-->
                <i class="el-icon-edit"/>
              </v-btn>
              <v-btn icon @click="deleteBrand(props.item.id)">
                <i class="el-icon-delete"/>
              </v-btn>
            </td>
          </template>
        </v-data-table>

        <!--弹出的对话框-->
        <v-dialog max-width="500" v-model="show" persistent>
          <v-card>
            <!--对话框的标题-->
            <v-toolbar dense dark color="primary">
              <v-toolbar-title>{{isEdit ? '修改':'新增'}}品牌</v-toolbar-title>
              <v-spacer/>
              <!--关闭窗口的按钮-->
              <v-btn icon @click="show=false"><v-icon>close</v-icon></v-btn>
            </v-toolbar>
            <!--对话框的内容，表单-->

            <v-card-text class="px-5">
              <my-brand-form @close="closeWindow" :oldBrand="oldBrand" :isEdit="isEdit" />
            </v-card-text>

          </v-card>
        </v-dialog>

      </v-card>
    </div>
</template>

<script>
  // 导入自定义的表单组件
  import MyBrandForm from './MyBrandForm'

  export default {
    name:"MyBrand",
    components:{
      MyBrandForm
    },
    data(){
          return {
            totalDesserts:0,
            desserts:[],
            loading:true,
            pagination:{},
            search:"",
            headers:[
              {text: 'Id',value: 'id', sortable: true, align: 'center'},
              { text: '名称', value: 'name', sortable: false, align: 'center'},
              { text: 'LOGO', value: 'image', sortable: false, align: 'center'},
              { text: '首字母', value: 'letter', sortable: false, align: 'center'},
              { text: '操作', sortable: false, align: 'center'},
            ],
            show:false,
            oldBrand:{},
            isEdit:false,
          }
        },
    watch: {
      pagination: {
        handler() {
          this.getDataFromServer();
        },
        deep: true
      },
      search(val) {
        this.getDataFromServer();
      }
    },
    methods:{
      getDataFromServer(){
        this.$http.get("/item/brand/page", {
          params:{
            page: this.pagination.page,
            rows: this.pagination.rowsPerPage,
            sortBy: this.pagination.sortBy,
            desc: this.pagination.descending,
            key: this.search
          }
        }).then(({data})=>{
            this.desserts = data.items;
            this.totalDesserts = data.total;
            this.loading= false;
          });
      },
      closeWindow(){
        // 关闭窗口
        this.show = false;
        // 重新加载数据
        this.getDataFromServer();
      },
      mounted() {
        this.getDataFromServer();
      },
      addBrand(){
        this.isEdit = false;
        this.show = true;
        this.oldBrand = null;
      },
      editBrand(oldBrand){
        // 根据品牌信息查询商品分类
        this.$http.get("/item/category/bid/" + oldBrand.id).then(
          ({data}) => {
            this.isEdit = true;
            // 控制弹窗可见：
            this.show = true;
            // 获取要编辑的brand
            this.oldBrand = oldBrand;
            // 回显商品分类
            this.oldBrand.categories = data;
          })
      },
      deleteBrand(id) {
        this.$message.confirm("确认要删除该参数吗？")
          .then(() => {
            this.$http.delete("/item/brand/" + id)
              .then(() => {
                this.$message.success("删除成功");
              })
              .catch(() => {
                this.$message.error("删除失败");
              })
          })
      },
    }
  }

</script>

<style scoped>

</style>
