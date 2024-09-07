<template>
<view class="share-container">
    <view class="mask" @click="close()"></view>
    <view class="widget-center coup-anim">
        <view class="share-header">您的分享码</view>
        <view class="qrcode-container">
            <img class="qrcode" :src="shareQr" alt="QR Code">
            <view class="share-tip">通过分享此二维码邀请他人点餐，您可获得其消费总额10%的返现额度!</view>
        </view>
        <view class="info">
            <div class="username">{{ userInfo.nickname }}</div><br>
            <div class="credit">您的额度: {{ userInfo.credit }}</div>
        </view>
        <button class="withdraw-button" @click="withdraw">提现</button>
    </view>
</view>
</template>

<script>

export default {
    data() {
        return {
            request_url: getApp().globalData.requestUrl,
        };
    },
    props: {userInfo: Object},
    methods: {
        close() {
            this.$parent.qeShareToggle(false);
        },
        withdraw() {
        },
    },
};
</script>

<style scoped>
@import '../style/shadow.css';

.share-container {
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
}


.share-header {
    font-size: 24px;
    font-weight: bold;
    margin-bottom: 20px;
}

.qrcode-container {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.qrcode {
    width: 200px;
    height: 200px;
    margin-bottom: 10px;
}

.share-tip {
    font-size: 14px;
    color: #555;
    margin-top: 10px;
}

.info {
    margin: 20px 0;
}

.username {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 10px;
}

.credit {
    font-size: 16px;
    color: #666;
}

.withdraw-button {
    width: 100%;
    padding: 10px 0;
    background-color: #812740;
    color: #fff;
    border: none;
    border-radius: 10px;
    font-size: 18px;
    cursor: pointer;
}

.withdraw-button:hover {
    background-color: #812740;
}
</style>