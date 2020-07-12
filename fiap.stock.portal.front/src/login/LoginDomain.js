import axios from 'axios'

const USER_PORTAL_TYPE = 'customer'
const LOGIN_BASE_RESOURCE = `/api/login`

export const fetchUserIdByLogin = async (login) => {
    return axios
        .get(`${LOGIN_BASE_RESOURCE}/${USER_PORTAL_TYPE}/${login}`)
        .then(response => response.data)
}

export const postNewUser = async (login) => {
    const user = {
        login: login,
        type: USER_PORTAL_TYPE
    }

    return axios
        .post(`${LOGIN_BASE_RESOURCE}`, user)
        .then(response => response.data)
}