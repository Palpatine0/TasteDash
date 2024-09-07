// >dev
const url = 'http://localhost:8089/'
// >prd
// const url = 'http://111.231.19.137:8089/'

const urls = class {
    static m() {
        // user
        const login = `${url}admin/user/login`
        const register = `${url}admin/user/register/`
        const updatePassword = `${url}admin/user/updatePassword`

        // dish
        const uploadres = `${url}admin/dish/uploadImage`
        const uploaddishes = `${url}admin/dish/save`
        const dishDelete = `${url}admin/dish/delete`
        const fromsale = `${url}admin/dish/updateOnSale`
        const obtaindishes = `${url}admin/dish/list`

        // category
        const listAllcategory = `${url}admin/category/listAll`
        const addcategory = `${url}admin/category/add`
        const deleteCategory = `${url}admin/category/delete`
        const obtaincate = `${url}admin/category/list`

        // unit
        const dishunit = `${url}admin/unit/add`
        const obtainunit = `${url}admin/unit/listAll`

        // order
        const getOrderList = `${url}admin/order/getOrderList`
        const updateStatus = `${url}admin/order/updateStatus`
        const updateHandlingStatus = `${url}admin/order/updateHandlingStatus`
        const vieworder = `${url}admin/orderDetail/list`
        const salesvolume = `${url}admin/order/salesvolume`

        // table
        const qrcode = `${url}admin/table/add`
        const tableDelete = `${url}admin/table/delete`
        const getqrcode = `${url}admin/table/list`

        const obtainshop = `${url}obtainshop/`
        const uploadshop = `${url}uploadshop/`
        const modifyshop = `${url}modifyshop/`
        const modifydishes = `${url}modifydishes/`

        return {
            // user
            login,
            register,
            updatePassword,

            // dish
            uploadres,
            uploaddishes,
            dishDelete,
            fromsale,
            obtaindishes,

            obtainshop,
            uploadshop,
            modifyshop,
            obtaincate,
            listAllcategory,
            deleteCategory,
            addcategory,
            obtainunit,
            dishunit,
            modifydishes,
            getOrderList,
            vieworder,
            updateStatus,
            updateHandlingStatus,
            qrcode,
            getqrcode,
            tableDelete,
            salesvolume,
        }
    }
}
export default urls
