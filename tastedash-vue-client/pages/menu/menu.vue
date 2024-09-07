<template>
<view>
    <view>
        <!-- header -->
        <view class="top-view">
            <view>{{ headcount }}人就餐</view>
            <div class="top-view-flex">
                <img :src="avatar" class="avatar" @click="meRedirect">
            </div>
        </view>
        <view class="order-view">
            <view class="commodity">
                <!-- left -->
                <view class="order-left">
                    <scroll-view scroll-y="true" class="scroll-Hei" :scroll-with-animation="true" :enhanced="true" :show-scrollbar="false">
                        <block v-for="(item,index) in categoryList" :key="index">
                            <view class="category-text" :class="{active: index == trigger}" @click="categoryNavigation(index,item.cid)">
                                <text>{{ item.value }}</text>
                                <!--<text v-if="item.sele_quantity > 0">{{ item.sele_quantity }}</text>-->
                            </view>
                        </block>
                    </scroll-view>
                </view>
                <!-- right -->
                <view class="order-right">
                    <scroll-view scroll-y="true" class="scroll-Hei" :scroll-with-animation="true" :enhanced="true" :show-scrollbar="false" :scroll-into-view="scrollInto" @scroll="scroll">
                        <block v-for="(item,index) in itemList" :key="index">
                            <view :id="item.cid" class="rig-height">
                                <view class="classif">{{ item.label }}</view>
                                <view class="classif-goods" v-for="(itemgood,good_index) in item.dishList" :key="good_index" @click="itemDetailToggle(true,index,good_index,item.cid,itemgood)">
                                    <view class="goods-image">
                                        <image :src="requestUrl+'/image/dish/'+itemgood.image" mode="aspectFill"></image>
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
                                                    <image v-if="itemgood.quantity > 0" src="/static/tab/minus.jpg" mode="widthFix" @click.stop="removeFromCart(index,good_index,item.cid,itemgood)"></image>
                                                </view>
                                                <view>
                                                    <text v-if="itemgood.quantity > 0">{{ itemgood.quantity }}</text>
                                                </view>
                                                <view>
                                                    <image src="/static/tab/plus.jpg" mode="widthFix" @click.stop="addToCart(index,good_index,item.cid,itemgood)"></image>
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
                <view class="order-bottom" @click="cartToggle()" :style="{'padding-bottom': needsTopPadding ? '68rpx' : ''}">
                    <view class="cart-box">
                        <view class="cart" style="width: 115rpx;">
                            <view class="cart-left">
                                <image src="https://i.imghippo.com/files/GIPAi1725152379.png" mode="widthFix" style="z-index: 100"></image>
                            </view>
                            <view class="cart-number" v-if="total_quantity > 0">{{ total_quantity }}</view>
                        </view>
                        <view class="cart-title" v-if="total_quantity > 0">已点{{ total_quantity }}份菜品</view>
                        <view class="place-order" @click="orderConfirm">
                            <button plain="true"><p>下单</p></button>
                        </view>
                    </view>
                </view>
            </div>
        </view>
    </view>

    <!--widgets-->
    <Cart v-if="cardVisible" :cartItemList="cartItemList"></Cart>
    <Details v-if="itemDetailVisible" :itemDetail="itemDetail"></Details>
    <SkeletonMenu v-if="skeletonVisible"></SkeletonMenu>

</view>
</template>

<script>
import {requestUtil} from "../../utils/requestUtil.js"

const app = getApp()
const {needsTopPadding} = app.globalData
import SkeletonMenu from '../../components/skeleton/skeleton-menu.vue'
import Cart from '../../components/cart.vue'
import Details from '../../components/item-details.vue'
import {Code} from '../../config/order.js'

