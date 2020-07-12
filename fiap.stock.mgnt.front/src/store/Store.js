import { init } from '@rematch/core'
import loginModel from '../login/LoginModel'
import catalogModel from '../catalogs/CatalogModel'
import productModel from '../products/ProductModel'
import orderModel from '../orders/OrderModel'

export const initStore = middlewares => {
    return init({
        redux: {
            middlewares: middlewares
        },
        models: {
            loginModel,
            catalogModel,
            productModel,
            orderModel
        }
    })
}

let middlewares = []

const store = initStore(middlewares)

export const configureStore = () => initStore(middlewares)

export default store