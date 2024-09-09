<template>
<view class="details-view">
    <view class="order-top">
        <view class="order-remind">
            <view>您的订单信息</view>
            <view>下单后需要等15分钟</view>
        </view>
    </view>
    <view class="food-list">
        <view class="foot-back">
            <view class="foot-til">
                <text>我的订单</text>
            </view>
            <block v-for="(item,index) in dishList" :key="index">
                <view class="foot-deta">
                    <view>
                        <image :src="requestUrl+'/image/dish/'+item.image" mode="aspectFill"></image>
                    </view>
                    <view class="foot-name">
                        <text>{{ item.name }}</text>
                        <text>{{ item.quantity }}{{ item.unit }}</text>
                    </view>
                    <view class="foot-total">¥{{ item.total_price }}</view>
                </view>
            </block>
            <view class="total-view">
                <view>共 {{ dishCount }} 份</view>
                <view class="total-price">
                    <text>总计</text>
                    <text>¥{{ Price(Number(orderInfo.sett_amount)) }}</text>
                </view>
            </view>
        </view>
        <view class="foot-back order-number">
            <text>订单编号：{{ orderInfo.order_no }}</text>
            <text>下单时间：{{ orderInfo.createTime }}</text>
            <text>桌台名称：{{ orderInfo.table_number }}</text>
        </view>
        <view style="height: 300rpx;"></view>
    </view>

    <div class="opts">
        <view :style="{'padding-bottom': needsTopPadding ? '68rpx' : ''}" @click="orderRedirect">
            <view class="order-more">再来一单</view>
        </view>
        <view v-if="!paid" :style="{'padding-bottom': needsTopPadding ? '68rpx' : ''}" @click="paymentMethodSelectionToggle">
            <view class="pay">确认支付</view>
        </view>
    </div>

    <PaymentMethodSelection v-if="paymentMethodSelectionVisible" :userInfo="userInfo" :orderInfo="orderInfo" :balanceAdequate="balanceAdequate"></PaymentMethodSelection>
    <SkeletonOrder v-if="skeletonVisible"></SkeletonOrder>

</view>
</template>

<script>
import {requestUtil} from "../../utils/requestUtil.js"
import SkeletonOrder from '../../components/skeleton/skeleton-order.vue'
import PaymentMethodSelection from "../../components/paymentMethodSelection.vue";

const app = getApp()
const {needsTopPadding} = app.globalData

const Price = require('e-commerce_price')
export default {
    components: {PaymentMethodSelection, SkeletonOrder},
    data() {
        return {
            requestUrl: getApp().globalData.requestUrl,
            skeletonVisible: true,
            needsTopPadding,
            Price,

            orderInfo: {},
            dishCount: 0,
            dishList: [],

            userInfo: {},

            paymentMethodSelectionVisible: false,
            dialogVisible: false,

            oid: "",
            paid: false,
            balanceAdequate: false,
        }
    },
    methods: {
        async getOrder() {
            try {
                const res = await requestUtil({
                    url: "/order/getOrder",
                    data: {
                        oid: this.oid,
                    },
                    method: "POST"
                })
                let dishList = res.dishList
                this.dishCount = dishList.length
                this.orderInfo = res.order
                this.dishList = dishList
                this.skeletonVisible = false
                this.getUserInfo()
            } catch (e) {
                //TODO handle the exception
            }
        },
        getUserInfo() {
            uni.request({
                url: getApp().globalData.requestUrl + '/user/getUserInfo',
                method: 'POST',
                data: {
                    id: uni.getStorageSync("uid")
                },
                success: (res) => {
                    this.userInfo = res.data;
                    this.balanceAdequateValidation();
                },
            });
        },
        balanceAdequateValidation() {
            // Convert the amount and balance to floats to ensure accurate comparison
            const amount = parseFloat(this.orderInfo.sett_amount);
            const balance = parseFloat(this.userInfo.balance);

            // Ensure both amount and balance are valid numbers (not NaN)
            if (isNaN(amount) || isNaN(balance)) {
                // If either value is NaN, set balanceAdequate to false
                this.balanceAdequate = false;
                return;
            }
            // Check if the user's balance is sufficient to cover the required amount
            if (balance >= amount) {
                this.balanceAdequate = true;
            } else {
                this.balanceAdequate = false;
            }
        },

        orderRedirect() {
            wx.reLaunch({
                url: '/pages/menu/menu'
            })
        },
        paymentMethodSelectionToggle() {
            this.paymentMethodSelectionVisible = !this.paymentMethodSelectionVisible;
        },


    },
    onLoad(params) {
        this.oid = params.oid;
        this.getOrder()
    }
}
</script>

<style>
page {
    background-color: #f4f4f4;
}

.details-view {
    position: relative;
}

.order-top {
    background: #f8be23;
    height: 300rpx;
}

.order-remind view:nth-child(1) {
    font-size: 42rpx;
    font-weight: bold;
    padding-bottom: 20rpx;
}

.order-remind {
    height: 200rpx;
    padding: 50rpx 0 0 50rpx;
}

.food-list {
    position: absolute;
    top: 200rpx;
    left: 20rpx;
    right: 20rpx;
    border-radius: 50rpx;
    background-image: url('https://i.imghippo.com/files/BKQdC1725154197.png');
}

.foot-back {
    background-color: #fefefe;
    border-radius: 25rpx;
    padding: 0 20rpx;
    margin-bottom: 30rpx;
}

.foot-til {
    height: 100rpx;
    color: #999999;
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.foot-deta image {
    display: block;
    width: 130rpx;
    height: 130rpx;
    border-radius: 10rpx;
}

.foot-deta {
    display: flex;
    justify-content: space-between;
    height: 130rpx;
    margin: 40rpx 0;
}

.foot-name {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    padding: 0 20rpx;
    font-size: 30rpx;
}

.foot-name text:nth-child(1) {
    font-size: 31rpx !important;
    font-weight: bold;
}

.foot-name text:nth-child(2) {
    color: #666666;
}

.foot-total {
    font-weight: bold;
}

.expand-more image {
    width: 25rpx;
    height: 25rpx;
    display: block;
    padding-left: 10rpx;
}

.expand-more {
    display: flex;
    align-items: center;
    justify-content: center;
    color: #999999;
    font-size: 25rpx;
    padding: 30rpx 0;
    border-bottom: 1rpx solid #f1f1f2;
}

.total-price {
    display: flex;
    align-items: center;
    color: #333333;
    padding-left: 40rpx;
}

.total-price text:nth-child(2) {
    font-size: 35rpx;
    font-weight: bold;
    padding-left: 30rpx;
}

.total-view {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    padding: 30rpx 0;
}

.total-view view:nth-child(1) {
    color: #999999;
}

.order-number text {
    display: block;
    padding: 15rpx 0;
    font-size: 28rpx;
    color: #999999;
}

.opts {
    background-color: #fefefe;
    height: 150rpx;
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    box-shadow: 0rpx -1.9rpx 1rpx 1rpx #f9f9f9;
    padding: 0 20rpx;
    z-index: 9;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 10px;
}

.order-more {
    background: #737784;
    width: 200rpx;
    height: 75rpx;
    line-height: 75rpx;
    text-align: center;
    border-radius: 50rpx;
    font-weight: bold;
    color: white;
}

.pay {
    background: #f8be23;
    width: 200rpx;
    height: 75rpx;
    line-height: 75rpx;
    text-align: center;
    border-radius: 50rpx;
    font-weight: bold;
}
</style>
