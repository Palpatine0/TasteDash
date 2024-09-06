<template>
<div class="ordering" v-loading.fullscreen.lock="loading">
    <div class="heading">订单管理</div>
    <div class="content-view">
        <div class="query-view">
            <div class="quotation-query">
                <div>交易状态</div>
                <div>
                    <el-select v-model="statevalue" placeholder="请选择">
                        <el-option
                            v-for="item in options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                        </el-option>
                    </el-select>
                </div>
            </div>
            <div class="quotation-query">
                <el-button type="success" size="medium" @click="queryFun()">查询</el-button>
            </div>
        </div>
        <div class="button-view">
            <el-button type="warning" size="small" @click="refresh_order()">刷新订单</el-button>
        </div>
        <div v-if="nodatas">
            <div class="tab-list">
                <span v-for="(item,index) in tablist" :key="index">{{ item }}</span>
            </div>
            <div class="tab-table-quo" v-for="(item,index) in tabcont" :key="index">
                <div>{{ item.createTime }}</div>
                <div>{{ item.nickname }}</div>
                <div>{{ item.table_number }}</div>
                <div>{{ item.number_of_diners }}</div>
                <div class="remarks-text">
                    <el-button type="small" :loading="index == deta_load ? true : false" @click="detailed_menu(index,item.id)">详细菜单</el-button>
                </div>
                <div>{{ Price(item.sett_amount) }}</div>
                <div>
                    <el-button type="info" size="small" v-if="item.order_receiving == 'mis_orders'" :loading="index == rece_load ? true : false" @click="receiving(index,item.id)">待接单</el-button>
                    <el-button size="small" type="success" disabled v-if="item.transac_status == 'success'">已结账</el-button>
                    <el-button size="small" type="success" v-if="item.transac_status == 'unsettled' && item.order_receiving=='rec_order'" :loading="index == check_load ? true : false" @click="checkout(index,item.id)">待结账</el-button>
                </div>
            </div>
            <el-pagination
                background
                layout="prev, pager, next"
                :hide-on-single-page="true"
                :total="total"
                @current-change="currentchange"
            >
            </el-pagination>
        </div>
        <!-- 没有数据 -->
        <div class="nodatas" v-if="!nodatas">{{ nodvalue }}</div>
        <!-- 订单详细弹窗 -->
        <el-dialog
            title="详细菜单"
            :visible.sync="dialogVisible"
            width="500px"
            :center="true"
        >
            <div>
                <div class="menu-padd" v-for="(item,index) in user_menu" :key="index">
                    <div class="Menu-details menu-margin">
                        <span>{{ item.name }}</span>
                        <span>{{ item.quantity }}{{ item.unit }}</span>
                    </div>
                </div>
            </div>
        </el-dialog>
    </div>
    <div style="height: 120px;"></div>
</div>
</template>

<script>
// 交易状态
import {staff} from '../../../config/state-type.js'
// 价格补领
const Price = require('e-commerce_price')
export default {
    data() {
        return {
            Price: Price,
            options: staff(),
            loading: true,
            nodatas: true,
            dialogVisible: false,
            deta_load: -1,
            rece_load: -1,
            check_load: -1,
            time: '',
            statevalue: '',
            nodvalue: '没有订单数据',
            total: 0,
            pagenum: 0,
            pickerOptions: {
                disabledDate(time) {
                    return time.getTime() > Date.now();
                }
            },
            tablist: ['交易时间', '用户昵称', '桌号', '用餐人数', '菜单详情', '交易金额(元)', '交易状态'],
            tabcont: [],
            user_menu: [],
            dingdan: 0,
        }
    },
    methods: {
        currentchange(e) {
            this.pagenum = e - 1
            this.getOrderList(0)
        },
        async getOrderList(vle) {
            try {
                let res = await new this.Request(this.Urls.m().getOrderList, {"pageNum": this.pagenum + 1, "query": this.statevalue}).modepost()
                console.log(res)
                this.nodatas = res.data.total == 0 ? false : true
                this.tabcont = res.data.orderList
                this.total = res.data.total
                this.loading = false
                if (vle == 1) {
                    localStorage.setItem('order_num', 0)
                    this.$store.commit('order_remind', 0)
                }
            } catch (e) {
                new this.mytitle(this.$message, 'error', '服务器发生错误,请重试').funtitle()
            }
        },
        async detailed_menu(index, id) {
            this.deta_load = index
            try {
                let res = await new this.Request(this.Urls.m().vieworder + '?id=' + id).modeget()
                console.log(res)
                this.user_menu = res.data.list
                this.deta_load = -1
                this.dialogVisible = true
            } catch (e) {
                this.deta_load = -1
                new this.mytitle(this.$message, 'error', '服务器发生错误,请重试').funtitle()
            }
        },
        async receiving(index, id) {
            this.rece_load = index
            try {
                let res = await new this.Request(this.Urls.m().receiving, {id: id, order_receiving: 'rec_order'}).modepost()
                this.$set(this.tabcont[index], 'order_receiving', 'rec_order')
                new this.mytitle(this.$message, 'success', '执行成功').funtitle()
            } catch (e) {
                new this.mytitle(this.$message, 'error', '服务器发生错误,请重试').funtitle()
            }
        },
        async checkout(index, id) {
            this.check_load = index
            try {
                let res = await new this.Request(this.Urls.m().checkout, {id: id, transac_status: 'success'}).modepost()
                console.log(res)
                this.$set(this.tabcont[index], 'transac_status', 'success')
                new this.mytitle(this.$message, 'success', '执行成功').funtitle()
            } catch (e) {
                new this.mytitle(this.$message, 'error', '服务器发生错误,请重试').funtitle()
            }
        },
        refresh_order() {
            this.loading = true
            this.deta_load = -1,//查看详细菜单
                this.rece_load = -1,//接单
                this.check_load = -1,//结账
                this.getOrderList(1)
        },
        queryFun() {
            this.pagenum = 0
            this.getOrderList(0)
        }
    },
    created() {
        this.getOrderList(0)
        this.dingdan = localStorage.getItem("order_num")
    },
    watch: {
        "$store.state.remind"(newValue, oldValue) {
            this.dingdan = newValue.num
        }
    },
}
</script>

<style scoped="scoped">
@import url("../../../style/pubiss.css");
@import url("../../../style/popup.css");

::v-deep .el-pagination.is-background .el-pager li:not(.disabled).active {
    background-color: #00be06;
    color: #fff;
}

::v-deep .el-badge {
    margin-right: 5px;
}

.menu-padd {
    border-bottom: 1px solid #f8f8f8;
}

.Menu-details {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 50px;
}

.menu-margin {
    margin: 20px 0;
}

.menu-span {
    font-weight: bold;
    font-size: 15px;
    padding-top: 20px;
}

::v-deep .el-dialog {
    height: 600px;
    overflow-y: auto;
    border-radius: 5px !important;
}
</style>
