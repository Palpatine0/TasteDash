<template>
<view>
    <view>
        <!-- header -->
        <view class="top-view">
            <view>{{ number_people }}人就餐</view>
            <view class="top-view-flex">
                <image src="/static/tab/fenxiang.svg" mode="widthFix" class="top-search"></image>
            </view>
        </view>
        <view class="order-view">
            <view class="commodity">
                <!-- left -->
                <view class="order-left">
                    <scroll-view scroll-y="true" class="scroll-Hei" :scroll-with-animation="true" :enhanced="true" :show-scrollbar="false">
                        <block v-for="(item,index) in itemize" :key="index">
                            <view class="itemize-text" :class="{active: index == trigger}" @click="itemIze(index,item.cid)">
                                <text>{{ item.value }}</text>
                                <!--<text v-if="item.sele_quantity > 0">{{ item.sele_quantity }}</text>-->
                            </view>
                        </block>
                    </scroll-view>
                </view>
                <!-- right -->
                <view class="order-right">
                    <scroll-view scroll-y="true" class="scroll-Hei" :scroll-with-animation="true" :enhanced="true" :show-scrollbar="false" :scroll-into-view="scroll_into" @scroll="scroLl">
                        <block v-for="(item,index) in goods" :key="index">
                            <view :id="item.cid" class="rig-height">
                                <view class="classif">{{ item.label }}</view>
                                <view class="classif-goods" v-for="(itemgood,good_index) in item.dishList" :key="good_index" @click="popup_item(true,index,good_index,item.cid,itemgood)">
                                    <view class="goods-image">
                                        <image :src="baseUrl+'/image/dish/'+itemgood.image" mode="aspectFill"></image>
                                    </view>
                                    <view style="height: 64px;margin-top: 3px">
                                        <div class="goods-info">
                                            <text class="item-title">{{ itemgood.name }}</text>
                                        </div>
                                        <div style="width:96%;justify-content:space-between;display: flex;padding: 0px 20rpx 0 15rpx;">
                                            <view class="unit-price">
                                                <text class="Symbol">¥</text>
                                                <text class="item-title">{{ itemgood.unitprice }}</text>
                                                <text class="Thinning">/{{ itemgood.unit }}</text>
                                            </view>
                                            <view class="quantity">
                                                <view>
                                                    <image v-if="itemgood.quantity > 0" src="/static/tab/minus.jpg" mode="widthFix" @click.stop="reduce(index,good_index,item.cid,itemgood)"></image>
                                                </view>
                                                <view>
                                                    <text v-if="itemgood.quantity > 0">{{ itemgood.quantity }}</text>
                                                </view>
                                                <view>
                                                    <image src="/static/tab/plus.jpg" mode="widthFix" @click.stop="plus(index,good_index,item.cid,itemgood)"></image>
                                                </view>
                                            </view>
                                        </div>
                                    </view>
                                    <view class="view-divider"/>
                                </view>
                            </view>
                        </block>
                        <view style="height: 400rpx;"></view>
                    </scroll-view>
                </view>
            </view>
            <!-- bottom -->
            <div v-if="total_quantity > 0">
                <view class="order-bottom" @click="pop_Shopping()" :style="{'padding-bottom': Modelmes ? '68rpx' : ''}">
                    <view class="cart-box">
                        <view class="cart" style="width: 115rpx;">
                            <view class="cart-left">
                                <image src="https://i.imghippo.com/files/GIPAi1725152379.png" mode="widthFix"></image>
                            </view>
                            <view class="cart-number" v-if="total_quantity > 0">{{ total_quantity }}</view>
                        </view>
                        <view class="cart-title" v-if="total_quantity > 0">已点{{ total_quantity }}份菜品</view>
                        <view class="place-order" @click="orderConfirm">
                            <button plain="true" open-type="getUserInfo"><p>下单</p></button>
                        </view>
                    </view>
                </view>
            </div>
        </view>
    </view>

    <Cart v-if="card" :shopping_card="shopping_card"></Cart>
    <Details v-if="popupitem" :pro_details="pro_details"></Details>
    <Home v-if="exist"></Home>

</view>
</template>

<script>
import {getBaseUrl, requestUtil} from "../../utils/requestUtil.js"

