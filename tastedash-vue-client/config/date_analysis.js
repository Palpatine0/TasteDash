const db = wx.cloud.database()
const _ = db.command
const seven = db.collection('seven_day_sales')

class analysis {
    constructor() {
    }

    async sameday(time, sales_valume) {
        try {
            let query = await seven.where({time}).get()
            if (query.data.length == 0) {
                await seven.add({data: {time, sales_valume}})
            } else {
                let total_amount = Number(query.data[0].sales_valume) + sales_valume
                let final_data = parseFloat((total_amount).toFixed(10))
                await seven.doc(query.data[0]._id).update({data: {sales_valume: final_data}})
            }
        } catch (e) {
            throw '错误'
        }
    }
}

export {analysis}