<template>
  <div class="about"  >
    <q-form class="q-ma-lg">
      <div style="width:100%;text-align:center" >
        <div class="q-ma-lg" style="margin-left:25%; max-width: 50%">
<!--          需要捆绑的类型是 num-->
        <q-input v-model="num" label="输入学时" mask="###">

        </q-input>
        </div>
        <div class="q-mt-lg">
<!--          确认积分：addscore 查看积分：open-->
        <q-btn color="primary" @click="addScore" label="确认积分"></q-btn>
          <q-btn @click="open" label="查看积分" class="q-ml-md"></q-btn>
        </div>
      </div>
    </q-form>

    <q-separator />
    <q-form  @submit="addToDo" >
      <div style="width:100%;text-align:center">
        <div class="q-pa-md" style="margin-left:25%; max-width: 50%">
          <strong style="text-align: center">输入待办事项</strong>
        </div>
        <div class="q-pa-md" style="margin-left:25%; max-width: 50%">
          <q-input filled v-model="value1">
            <template v-slot:append>
              <q-icon name="event" class="cursor-pointer">
                <q-popup-proxy ref="qDateProxy" transition-show="scale" transition-hide="scale">
                  <q-date v-model="value1" mask="YYYY-MM-DD">
                    <div class="row items-center justify-end">
                      <q-btn v-close-popup label="Close" color="primary" flat />
                    </div>
                  </q-date>
                </q-popup-proxy>
              </q-icon>
            </template>
          </q-input>
        </div>
        <div class="q-pa-md" style="margin-left:25%; max-width: 50%">
          <q-input
            class="input-text  "
            filled

            label="输入待办事项"
            v-model="textarea"
            outlined
            autogrow
          />
        </div>
        <div class="q-pa-md" style="margin-left:25%; max-width: 50%">

          <q-btn label="Submit" style="display:block;margin:0 auto" type="submit"  color="primary"/>
        </div>
      </div>
    </q-form>
    <q-separator />

    <div class="q-mt-md" style=" width: 100%;">
      <canlendar />
    </div>

    <q-separator />
    <p style="text-align: center;" class="q-mt-sm"><strong>待办事项</strong></p>
    <div>
      <div class="row q-mt-md" v-for="(num,index) in this.todo.length" :key="index" v-bind="todo">
        <q-card :v-bind="todo" style="width: 70%;margin-left: 15%">
          <q-card-section>
            截 止 日 期:    {{ todo[index]["limittime"] }}
            <q-btn    flat color="primary" @click.prevent="finish(index)">finish</q-btn>
          </q-card-section>
          <q-card-section>
            <p style="text-align: center">
              {{todo[index]['content']}}
            </p>
          </q-card-section>

        </q-card>

      </div>
    </div>

  </div>
</template>
<script>
import canlendar from "components/canlendar";
import {date} from "../Utils/utils"
export default {
  inject:['reload'],
  components: {
    canlendar
  }
,
  data() {
    return {
      login_user:undefined,
      textarea: "",
      todo: [],
      test: [],
      num: null,
      value1: '2021-04-06',
      goalflag: 1,
      score: 0,
      api_url:process.env.API_URL
    };
  },

  methods: {
    addToDo() {
      if (
        this.textarea == null ||
        this.value1 == null ||
        this.textarea === "" ||
        this.value1 === ""
      ) {
        this.$q.notify({message:"please! check your input infomation!",position:"center"});
      }

      else {
        this.$axios
          .post(this.api_url+"/api/study/todo", {
            content: this.textarea,
            limittime: this.value1,
            userid:localStorage.getItem('userId')
          })
          .then(() => {
            this.reload()
            this.$q.notify({message:"add successfully! refresh the page to check!",position:"center"});

          }).catch(e=>console.log(e));
          }
    },
    finish(index) {
      let key = this.todo[index]["id"];
      this.$axios
        .delete(this.api_url+"/api/study/todo/" + key)
        .then(() => {      this.$q.notify({message:"GOOD JOB !",position:"center"});
          this.reload()});

    },
    open() {
      this.$axios
        .get(this.api_url+"/api/study/"+this.login_user)
        .then((res) => (this.test = res.data));
      let len = this.test.length;
      let sum = 0;
      for (let i = 0; i < len; i++) {
        sum += this.test[i]["score"];
      }

      this.$q.notify({message:
        "your total score is " +
        sum +
        "\n" +
        "if didn't update the latest data ,please click button again!"
      ,position:"center"});
    },
    addScore() {
      let now = new Date();
      let formatDate = date.formatDate(now,"YYYY-MM-DD")
      this.$axios({
        url: this.api_url+"/api/study/score",
        method: "post",
        data:{date:formatDate,score:this.num,gift_name:null,gift_finish:null,username:localStorage.getItem('username')}
      }).then(()=>{this.reload()}).catch(e=>console.log(e))


      this.$q.notify({message:
        "sucess to post data to Database you can check it maybe 10 seconds after!"
      ,position:"center"});
    },
  },
  mounted() {
    this.login_user = localStorage.getItem('username');


    this.$axios
      .get(this.api_url+"/api/study/todolist")
      .then((res2) => {

        this.todo = res2.data
        this.todo=this.todo.sort((a,b)=>{return a.limittime<=b.limittime?-1:1})})
      .catch((e) => console.log(e));
    this.$axios
      .get(this.api_url+"/api/study/"+this.login_user)
      .then((res) => (this.test = res.data));

    let today = new Date();
    let todayDate = date.formatDate(today,"YYYY-MM-DD")

    this.value1=todayDate

  },
};
</script>
<style>
.input-text {
  width: 100%;
  height: 50px;
}
</style>
