import axios from 'axios'

export const fetchAllProducts = async (userId) => {
    return axios
        .get(`/api/portal/users/${userId}/products`)
        .then(response => response.data)
}
