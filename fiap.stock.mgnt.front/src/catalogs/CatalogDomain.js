import axios from 'axios'

const STOCK_BASE_RESOURCE = `/api/stock/users`

export const fetchCatalogItems = async (login) => {
    return axios
        .get(`${STOCK_BASE_RESOURCE}/${login}/catalogs`)
        .then(response => response.data)
}

export const postNewCatalogItem = async (login, description) => {
    const catalogItem = {
        description: description
    }

    return axios
        .post(`${STOCK_BASE_RESOURCE}/${login}/catalogs`, catalogItem)
        .then(response => response.data)
}