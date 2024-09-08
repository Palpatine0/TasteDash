<template>
<view class="profile-page">
    <!-- User Info Section -->
    <view class="user-info">
        <view class="profile-picture">
            <image :src="userInfo.avatar" mode="aspectFill" class="profile-img"></image>
        </view>
        <view class="user-details">
            <text class="user-name">{{ userInfo.nickname }}</text>
            <view class="points-balance">
                <image src="/static/tab/coins.svg" class="points-icon"></image>
                <text class="user-points">{{ userInfo.credit }}</text>
            </view>
        </view>
    </view>

    <!-- Wallet and Balance Section -->
    <view class="wallet-balance">
        <view class="wallet-info">
            <view class="balance-card">
                <view class="balance-details">
                    <text style="font-size: 14px">您的余额</text>
                    <text class="balance-amount">¥ {{ userInfo.balance }}</text>
                </view>
                <view class="pay-button" @click="topUpToggle()">充值</view>
            </view>
        </view>
    </view>

    <!-- Orders and Share QR Code Section -->
    <view class="myorders-shareqrcode">
        <view class="wrapper" @click="orderHistoryRedirect">
            <view class="content">
                <text style="font-weight: bold;">我的订单</text>
                <br>
                <text class="view-more">点击查看</text>
            </view>
            <view class="icon-wrapper">
                <image class="icon" src="/static/tab/history.jpg"></image>
            </view>
        </view>

        <!-- Vertical Divider Line -->
        <view class="vertical-moulding"></view>

        <view class="wrapper" @click="qeShareToggle">
            <view class="content">
                <text style="font-weight: bold;">我的分享码</text>
                <br>
                <text class="view-more">点击查看</text>
            </view>
            <view class="icon-wrapper">
                <image class="icon" src="/static/tab/qrcode.svg"></image>
            </view>
        </view>
    </view>

    <!--widgets-->
    <QrShare v-if="qeShareVisible" :userInfo="userInfo"></QrShare>
    <TopUp v-if="topUpVisible" :userInfo="userInfo"></TopUp>
</view>
</template>

<script>
import QrShare from "../../components/qrShare.vue";
import TopUp from "../../components/topUp.vue";

export default {
    components: {QrShare, TopUp},
    data() {
        return {
            userInfo: {},
            userBalance: 0.00,
            qeShareVisible: false,
            topUpVisible: false,
        };
    },
    methods: {
        getUserInfo() {
            uni.request({
                url: getApp().globalData.requestUrl + '/user/getUserInfo',
                method: 'POST',
                data: {
                    id: uni.getStorageSync("uid")
                },
                success: (res) => {
                    this.userInfo = res.data;
                    console.log(this.userInfo);
                },
            });
        },
        qeShareToggle() {
            this.qeShareVisible = !this.qeShareVisible
        },
        orderHistoryRedirect() {
            uni.navigateTo({
                url: '/pages/order-history/order-history'
            })
        },
        topUpToggle() {
            this.topUpVisible = !this.topUpVisible
        }
    },
    onLoad() {
        this.getUserInfo();  // Fetch user info on page load
    }
}
</script>

<style>
.profile-page {
    background-image: url('https://i.imghippo.com/files/BKQdC1725154197.png');
    padding: 20rpx;
    height: 100vh;
}

/* User Info Section */
.user-info {
    display: flex;
    align-items: center;
    padding: 20rpx;
    border-radius: 20rpx;
    margin-bottom: 20rpx;
}

.profile-picture {
    margin-right: 20rpx;
}

.profile-img {
    width: 130rpx;
    height: 130rpx;
    border-radius: 50%;
}

.user-details {
    display: flex;
    flex-direction: column;
}

.user-name {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
}

.points-balance {
    display: flex;
    align-items: center;
    margin-top: 10rpx;
}

.points-icon {
    width: 30rpx;
    height: 30rpx;
    margin-right: 10rpx;
}

.user-points {
    font-size: 36rpx;
    color: #f8be23;
}

/* Wallet and Balance Section */
.wallet-balance {
    background-color: #fac322;
    padding: 20rpx;
    border-radius: 45rpx;
    margin-bottom: 20rpx;
    height: 160px;
}

.balance-card {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 70px;
    padding: 18px;
}

.balance-details {
    display: flex;
    flex-direction: column;
}

.balance-amount {
    font-size: 40rpx;
    font-weight: bold;
    color: #333;
    margin-top: 10rpx;
}

.pay-button {
    background-color: #fbd83f;
    color: white;
    padding: 10rpx 30rpx;
    border-radius: 50rpx;
    font-size: 44rpx;
    margin-top: 20rpx;
}

/* Orders and Favorites Section */
.myorders-shareqrcode {
    display: flex;
    padding: 26px;
    border-radius: 45rpx;
    margin-bottom: 20rpx;
    background-color: #fff;
    box-shadow: 0 5rpx 10rpx rgba(0, 0, 0, 0.05);
    justify-content: space-between;
}

.wrapper {
    justify-content: space-between;
    display: flex;
    width: 48%;
}

.content {
    margin-left: 12px;
}

.icon-wrapper {
    margin-right: 20rpx;
}

.icon {
    width: 40px;
    height: 40px;
}

.view-more {
    color: #888;
    font-size: 26rpx;
}

.vertical-moulding {
    float: left;
    width: 0.5px;
    height: 44px;
    background: #ececec;
}

</style>