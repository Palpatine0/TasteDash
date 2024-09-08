<template>
<view class="payment-container">
    <view class="mask" @click="close()"></view>
    <view class="widget-center payment-anim">
        <view class="payment-header">选择支付方式</view>

        <!-- Payment Method Selection -->
        <view class="payment-methods">
            <button class="payment-button" style="background-color: #25b671" @click="payWithWeChat">微信支付</button>
            <button class="payment-button" :disabled="!balanceAdequate" @click="payWithBalance">余额支付</button>
            <div class="payment-method-title">
                您的余额:¥{{ userInfo.balance }}
                <div v-if="!balanceAdequate">(余额不足)</div>
            </div>
        </view>

    </view>
</view>
</template>

<script>
export default {
    props: {userInfo: Object, orderInfo: Object, balanceAdequate: Boolean},
    methods: {
        close() {
            this.$parent.paymentMethodSelectionToggle(false);
        },

        payWithWeChat() {
            wx.showLoading({
                title: '加载中',
            })
            uni.request({
                url: getApp().globalData.requestUrl + '/order/payWithWeChat',
                method: 'POST',
                data: {
                    oid: this.orderInfo.order_no,
                    uid: wx.getStorageSync('uid'),
                    openid: wx.getStorageSync('openid')
                },
                success: (res) => {
                    this.paid = true;
                    wx.hideLoading();
                    wx.showToast({
                        title: res.data.msg,
                    });
                    this.updateOrderPaymentStatus()
                },
                fail: (err) => {
                    wx.hideLoading();
                    uni.showToast({title: '支付失败，请重试', icon: 'none'})
                }
            })
        },
        payWithBalance() {
            wx.showLoading({
                title: '加载中',
            })
            uni.request({
                url: getApp().globalData.requestUrl + '/order/payWithBalance',
                method: 'POST',
                data: {
                    oid: this.orderInfo.id,
                    uid: wx.getStorageSync('uid'),
                },
                success: (res) => {
                    this.paid = true;
                    wx.hideLoading();
                    wx.showToast({
                        title: res.data.msg,
                    });
                    this.updateOrderPaymentStatus()
                },
                fail: (err) => {
                    wx.hideLoading();
                    uni.showToast({title: '支付失败，请重试', icon: 'none'})
                }
            })
        },
        updateOrderPaymentStatus() {
            uni.request({
                url: getApp().globalData.requestUrl + '/order/updateOrderPaymentStatus',
                method: 'POST',
                data: {
                    oid: this.orderInfo.id
                },
                success: (res) => {
                    uni.showToast({title: '支付成功', icon: 'none'})
                },
            })
        }

    },
};
</script>

<style scoped>
@import '../style/shadow.css';

.payment-container {
    z-index: 1000;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5); /* Dimmed background for focus */
}

.widget-center {
    z-index: 1100;
    background-color: #ffffff;
    border-radius: 20rpx;
    padding: 40rpx 30rpx;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
    width: 80%;
    text-align: center;
}

.payment-header {
    font-size: 34rpx;
    font-weight: bold;
    margin-bottom: 20rpx;
}

.payment-methods {
    margin-top: 20rpx;
}

.payment-method-title {
    font-size: 28rpx;
    margin-bottom: 10rpx;
}

.payment-button {
    width: 100%;
    padding: 10rpx 0;
    background-color: #feb327;
    color: #fff;
    border: none;
    border-radius: 30rpx;
    margin-bottom: 10rpx;
    font-size: 32rpx;
}

</style>