const app = getApp()
const {Modelmes} = app.globalData
import Home from '../skeleton-view/home.vue'
import Cart from './components/shopping-cart.vue'
import Details from './components/goods-details.vue'
import {Code} from '../../config/order.js'

const db = wx.cloud.database()
const _ = db.command
const good_collect = db.collection('order-data')
const dishes_data = db.collection('dishes-data')
export default {
    components: {Cart, Details, Home},
    data() {
        return {
            baseUrl: '',
            exist: true,
            Modelmes,
            itemize: [],//类目
            trigger: 0,//类目选中的值
            goods: [],//所有菜品
            heightset: [],//存储右边每一个分类菜品的高度
            tophei: 0,//滚动时距离顶部的高度
            scroll_into: '',
            card: false,//购物车隐藏
            shopping_card: [],//购物车里的数据
            popupitem: false,//单个商品弹出框隐藏
            pro_details: {},//单个商品弹出框里的数据
            tmplIds: 'FANEJh9NPNhJrLpqQx7UhNerntR5GwEsLKK-95tuvNM',//模板id
            number_people: 0,//用餐人数
        }
    },
    methods: {
        // 点击类目加上背景色
        itemIze(index, cid) {
            this.trigger = index
            this.scroll_into = cid
            setTimeout(() => {
                this.scroll_into = ''
            }, 200)
        },
        // 右边菜品滚动时触发
        scroLl(event) {
            let scrollTop = event.detail.scrollTop
            if (scrollTop >= this.tophei) {//上拉
                // 当前分类商品的高度小于滚动高度时跳转下一个分类
                if (scrollTop >= this.heightset[this.trigger]) {
                    this.trigger += 1
                }
            } else {//下拉
                // 当前分类商品的高度大于滚动高度时跳转下一个分类
                if (scrollTop < this.heightset[this.trigger - 1]) {
                    this.trigger -= 1
                }
            }
            this.tophei = scrollTop
        },

        // 单个商品+
        plus(index, good_index, cid, itemgood) {
            const {quantity, image, name, unitprice, unit, id} = itemgood
            const QU = quantity + 1
            this.$set(this.goods[index].dishList[good_index], 'quantity', QU)
            const arr = {image, name, unitprice, quantity: QU, unit, total_price: unitprice * QU, id, cid, good_index}
            this.shopping_Cart(arr)
        },

        // 单个商品-
        reduce(index, good_index, cid, itemgood) {
            const {quantity, image, name, unitprice, unit, id} = itemgood
            const QU = quantity - 1
            this.$set(this.goods[index].dishList[good_index], 'quantity', QU)
            const arr = {image, name, unitprice, quantity: QU, unit, total_price: unitprice * QU, id, cid, good_index}
            this.shopping_Cart(arr)
        },

        // 添加进购物车的商品
        shopping_Cart(arr) {
            // 一：购物车没有数据，空数组：
            // 直接添加进数据
            // 二：购物车里有数据
            // 1.没有相同的菜品存在
            // 2.有相同的菜品存在
            if (this.shopping_card.length == 0) {
                // 一：购物车没有数据，空数组：
                this.shopping_card.push(arr)
            } else {
                // 二：购物车里有数据
                let itemindex = this.shopping_card.findIndex(item => item.id == arr.id)
                if (itemindex == -1) {
                    // 没有相同的菜品存在
                    this.shopping_card.unshift(arr)
                } else {
                    this.$set(this.shopping_card[itemindex], 'quantity', arr.quantity)
                    this.$set(this.shopping_card[itemindex], 'total_price', arr.total_price)
                }
            }
            console.log(this.shopping_card)
            this.qunint_of_goods()
        },

        // 计算左边各分类下添加了多少菜品
        qunint_of_goods() {
            let array = this.shopping_card
            let res = {}
            array.forEach(item => {
                if (res[item.cid]) {
                    res[item.cid] += item.quantity
                } else {
                    res[item.cid] = item.quantity
                }
            })
            let M = []
            for (let k in res) {
                M.push({cid: k, value: res[k]})
            }
            M.forEach(item => {
                let res_index = this.itemize.findIndex(iteming => iteming.cid == item.cid)
                this.$set(this.itemize[res_index], 'sele_quantity', item.value)
            })
        },


        //购物车商品加减数量
        shopping_Cart_add_sub(index, QU, id, cid, good_index, unitprice) {
            this.$set(this.shopping_card[index], 'quantity', QU)
            this.$set(this.shopping_card[index], 'total_price', QU * unitprice)
            // 根据id唯一标识查询商品的数量做到商品加减同步
            const itemcid = this.goods.findIndex(item => item.cid == cid)
            this.$set(this.goods[itemcid].dishList[good_index], 'quantity', QU)
            this.qunint_of_goods()
        },

        // 清空已点：被子组件调用
        empty_data() {
            this.shopping_card = []
            this.itemize.forEach(item => {
                item.sele_quantity = 0
            })
            this.goods.forEach(item => {
                item.dishList.forEach(T => {
                    T.quantity = 0
                })
            })
            this.pop_Shopping(false)
        },

        // 弹出或隐藏单个商品弹出框
        popup_item(value = true, index, good_index, cid, itemgood) {
            this.popupitem = value
            this.pro_details = {index, good_index, cid, itemgood}
            console.log(this.pro_details)
        },
        // 显示购物车组件
        pop_Shopping(value = true) {
            this.card = value
        },

        // 请求数据
        async dishEs() {
            const res = await requestUtil({url: "/category/listAll", method: "get"})
            const res2 = await requestUtil({url: "/dish/listAll", method: "get"})
            console.log(res)
            this.itemize = res.categoryListAll//类目
            this.goods = res2.allDish//所有菜品
            this.$nextTick(() => {
                this.goods_height()
            })
        },
        // 计算右边每个分类菜品的高度
        goods_height() {
            this.heightset = []
            let cate_height = 0
            const query = wx.createSelectorQuery()
            query.selectAll('.rig-height').boundingClientRect()
            query.exec((res) => {
                res[0].forEach((item) => {
                    cate_height += item.height
                    this.heightset.push(cate_height)
                })
                this.exist = false
            })
        },

        orderConfirm() {
            wx.requestSubscribeMessage({
                tmplIds: [this.tmplIds],
                success: (res) => {
                    this.orderSubmit()
                },
                fail: (err) => {
                    this.orderSubmit()
                }
            })
        },


        async orderSubmit() {
            wx.showLoading({title: '正在下单', mask: true})
            // 1.过滤掉总价为0的购物车里的菜品;filter:过滤
            let res = this.shopping_card.filter(item => item.total_price != 0)
            // 2.计算总价
            let sett_amount = 0
            res.forEach(item => {
                sett_amount += item.total_price
            })
            // 取出本地缓存的桌号和用餐人数
            let table_number = wx.getStorageSync('table_num')
            let number_of_diners = wx.getStorageSync('number_of_diners')

            let order = {
                table_number,
                number_of_diners,
                sett_amount,
                order_no: Code(),
                transac_status: 'unsettled',//结账状态
                order_receiving: 'mis_orders',//接单状态
                goods_list: res
            }
            const res2 = await requestUtil({url: "/order/saveOrder", data: order, method: "post"})
            if (res2.code == 0) {
                wx.redirectTo({
                    url: '/pages/order-details/details'
                })
                wx.hideLoading()
            }
        },

        push_message() {
            var pubsub = this.goeasy.pubsub;
            pubsub.publish({
                channel: "my_channel",//替换为您自己的channel
                message: "小程序端来的",//替换为您想要发送的消息内容
                onSuccess: () => {
                    console.log("消息发布成功。");
                },
                onFailed: (error) => {
                    console.log("消息发送失败，错误编码：" + error.code + " 错误信息：" + error.content);
                }
            });
        },

        my_order() {
            wx.navigateTo({
                url: '/pages/my-order/my-order'
            })
        }
    },
    onLoad() {
        // 获取用餐人数
        this.number_people = wx.getStorageSync('number_of_diners')
        this.baseUrl = getBaseUrl()
        this.dishEs()
    },
    computed: {
        // 计算购物车的菜品总数
        total_quantity() {
            // var
            // let
            // const
            let quantity = 0
            this.shopping_card.forEach(item => {
                quantity += item.quantity
            })
            return quantity
        }
    }
}
</script>

