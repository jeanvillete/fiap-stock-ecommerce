import axios from 'axios'

const url = userId => `/api/stock/users/${userId}/products`

export const fetchAllProducts = async (userId) => {
    return axios
        .get(url(userId))
        .then(response => response.data)
}

export const postNewProduct = async (userId, product) => {
    const {catalogId} = product

    return axios
        .post(`/api/stock/users/${userId}/catalogs/${catalogId}/products`, product)
        .then(response => response.data)
}