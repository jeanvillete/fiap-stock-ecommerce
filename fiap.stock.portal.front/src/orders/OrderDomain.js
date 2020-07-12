import axios from 'axios'

export const fetchAllOrders = async (userId) => {
    return axios
        .get(`/api/portal/users/${userId}/orders`)
        .then(response => response.data)
}

export const postNewOrder = async (userId, order) => {
    return axios
        .post(`/api/portal/users/${userId}/orders`, order)
        .then(response => response.data)
}