// >dev
const url = 'http://localhost:8089/'
// >prd
// const url = 'http://111.231.19.137:8089/'

const urls = class {
    static m() {
        // 注册接口
        const register = `${url}register/`
        // 登录接口
        const login = `${url}admin/user/login`
        // 修改密码
        const updatePassword = `${url}admin/updatePassword`
        // 获取商家信息
        const obtainshop = `${url}obtainshop/`
        // 上传图片接口
        const uploadres = `${url}admin/dish/uploadImage`
        // 上传商家信息
        const uploadshop = `${url}uploadshop/`
        // 修改商家信息
        const modifyshop = `${url}modifyshop/`
        // 获取菜品类目
        const obtaincate = `${url}admin/category/list`
        // 获取所有类目
        const listAllcategory = `${url}admin/category/listAll`
        // 添加类目
        const addcategory = `${url}admin/category/add`
        // 添加类目
        const deleteCategory = `${url}admin/category/delete`
        // 获取菜品
        const obtaindishes = `${url}admin/dish/list`
        // 获取菜品单位
        const obtainunit = `${url}admin/unit/listAll`
        // 添加菜品单位
        const dishunit = `${url}admin/unit/add`
        // 上架菜品
        const uploaddishes = `${url}admin/dish/save`
        // 删除菜品
        const dishDelete = `${url}admin/dish/delete`
        // 修改上架的菜品
        const modifydishes = `${url}modifydishes/`
        // 下架/上架菜品
        const fromsale = `${url}admin/dish/updateOnSale`
        // 获取订单
        const getOrderList = `${url}admin/order/getOrderList`

        // 查看详细菜单
        const vieworder = `${url}admin/orderDetail/list`
        // 修改订单状态
        const updateStatus = `${url}admin/order/updateStatus`
        // 接单
        const receiving = `${url}admin/order/receiving`
        // 结账
        const checkout = `${url}admin/order/checkout`
        // 桌号管理
        const qrcode = `${url}admin/table/add`
        // 获取桌号
        const getqrcode = `${url}admin/table/list`
        // 删除桌号
        const tableDelete = `${url}admin/table/delete`
        // 数据分析：七天销售额
        const salesvolume = `${url}admin/order/salesvolume`
        return {
            register,
            login,
            obtainshop,
            uploadres,
            uploadshop,
            modifyshop,
            obtaincate,
            listAllcategory,
            deleteCategory,
            addcategory,
            obtaindishes,
            dishDelete,
            obtainunit,
            dishunit,
            uploaddishes,
            modifydishes,
            fromsale,
            getOrderList: getOrderList,
            vieworder,
            updateStatus,
            receiving,
            checkout,
            qrcode,
            getqrcode,
            tableDelete,
            salesvolume,
            updatePassword
        }
    }
}
export default urls
