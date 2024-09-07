<template>
<view class="">
    <view class="mask" @click="close()"></view>
    <view class="center_h widget-popup coup-anim">
        <view class="item-image">
            <image :src="requestUrl+'/image/dish/'+itemDetail.itemgood.image" mode="aspectFill"></image>
            <image src="/static/tab/guanbi.png" mode="widthFix" @click="close()"></image>
        </view>
        <view class="details-padd">
            <view class="details-name">{{ itemDetail.itemgood.name }}</view>
            <view class="details-Thinning">已售 {{ itemDetail.itemgood.monthlysale }}</view>
            <view class="describe">
                <view class="details-unit-price">
                    <text>¥</text>
                    <text>{{ itemDetail.itemgood.unitprice }}</text>
                    <text>/{{ itemDetail.itemgood.unit }}</text>
                </view>
                <view class="details-quantity">
                    <view>
                        <image v-if="itemDetail.itemgood.quantity > 0" src="/static/tab/minus.jpg" @click="removeFromCart(itemDetail)"></image>
                    </view>
                    <view>
                        <text v-if="itemDetail.itemgood.quantity > 0">{{ itemDetail.itemgood.quantity }}</text>
                    </view>
                    <view>
                        <image src="/static/tab/plus.jpg" @click="addToCart(itemDetail)"></image>
                    </view>
                </view>
            </view>
        </view>
    </view>
</view>
</template>


<script>
export default {
    props: {itemDetail: Object},
    data() {
        return {
            requestUrl: getApp().globalData.requestUrl
        }
    },
    methods: {
        close() {
            this.$parent.itemDetailToggle(false)
        },
        removeFromCart(itemDetail) {
            let {index, good_index, cid, itemgood} = itemDetail
            this.$parent.removeFromCart(index, good_index, cid, itemgood)
        },
        addToCart(itemDetail) {
            let {index, good_index, cid, itemgood} = itemDetail
            this.$parent.addToCart(index, good_index, cid, itemgood)
        }
    }
}
</script>

<style scoped>
@import '../style/shadow.css';

.item-image {
    width: 100%;
    height: 500rpx;
    position: relative;
}

.item-image image:nth-child(1) {
    display: block;
    width: 100%;
    height: 500rpx;
    border-top-left-radius: 20rpx;
    border-top-right-radius: 20rpx;
}

.item-image image:nth-child(2) {
    display: block;
    width: 50rpx;
    height: 50rpx;
    position: absolute;
    top: 20rpx;
    right: 20rpx;
    border-radius: 50%;
}

.details-padd {
    padding: 20rpx 20rpx 0 20rpx;
    height: 700rpx;
}

.describe {
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.details-quantity image {
    width: 50rpx;
    height: 50rpx;
    display: block;
}

.details-quantity {
    display: flex;
    align-items: center;
    width: 200rpx;
    justify-content: space-between;
}

.details-name {
    font-size: 35rpx;
    font-weight: bold;
}

.details-Thinning {
    font-size: 30rpx;
    color: #a4a4a4;
    padding: 20rpx 0;
}

.details-unit-price {
    font-size: 30rpx;
    color: #ec702d;
    display: flex;
    align-items: baseline;
}

.details-unit-price text:nth-child(2) {
    font-size: 35rpx;
}

.details-unit-price text:nth-child(3) {
    color: #999999 !important;
}
</style>
