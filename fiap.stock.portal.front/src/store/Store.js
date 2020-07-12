import { init } from '@rematch/core'
import loginModel from '../login/LoginModel'
import productModel from '../products/ProductModel'
import orderModel from '../orders/OrderModel'
import addressModel from '../addresses/AddressModel'

export const initStore = middlewares => {
    return init({
        redux: {
            middlewares: middlewares
        },
        models: {
            loginModel,
            productModel,
            orderModel,
            addressModel
        }
    })
}

let middlewares = []

const store = initStore(middlewares)

export const configureStore = () => initStore(middlewares)

export default store