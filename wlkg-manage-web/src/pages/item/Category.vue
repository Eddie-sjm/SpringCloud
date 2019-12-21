<template>
  <v-card>
      <v-flex xs12 sm10>
        <v-tree url="/item/category/list"
                :isEdit="isEdit"
                @handleAdd="handleAdd"
                @handleEdit="handleEdit"
                @handleDelete="handleDelete"
                @handleClick="handleClick"
        />
      </v-flex>
  </v-card>
</template>

<script>
  export default {
    name: "category",
    data() {
      return {
        isEdit:true,
      }
    },
    methods: {
      handleAdd(node) {
        this.$http.post('/item/category/addCategory',{
            id: node.id,
            name: node.name,
            parentId: node.parentId,
            isParent: node.isParent,
            sort: node.sort
        }).then(resp =>{

        })
      },
      handleEdit(id, name) {
        this.$http.put('/item/category/updateCategory',{
            id: id,
            name:name
        }).then(resp =>{

        })
        console.log("edit... id: " + id + ", name: " + name)
      },
      handleDelete(id) {
        this.$http.delete('/item/category/deleteCategory',{
          params:{
            id: id,
          }
        }).then(resp =>{

        })
        console.log("delete ... " + id)
      },
      handleClick(node) {
        console.log(node)
      }
    }
  };
</script>

<style scoped>

</style>
