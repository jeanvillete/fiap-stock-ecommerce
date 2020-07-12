import { fetchAllAddresses, postNewAddress, deleteAddress } from './AddressDomain'

const defaultState = {
    addresses: []
}

const addressModel = {
    state: defaultState,
    reducers: {
        setAddresses(currentState, addresses) {
            return {...currentState, addresses}
        }
    },
    effects: {
        async findAll(userId) {
            return fetchAllAddresses(userId)
                .then(this.setAddresses)
        },
        async persistNewAddress({userId, address}) {
            return postNewAddress(userId, address)
                .then(addressCreated => {
                    this.findAll(userId)

                    return addressCreated
                })
        },
        async removeAddressByCode({userId, addressCode}) {
            return deleteAddress(userId, addressCode)
                .then(() => this.findAll(userId))
        }
    }
}

export default addressModel