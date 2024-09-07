<template>
<view>
    <view class="mask" @click="close()"></view>
    <view class="widget-popup coup-anim">
        <view class="empty">
            <image src="/static/tab/qingkong.svg" mode="widthFix"></image>
            <text @click="emptyCart()">清空已点</text>
        </view>
        <block v-for="(item,index) in cartItemList" :key="index">
            <view class="item-list" v-if="item.quantity > 0">
                <view class="item-list-image">
                    <image :src="requestUrl+'/image/dish/'+item.image" mode="aspectFill"></image>
                </view>
                <view class="item-list-name">
                    <view>{{ item.name }}</view>
                    <view class="list-text">
                        <text>¥</text>
                        <text>{{ item.total_price }}</text>
                    </view>
                </view>
                <view class="item-quantity">
                    <view>
                        <image src="/static/tab/jianhao.png" mode="widthFix" @click="removeFromCart(index,item.quantity,item._id,item.cid,item.good_index,item.unitprice)"></image>
                    </view>
                    <view>{{ item.quantity }}</view>
                    <view>
                        <image src="/static/tab/jia.png" mode="widthFix" @click="addToCart(index,item.quantity,item._id,item.cid,item.good_index,item.unitprice)"></image>
                    </view>
                </view>
            </view>
        </block>
        <view style="height: 100rpx;"></view>
    </view>
</view>
</template>

<script>
export default {
    data() {
        return {
            requestUrl: getApp().globalData.requestUrl
        }
    },
    props: {cartItemList: Array},
    methods: {
        close() {
            this.$parent.cartToggle(false)
        },
        removeFromCart(index, quantity, _id, cid, good_index, unitprice) {
            const QU = quantity - 1
            this.$parent.cartTotalPriceCalculation(index, QU, _id, cid, good_index, unitprice)
        },
        addToCart(index, quantity, _id, cid, good_index, unitprice) {
            const QU = quantity + 1
            this.$parent.cartTotalPriceCalculation(index, QU, _id, cid, good_index, unitprice)
        },
        emptyCart() {
            this.$parent.emptyCart()
        }
    }
}
</script>

<style scoped>
@import '../style/shadow.css';

.empty image {
    width: 25rpx;
    height: 25rpx;
    display: block;
    padding-right: 10rpx;
}

.empty {
    font-size: 25rpx;
    color: #aaaaaa;
    height: 90rpx;
    border-bottom: 1rpx solid #f2f2f2;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    margin: 0 20rpx;
}

.item-list-image image {
    display: block;
    width: 130rpx;
    height: 130rpx;
    border-radius: 10rpx;
}

.item-quantity image {
    display: block;
    width: 50rpx;
    height: 50rpx;
}

.item-quantity {
    display: flex;
    align-items: center;
    align-self: flex-end;
    width: 200rpx;
    justify-content: space-between;
}

.item-list {
    display: flex;
    justify-content: space-between;
    padding: 0 20rpx;
    height: 130rpx;
    font-size: 30rpx;
    margin: 25rpx 0 45rpx 0;
}

.item-list-name {
    flex: 1;
    position: relative;
    padding: 0 20rpx;
}

.list-text text {
    display: block;
}

.list-text {
    display: flex;
    align-items: center;
    position: absolute;
    bottom: 0;
}

.list-text text:nth-child(1) {
    font-size: 25rpx;
    padding-right: 5rpx;
}
</style>
