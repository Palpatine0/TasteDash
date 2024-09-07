<template>
<view class="skeleton-index">
    <view class="skeleton-menu-top">
        <view></view>
        <view></view>
    </view>
    <view class="skeleton-menu-cont">
        <view class="skeleton-menu-left">
            <view v-for="(item,index) in menu_left" :key="index">{{ item }}</view>
        </view>
        <view class="skeleton-menu-right">
            <view class="skeleton-menu-root" v-for="(item,index) in menu_right" :key="index">
                <view class="skeleton-menu-img">{{ item }}</view>
                <view class="skeleton-menu-position">
                    <view>{{ item }}</view>
                    <view>{{ item }}</view>
                    <view>{{ item }}</view>
                </view>
                <view class="skeleton-menu-number">{{ item }}</view>
            </view>
        </view>
    </view>
</view>
</template>

<script>
export default {
    data() {
        return {
            menu_left: ['', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''],
            menu_right: ['', '', '', '', '']
        }
    },
}
</script>

<style scoped>
.skeleton-menu-top {
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.skeleton-menu-top view {
    background-color: #f3f3f3;
    height: 50rpx;
    border-radius: 7rpx;
    margin: 20rpx;
}

.skeleton-menu-top view:nth-child(1) {
    width: 65%;
}

.skeleton-menu-top view:nth-child(2) {
    width: 200rpx;
}

.skeleton-menu-cont {
    display: flex;
    margin: 20rpx;
}

.skeleton-menu-left view {
    height: 50rpx;
    width: 150rpx;
    border-radius: 7rpx;
    background-color: #f3f3f3;
    margin-bottom: 20rpx;
}

.skeleton-menu-right {
    flex: 1;
}

.skeleton-menu-root {
    display: flex;
    height: 150rpx;
    justify-content: space-between;
    margin-bottom: 45rpx;
}

.skeleton-menu-img {
    width: 150rpx;
    height: 150rpx;
    border-radius: 7rpx;
    background-color: #f3f3f3;
    margin: 0 20rpx;
}

.skeleton-menu-position view {
    width: 100rpx;
    height: 40rpx;
    border-radius: 7rpx;
    background-color: #f3f3f3;
}

.skeleton-menu-position {
    position: relative;
    flex: 1;
}

.skeleton-menu-position view:nth-child(1) {
    margin-bottom: 10rpx;
}

.skeleton-menu-position view:nth-child(3) {
    position: absolute;
    bottom: 0;
}

.skeleton-menu-number {
    border-radius: 7rpx;
    background-color: #f3f3f3;
    height: 40rpx;
    width: 200rpx;
    align-self: flex-end;
}
</style>
