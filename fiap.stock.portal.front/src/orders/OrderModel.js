import { fetchAllOrders, putApproveOrder, putRejectOrder } from './OrderDomain'

const defaultState = {
    orders: []
}

const orderModel = {
    state: defaultState,
    reducers: {
        setOrders(currentState, orders) {
            return {...currentState, orders}
        }
    },
    effects: {
        async findAll(userId) {
            return fetchAllOrders(userId)
                .then(this.setOrders)
        },
        async approve({userId, orderCode}) {
            return putApproveOrder(userId, orderCode)
                .then(updatedOrder => {
                    this.findAll(userId)

                    return updatedOrder
                })
        },
        async reject({userId, orderCode}) {
            return putRejectOrder(userId, orderCode)
                .then(updatedOrder => {
                    this.findAll(userId)

                    return updatedOrder
                })
        }
    }
}

export default orderModel