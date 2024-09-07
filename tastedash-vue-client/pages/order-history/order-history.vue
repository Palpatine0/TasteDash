<template>
<view class="details-view">
    <div class="order-content">
        <block v-for="(order, orderIndex) in orderList" :key="orderIndex">
            <view class="food-list">
                <view class="foot-back">
                    <block v-for="(item, index) in order.goods_list" :key="index">
                        <view class="foot-deta">
                            <view>
                                <image :src="requestUrl+'/image/dish/'+item.image" mode="aspectFill" class="item-image"></image>
                            </view>
                            <view class="foot-name">
                                <text>{{ item.name }}</text>
                                <text>{{ item.quantity }}{{ item.unit }}</text>
                            </view>
                            <view class="foot-total">¥{{ item.total_price }}</view>
                        </view>
                    </block>

                    <view class="total-view">
                        <view>共 {{ order.goods_list.length }} 份</view>
                        <view class="total-price">
                            <text>总计</text>
                            <text>¥{{ Price(Number(order.sett_amount)) }}</text>
                        </view>
                    </view>
                </view>

                <view class="foot-back order-number">
                    <text>下单时间：{{ order.createTime }}</text>
                    <br>
                    <text>订单编号：{{ order.order_no }}</text>
                </view>
            </view>
            <view style="height: 60rpx;"></view>
        </block>
    </div>
</view>
</template>

<script>
import {requestUtil} from "../../utils/requestUtil.js"

const app = getApp()
const {needsTopPadding} = app.globalData
import Price from 'e-commerce_price'

export default {
    data() {
        return {
            requestUrl: getApp().globalData.requestUrl,
            
            skeletonVisible: true,
            needsTopPadding,
            Price,
            orderList: []
        }
    },
    methods: {
        async getOrderListByUid() {
            try {
                const res = await requestUtil({
                    url: "/order/getOrderListByUid",
                    data: {
                        id: uni.getStorageSync("uid")
                    },
                    method: "POST"
                })
                // Assign the returned orderList to your component's data
                this.orderList = res.orderList || []
                this.skeletonVisible = false
            } catch (e) {
                console.error(e)
            }
        },
        orderRedirect() {
            wx.reLaunch({
                url: '/pages/menu/menu'
            })
        }
    },
    onLoad() {
        this.getOrderListByUid()
    }
}

</script>

<style>
page {
    background-color: #f4f4f4;
}

.details-view {
    background-image: url('https://i.imghippo.com/files/BKQdC1725154197.png');
    position: relative;
    padding: 0 20rpx;
}

.order-remind view:nth-child(1) {
    font-size: 48rpx; /* Larger text for emphasis */
    font-weight: bold;
    color: #ffffff;
}

.order-content {
    padding-top: 20rpx; /* Space between top section and orders */
}

.food-list {
    margin-top: 20rpx;
    border-radius: 20rpx;
    box-shadow: 0 10rpx 30rpx rgba(0, 0, 0, 0.1); /* Softer shadow */
    background-color: #fff;
    padding: 20rpx;
}

.foot-back {
    border-radius: 20rpx;
    padding: 10rpx;
    margin-bottom: 20rpx;
    background-color: #f9f9f9; /* Light background for contrast */
    box-shadow: 0 5rpx 15rpx rgba(0, 0, 0, 0.05); /* Subtle shadow */
}

.foot-deta {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20rpx;
}

.item-image {
    width: 100rpx;
    height: 100rpx;
    border-radius: 10rpx;
    margin-right: 15rpx;
    box-shadow: 0 5rpx 10rpx rgba(0, 0, 0, 0.1); /* Image shadow for depth */
}

.foot-name {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    font-size: 30rpx;
}

.foot-name text:nth-child(1) {
    font-weight: bold;
    color: #333;
}

.foot-name text:nth-child(2) {
    color: #888;
}

.foot-total {
    font-weight: bold;
    font-size: 32rpx;
    color: #333;
}

.total-view {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 10rpx;
    font-size: 28rpx;
    border-top: 1rpx solid #ddd; /* Separator line */
}

.total-price {
    color: #f8be23;
    font-size: 35rpx;
    font-weight: bold;
}

.order-number {
    padding-top: 20rpx;
    font-size: 26rpx;
    color: #777;
    border-top: 1rpx solid #eee;
}

</style>