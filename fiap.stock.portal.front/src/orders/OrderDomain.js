import axios from 'axios'

export const fetchAllOrders = async (userId) => {
    return axios
        .get(`/api/stock/users/${userId}/orders`)
        .then(response => response.data)
}

export const putApproveOrder = async (userId, orderCode) => {
    return axios
        .put(`/api/stock/users/${userId}/orders/${orderCode}/approve`)
        .then(response => response.data)
}

export const putRejectOrder = async (userId, orderCode) => {
    return axios
        .put(`/api/stock/users/${userId}/orders/${orderCode}/reject`)
        .then(response => response.data)
}
