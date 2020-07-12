import axios from 'axios'

const url = userId => `/api/stock/users/${userId}/catalogs`

export const fetchCatalogItems = async (userId) => {
    return axios
        .get(url(userId))
        .then(response => response.data)
}

export const postNewCatalogItem = async (userId, description) => {
    const catalogItem = {
        description: description
    }

    return axios
        .post(url(userId), catalogItem)
        .then(response => response.data)
}