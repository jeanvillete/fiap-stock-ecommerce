import axios from 'axios'

export const fetchAllProducts = async (userId) => {
    return axios
        .get(`/api/stock/users/${userId}/products`)
        .then(response => response.data)
}

export const postNewProduct = async (userId, product) => {
    const {catalogId} = product

    return axios
        .post(`/api/stock/users/${userId}/catalogs/${catalogId}/products`, product)
        .then(response => response.data)
}