const db = wx.cloud.database()
const _ = db.command
const good_collect = db.collection('order-data')
const dishes_data = db.collection('dishes-data')
export default {
    components: {Cart, Details, SkeletonMenu},
    data() {
        return {
            requestUrl: getApp().globalData.requestUrl,

            skeletonVisible: true,
            heightset: [],
            topHeight: 0,
            needsTopPadding,
            trigger: 0,
            scrollInto: '',
            tmplIds: 'FANEJh9NPNhJrLpqQx7UhNerntR5GwEsLKK-95tuvNM',
            headcount: 0,

            avatar: "",
            userInfo: [],

            categoryList: [],

            itemDetailVisible: false,
            itemDetail: {},
            itemList: [],

            cardVisible: false,
            cartItemList: [],
        }
    },
    methods: {
        categoryNavigation(index, cid) {
            this.trigger = index
            this.scrollInto = cid
            setTimeout(() => {
                this.scrollInto = ''
            }, 200)
        },
        scroll(event) {
            let scrollTop = event.detail.scrollTop
            if (scrollTop >= this.topHeight) {
                if (scrollTop >= this.heightset[this.trigger]) {
                    this.trigger += 1
                }
            } else {
                if (scrollTop < this.heightset[this.trigger - 1]) {
                    this.trigger -= 1
                }
            }
            this.topHeight = scrollTop
        },
        addToCart(index, good_index, cid, itemgood) {
            const {quantity, image, name, unitprice, unit, id} = itemgood
            const QU = quantity + 1
            this.$set(this.itemList[index].dishList[good_index], 'quantity', QU)
            const arr = {image, name, unitprice, quantity: QU, unit, total_price: unitprice * QU, id, cid, good_index}
            this.handleCart(arr)
        },
        removeFromCart(index, good_index, cid, itemgood) {
            const {quantity, image, name, unitprice, unit, id} = itemgood
            const QU = quantity - 1
            this.$set(this.itemList[index].dishList[good_index], 'quantity', QU)
            const arr = {image, name, unitprice, quantity: QU, unit, total_price: unitprice * QU, id, cid, good_index}
            this.handleCart(arr)
        },
        handleCart(arr) {
            if (this.cartItemList.length == 0) {
                this.cartItemList.push(arr)
            } else {
                let itemindex = this.cartItemList.findIndex(item => item.id == arr.id)
                if (itemindex == -1) {
                    this.cartItemList.unshift(arr)
                } else {
                    this.$set(this.cartItemList[itemindex], 'quantity', arr.quantity)
                    this.$set(this.cartItemList[itemindex], 'total_price', arr.total_price)
                }
            }
            this.totalPriceCalculation()
        },
        totalPriceCalculation() {
            let array = this.cartItemList
            let res = {}
            array.forEach(item => {
                if (res[item.cid]) {
                    res[item.cid] += item.quantity
                } else {
                    res[item.cid] = item.quantity
                }
            })
            let arr = []
            for (let k in res) {
                arr.push({cid: k, value: res[k]})
            }
            arr.forEach(item => {
                let res_index = this.categoryList.findIndex(iteming => iteming.cid == item.cid)
                this.$set(this.categoryList[res_index], 'sele_quantity', item.value)
            })
        },
        cartTotalPriceCalculation(index, QU, id, cid, good_index, unitprice) {
            this.$set(this.cartItemList[index], 'quantity', QU)
            this.$set(this.cartItemList[index], 'total_price', QU * unitprice)
            const itemcid = this.itemList.findIndex(item => item.cid == cid)
            this.$set(this.itemList[itemcid].dishList[good_index], 'quantity', QU)
            this.totalPriceCalculation()
        },

        emptyCart() {
            this.cartItemList = []
            this.categoryList.forEach(item => {
                item.sele_quantity = 0
            })
            this.itemList.forEach(item => {
                item.dishList.forEach(T => {
                    T.quantity = 0
                })
            })
            this.cartToggle(false)
        },

        itemDetailToggle(value = true, index, good_index, cid, itemgood) {
            this.itemDetailVisible = value
            this.itemDetail = {index, good_index, cid, itemgood}
        },
        cartToggle(value = true) {
            this.cardVisible = value
        },

        async getDishesInfo() {
            const categoryList = await requestUtil({url: "/category/listAll", method: "get"})
            const itemList = await requestUtil({url: "/dish/listAll", method: "get"})
            this.categoryList = categoryList.categoryListAll
            this.itemList = itemList.allDish
            this.$nextTick(() => {
                this.itemHeightCalculation()
            })
        },
        itemHeightCalculation() {
            this.heightset = []
            let cate_height = 0
            const query = wx.createSelectorQuery()
            query.selectAll('.rig-height').boundingClientRect()
            query.exec((res) => {
                res[0].forEach((item) => {
                    cate_height += item.height
                    this.heightset.push(cate_height)
                })
                this.skeletonVisible = false
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
            let res = this.cartItemList.filter(item => item.total_price != 0)
            let sett_amount = 0
            res.forEach(item => {
                sett_amount += item.total_price
            })
            let table_number = wx.getStorageSync('tableId')
            let headcount = wx.getStorageSync('headcount')

            let order = {
                uid: uni.getStorageSync('uid'),
                table_number,
                headcount,
                sett_amount,
                order_no: Code(),
                paymentStatus: 0,
                handlingStatus: 0,
                goods_list: res
            }
            const ress = await requestUtil({url: "/order/saveOrder", data: order, method: "post"})
            if (ress.code == 0) {
                wx.navigateTo({
                    url: '/pages/order/order?oid='+ress.oid
                })
                wx.hideLoading()
            }
        },
        pushMessage() {
            var pubsub = this.goeasy.pubsub;
            pubsub.publish({
                channel: "my_channel",
                message: "来自小程序端",
                onSuccess: () => {
                    console.log("消息发布成功");
                },
                onFailed: (error) => {
                    console.log("消息发送失败，错误编码：" + error.code + " 错误信息：" + error.content);
                }
            });
        },

        orderHistoryRedirect() {
            uni.navigateTo({
                url: '/pages/order-history/order-history'
            })
        },
        meRedirect() {
            uni.navigateTo({
                url: '/pages/me/me'
            })
        }
    },
    onLoad() {
        this.headcount = wx.getStorageSync('headcount')
        this.avatar = uni.getStorageSync('avatar');
        this.getDishesInfo()
    },
    computed: {
        total_quantity() {
            let quantity = 0
            this.cartItemList.forEach(item => {
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


.top-view-flex {
    display: flex;
    align-items: center;
}

.avatar {
    height: 40px;
    width: 40px;
    border-radius: 50px;
}

.order-history {
    margin-right: 10px;
    width: 28px;
    height: 28px;
}

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

.category-text {
    font-size: 27rpx;
    padding: 30rpx 10rpx;
    display: flex;
    align-items: center;
    color: #797979;
}

.category-text text:nth-child(1) {
    text-align: center;
    flex: 1;
}

.category-text text:nth-child(2) {
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
    z-index: 100;
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
