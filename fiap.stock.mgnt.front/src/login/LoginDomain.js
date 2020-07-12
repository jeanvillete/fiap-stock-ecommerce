import axios from 'axios'

const USER_STOCK_TYPE = 'stock'
const LOGIN_BASE_RESOURCE = `/api/login`

export const fetchUserIdByLogin = async (login) => {
    return axios
        .get(`${LOGIN_BASE_RESOURCE}/${USER_STOCK_TYPE}/${login}`)
        .then(response => response.data)
}

export const postNewUser = async (login) => {
    const user = {
        login: login,
        type: USER_STOCK_TYPE
    }

    return axios
        .post(`${LOGIN_BASE_RESOURCE}`, user)
        .then(response => response.data)
}