<script>
export default {
    globalData: {
        needsTopPadding: null,
        // dev
        requestUrl: "http://localhost:8089",
        // prd
        // request_url:"http://111.231.19.137:8089"
    },
    onLaunch() {
        wx.cloud.init({
            env: 'YOUR ENV',
            traceUser: true,
        })
        this.goeasy.connect({
            id: "",
            data: {},
            onSuccess: function () {
                console.log("连接成功")
            },
            onFailed: function (error) {
                console.log("连接失败");
            },
            onProgress: function (attempts) {
                console.log("连接或自动重连中");
            }
        });
        wx.getSystemInfo({
            success: (res) => {
                if (res.safeArea.top > 40) {
                    this.globalData.needsTopPadding = true
                } else {
                    this.globalData.needsTopPadding = false
                }
            }
        })
    },
}
</script>

<style>
/*每个页面公共css */
</style>
