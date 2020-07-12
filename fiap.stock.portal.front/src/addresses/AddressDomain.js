import axios from 'axios'

export const fetchAllAddresses = async (userId) => {
    return axios
        .get(`/api/portal/users/${userId}/addresses`)
        .then(response => response.data)
}

export const postNewAddress = async (userId, address) => {
    return axios
        .post(`/api/portal/users/${userId}/addresses`, address)
        .then(response => response.data)
}


export const deleteAddress = async (userId, addressCode) => {
    return axios
        .delete(`/api/portal/users/${userId}/addresses/${addressCode}`)
        .then(response => response.data)
}
