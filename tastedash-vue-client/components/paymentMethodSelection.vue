<template>
<view class="charge-container">
    <view class="mask" @click="close()"></view>
    <view class="widget-center charge-anim">
        <view class="charge-header">余额充值</view>

        <!-- Predefined Amount Buttons -->
        <view class="charge-options">
            <button class="charge-button" @click="setAmount(50)">¥50</button>
            <button class="charge-button" @click="setAmount(100)">¥100</button>
            <button class="charge-button" @click="setAmount(150)">¥150</button>
        </view>

        <!-- Custom Amount Input -->
        <view class="custom-charge">
            <input v-model="balance" type="number" placeholder="自定义金额"/>
            <button class="charge-button" @click="setCustomAmount" style="height: 45px;padding: 0 !important;background-color: gray;">充值自定义金额</button>
        </view>

        <!-- Display Selected Amount -->
        <view class="info">
            <div class="credit">充值金额: ¥{{ selectedAmount }}</div>
        </view>

        <!-- Confirm Button -->
        <button class="confirm-button" @click="confirmCharge">确认充值</button>
    </view>
</view>
</template>

<script>
export default {
    data() {
        return {
            selectedAmount: 0,
            balance: '',
        };
    },
    props: {userInfo: Object},
    methods: {
        close() {
            this.$parent.topUpToggle(false);
        },
        setAmount(amount) {
            this.selectedAmount = amount;
        },
        setCustomAmount() {
            const amount = parseFloat(this.balance);
            if (!isNaN(amount) && amount > 0) {
                this.selectedAmount = amount;
            } else {
                this.$message.error("请输入有效的金额");
            }
        },
        confirmCharge() {
            if (this.selectedAmount > 0) {
                uni.request({
                    url: getApp().globalData.requestUrl + '/user/updateUserBalance',
                    method: 'POST',
                    data: {
                        id: this.userInfo.id,
                        balance: this.selectedAmount
                    },
                    success: (res) => {
                        console.log()
                        uni.showToast({title: '成功充值¥' + this.selectedAmount, icon: 'none'});
                        this.$parent.getUserInfo();
                    },
                    fail: (err) => {
                        console.log(err)
                        uni.showToast({title: '充值失败', icon: 'none'})
                    }
                });
            } else {
                this.$message.error("请选择或输入一个充值金额");
            }
        },
    },
};
</script>

<style scoped>
@import '../style/shadow.css';

.charge-container {
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

.charge-header {
    font-size: 34rpx;
    font-weight: bold;
    margin-bottom: 20rpx;
}

.charge-options {
    display: flex;
    justify-content: space-between;
    margin-bottom: 30rpx;
}

.charge-button {
    background-color: #812740;
    color: white;
    padding: 20rpx 0;
    border-radius: 30rpx;
    flex: 1;
    margin: 0 10rpx;
    font-size: 32rpx;
}


.charge-button:hover {
    background-color: #812740;
}

.custom-charge {
    display: flex;
    justify-content: space-between;
    margin-bottom: 30rpx;
}

.custom-charge input {
    flex: 1;
    padding: 20rpx;
    border-radius: 30rpx;
    border: 1px solid #ccc;
    margin-right: 10rpx;
    font-size: 30rpx;
}

.confirm-button {
    background-color: #f8be23;
    color: white;
    padding: 20rpx 0;
    border-radius: 30rpx;
    width: 100%;
    font-size: 32rpx;
}

.confirm-button:hover {
    background-color: #f8be23;
}

.info {
    margin: 20px 0;
}

.credit {
    font-size: 28rpx;
    color: #666;
}
</style>