<style scoped>
.top-view {
    background: white;
    height: 120rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20rpx;
    position: fixed;
    top: 0;
    right: 0;
    left: 0;
}

.top-view image {
    display: block;
    width: 35rpx;
    height: 35rpx;
}

.top-view-flex {
    display: flex;
    align-items: center;
}

.top-search {
    padding-right: 50rpx;
}

/* 点餐界面 */
.order-view {
    margin-top: 120rpx;
}

.commodity {
    display: flex;
    position: fixed;
    top: 120rpx;
    left: 0;
    right: 0;
}

.order-left {
    background-color: #f6f6f6;
    width: 150rpx;
    overflow-y: auto;
}

.itemize-text {
    font-size: 27rpx;
    padding: 30rpx 10rpx;
    display: flex;
    align-items: center;
    color: #797979;
}

.itemize-text text:nth-child(1) {
    text-align: center;
    flex: 1;
}

.itemize-text text:nth-child(2) {
    text-align: center;
    background-color: #eb5941;
    border-radius: 50%;
    font-size: 20rpx;
    color: #FFFFFF;
    width: 30rpx;
    height: 30rpx;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-left: 2rpx;
}

.scroll-Hei {
    height: 100vh;
    /* white-space: nowrap; */
}

.order-right {
    background-color: #FFFFFF;
    flex: 1;
    overflow-y: auto;

}

