import Vue from 'vue';
import axios from 'axios'
import {Base64} from 'js-base64'
import {MessageBox} from 'element-ui'

let instance = axios.create({
    responseType: "json",
    headers: {
        'content-type': 'application/json'
    }
})


instance.interceptors.request.use(
    config => {
        let token = localStorage.getItem("token")
        if (token) {
            config.headers.Authorization = token;
        }
        return config;
    },
    err => {
        return Promise.reject(err);
    });

instance.interceptors.response.use(
    response => {
        return response;
    },
    error => {
        if (error.response) {
            let ERRS = error.response.status
            let MSG = error.response.data.msg.msg
            let errdata = ERRS == 401 ? MSG : '服务器发生错误'
            switch (error.response.status) {
                case 401:
                    MessageBox.alert(errdata, '提示', {
                        confirmButtonText: '好的',
                        showCancelButton: false,
                        type: 'warning',
                        showClose: false,
                        closeOnClickModal: false
                    })
                    .then(() => {
                        window.location.href = '/'
                    }).catch(() => {
                    })
                    break;
            }
        }
        return Promise.reject(error.response.data)
    });

export default instance
