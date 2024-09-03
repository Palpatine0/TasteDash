<template>
<view>
    <view class="welcome">
        <text>欢迎来到</text>
        <text>Taste Dash</text>
    </view>
    <template>
        <view>
            <button open-type="getUserInfo" @getuserinfo="getUserInfo">微信登录</button>
        </view>
    </template>

    <view class="people-view">
        <view class="hello">请选择就餐人数</view>
        <view class="table-num">桌号：{{ table_number }}</view>
        <view class="table-num table-scroll">
            <scroll-view scroll-x="true" class="scroll-view_H" :enable-flex="true">
                <view class="table-block">
                    <block v-for="(item,index) in people" :key="index">
                        <view @click="choIce(index,item)" :class="{activetext: index === num}">{{ item }}</view>
                    </block>
                </view>
            </scroll-view>
        </view>
        <view class="start-diancan" :class="[num > -1 ? 'start-activ' : '']" @click="stArt()">开始点餐</view>
    </view>
</view>
</template>

<script>

import {requestUtil} from "../../utils/requestUtil";

export default {
    data() {
        return {
            num: -1,
            people: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
            table_number: null,
            hasUserInfo: false,
            canIUseGetUserProfile: false

        }
    },
    methods: {
        choIce(index, item) {
            this.num = index
            wx.setStorageSync('number_of_diners', item)
        },
        stArt() {
            if (this.num <= -1) return false
            wx.reLaunch({
                url: '/pages/home-page/page'
            })
        },
        /*getUserProfile(e) {
            /!*wx.getUserProfile({
                desc: '获取您的微信个人信息',
                success: (res) => {
                    this.hasUserInfo = true
                    var app = getApp()
                    console.log("userInfo")
                    console.log(res.userInfo)
                    app.globalData.userInfo = res.userInfo;
                },
                fail: function (e) {
                    wx.showToast({
                        title: '您选择了取消',
                        icon: "none",
                        duration: 1500,
                        mask: true
                    })
                }
            })*!/
            wx.getUserProfile({
                desc: '请授权后继续使用',
                success: res => {
                    let user = res.userInfo
                    wx.setStorageSync('user', user)
                    console.log('成功', res)
                    app.globalData.userInfo = res.userInfo;
                },
                fail: res => {
                    console.log('失败', res)
                }
            })
        },*/
        getUserInfo(e) {
            if (e.detail.userInfo) {
                uni.login({
                    provider: 'weixin',
                    success: (loginRes) => {
                        const {code} = loginRes;
                        console.log("get user info code")
                        console.log(code)
                        uni.request({
                            url: 'http://localhost:8089/user/getUserInfo',
                            method: 'POST',
                            data: {
                                code: code
                            },
                            success: (res) => {
                                if (res.data.code == "0") {
                                    const {openId, session_key, unionId} = res.data.userInfo;
                                    uni.setStorageSync('userInfo', res.data.userInfo);
                                    uni.setStorageSync('openId', openId);
                                    uni.setStorageSync('sessionKey', session_key);
                                    uni.setStorageSync('unionId', unionId);
                                } else {
                                    uni.showToast({title: '登录失败', icon: 'none'});
                                }
                            },
                            fail: () => {
                                uni.showToast({title: '请求失败', icon: 'none'});
                            }
                        });
                    },
                    fail: (err) => {
                        console.log('uni.login 接口调用失败,将无法正常使用开放接口等服务', err);
                        uni.showToast({title: '登录失败', icon: 'none'});
                    }
                });
            } else {
                uni.showModal({
                    title: '警告',
                    content: '您点击了拒绝授权,将无法进入小程序,请授权之后再进入!',
                    showCancel: false,
                    confirmText: '返回授权',
                    success: (res) => {
                        if (res.confirm) {
                            console.log('用户点击了“返回授权”');
                        }
                    }
                });
            }
        }
    },
    onLoad(e) {
        this.canIUseGetUserProfile = true
        this.table_number = e.number
        wx.setStorageSync('table_num', e.number)
    },
    onShow() {
        const storageInfo = uni.getStorageInfoSync();
        console.log("storageInfo storageInfo storageInfo storageInfo ")
        console.log(storageInfo)
        // get user info from cache
        var n = getApp().globalData.userInfo
        if (n.nickName != '' && n.nickName != null) {
            this.setData({
                hasUserInfo: true,
                canIUseGetUserProfile: true
            })
            wx.login({
                success: (res) => {
                    console.log(res);
                },
            })
        }
    }
}
</script>

<style>
page {
    background-image: url('https://i.imghippo.com/files/0bNg21725139568.jpg');
    background-attachment: fixed;
    background-repeat: no-repeat;
    background-size: cover;
}

.welcome {
    color: #FFFFFF;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding-top: 270rpx;
}

.welcome text {
    display: block;
    font-size: 50rpx;
}

.welcome text:nth-child(1) {
    font-family: monospace;
    padding-bottom: 20rpx;
}

/* 方块 */
.people-view {
    background-color: #FFFFFF;
    position: fixed;
    left: 30rpx;
    right: 30rpx;
    bottom: 100rpx;
    height: 550rpx;
    border-radius: 15rpx;
}

.hello {
    height: 80rpx;
    font-size: 35rpx;
    font-weight: bold;
    line-height: 80rpx;
    padding-left: 20rpx;
}

.table-num {
    font-size: 30rpx;
    padding-left: 20rpx;
}

.table-scroll {
    margin-top: 100rpx;
}

.scroll-view_H {
    white-space: nowrap;
    width: 100%;
    height: 75rpx;
}

.table-block {
    display: flex;
    align-items: center;
}

.table-block view {
    height: 75rpx;
    line-height: 75rpx;
    text-align: center;
    background-color: #f7f8f9;
    margin-right: 25rpx;
    padding: 0 70rpx;
    border-radius: 15rpx;
}

/* 按钮 */
.start-diancan {
    height: 90rpx;
    line-height: 90rpx;
    background-color: #fdf4d7;
    color: #c2c2c2;
    text-align: center;
    margin: 130rpx 20rpx 0 20rpx;
    border-radius: 15rpx;
}

/* 点击后加上颜色*/
.activetext {
    background-color: #f9dd89 !important;
}

.start-activ {
    /* background-color: #f6c947 !important; */
    color: #000000 !important;
    background: #f8be23 !important;
}
</style>
