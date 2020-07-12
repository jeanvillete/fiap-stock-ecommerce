import { init } from '@rematch/core'
import loginModel from '../login/LoginModel'

export const initStore = middlewares => {
    return init({
        redux: {
            middlewares: middlewares
        },
        models: {
            loginModel
        }
    })
}

let middlewares = []

const store = initStore(middlewares)

export const configureStore = () => initStore(middlewares)

export default store