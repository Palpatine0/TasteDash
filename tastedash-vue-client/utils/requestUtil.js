import app from "../App.vue";

let ajaxTimes = 0;

const requestUrl = app.globalData.requestUrl

export const getBaseUrl = () => {
    return requestUrl;
}

export const requestUtil = (params) => {
    let header = {...params.header};
    header["token"] = uni.getStorageSync("token");
    ajaxTimes++;

    uni.showLoading({
        title: "加载中",
        mask: true
    });

    var start = new Date().getTime();
    while (true) if (new Date().getTime() - start > 1000 * 1) break;
    return new Promise((resolve, reject) => {
        uni.request({
            ...params,
            header: header,
            url: requestUrl + params.url,
            success: (result) => {
                resolve(result.data);
            },
            fail: (err) => {
                uni.showToast({
                    icon: 'error',
                    title: '连接服务器失败',
                    duration: 3000
                })
                reject(err);
            },
            complete: () => {
                ajaxTimes--;
                if (ajaxTimes === 0) {
                    uni.hideLoading();
                }
            }
        });
    })
}