.classif {
    font-size: 27rpx;
    font-weight: bold;
    padding: 30rpx 20rpx;
    color: #797979;
}

/* 分类商品 */
.classif-goods {
    justify-content: space-between;
    padding: 0 20rpx;
    height: 410rpx;
    font-size: 30rpx;
    margin-bottom: 45rpx;
}

.goods-image image {
    display: block;
    width: 100%;
    height: 290rpx;
    border-radius: 10rpx;
}

.goods-info {
    width: 200px;
    flex: 1;
    position: relative;
    padding: 0 20rpx;
    height: 40px;
}

.goods-info text {
    display: block;
}

.goods-name {
    display: flex;
    flex-direction: column;
    position: relative;
    top: 0;
}

.goods-name text:nth-child(1) {
    padding-bottom: 9rpx;
}

.unit-price {
    position: relative;
    bottom: 0;
    display: flex;
    align-items: baseline;
    margin-top: 4px;
}

.item-title {
    font-weight: bold;
    font-size: 14px;
}

.Symbol {
    font-size: 20rpx;
}

.Thinning {
    font-size: 25rpx;
    color: #cccccc;
}

.quantity image {
    width: 50rpx;
    height: 50rpx;
}

.quantity view {
    width: 50rpx;
    height: 50rpx;
    text-align: center;
    line-height: 50rpx;
}

.quantity {
    display: flex;
    align-items: center;
    align-self: flex-end;
    width: 200rpx;
    justify-content: space-between;
}

.order-bottom {
    height: 100rpx;
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    display: flex;
    padding: 0 12rpx;
    z-index: 9;
}

.cart-box {
    width: 100%;
    display: flex;
    background-color: #444444;
    align-items: center;
    justify-content: space-between;
    height: 45px;
    border-radius: 50px;
}

.cart image {
    width: 210rpx;
    height: 210rpx !important;
    display: block;
    margin-top: -38px;
    display: block;
}

.cart-left {
    width: 75rpx;
    height: 75rpx;
}

.cart {
    display: flex;
    align-items: center;
    /* height: 120rpx; */
}

.cart-number {
    align-self: flex-start;
    background: #eb5941;
    color: #ffff;
    width: 40rpx;
    border-radius: 50rpx;
    text-align: center;
    font-size: 20rpx;
    margin: -12px 0 0 -34px;
}

.cart-title {
    flex: 1;
    padding: 0 25rpx;
    color: #999999;
    margin-left: 26px;
}

.place-order button {
    border: none;
    background: #f8be23;
    width: 200rpx;
    height: 91rpx;
    line-height: 75rpx;
    border-radius: 50rpx;
    font-weight: bold;
    z-index: 9;
    padding-top: 4px;
    font-size: 18px;
}

/* Sidebar active style */
.active {
    background-color: #FFFFFF;
    color: #000000 !important;
    border-radius: 30rpx 0rpx 0rpx 30rpx; /* Creates an outward curve on the left and no curve on the right */
    padding: 25rpx 10rpx; /* Adjust the padding if necessary */
}

.view-divider {
    background: #f5f5f5;
    width: 100%;
    height: 1rpx;
    margin: 10px;
}
</style>
